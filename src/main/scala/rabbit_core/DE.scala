package rabbit_core

import Properties._
import chisel3._
import chisel3.util._

object Main extends App {
  chisel3.Driver.execute(Array[String](), () => new DE(classOf[RegFile]))
}

// RegFileのIO Interface
class RegFileIO extends Bundle {
  val write_addr = Input(UInt(log2Ceil(XLEN).W))
  val write_data = Input(UInt(XLEN.W))
  val read_addr1 = Input(UInt(log2Ceil(RF_CNT).W))
  val read_addr2 = Input(UInt(log2Ceil(RF_CNT).W))
  val out1 = Output(UInt(XLEN.W))
  val out2 = Output(UInt(XLEN.W))
}

// M型のIO Interfaceを備えたModuleを定義するためのモジュール
trait HasIO[M <: Record] extends Module {
  val io: M
}

class RegFile extends HasIO[RegFileIO] {
  val io: RegFileIO = IO(new RegFileIO)
  val reg_file = Reg(Vec(RF_CNT, UInt(XLEN.W)))
  when (io.write_addr =/= 0.U) {
    reg_file(io.write_addr) := io.write_data
  }
  io.out1 := reg_file(io.read_addr1)
  io.out2 := reg_file(io.read_addr2)
}

// RegFileIO IO Interfaceを備えたモジュールを受け取る
// -> このことでテストを書くときに外部からいい感じのRegFileを渡すことができる
// -> 例えば定数を返すRegFile等
class DE[RF <: HasIO[RegFileIO]](val RegFile: Class[RF]) extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(XLEN.W))
    val pc = Input(UInt(XLEN.W))

    val pc_w = Output(Bool())
    val rf_w = Output(Bool())
    val mem_w = Output(Bool())
    val rd_addr = Output(UInt(4.W))
    val alith = Output(UInt(2.W))
    val rd = Output(UInt(XLEN.W))
    val rs = Output(UInt(XLEN.W))
    val source1 = Output(UInt(XLEN.W))
    val source2 = Output(UInt(XLEN.W))

    val write_rf_addr = Input(UInt(XLEN.W))
    val write_rf_data = Input(UInt(XLEN.W))
  })
  val inst = Wire(new Inst)
  inst := io.inst.asTypeOf(new Inst)

  val rf = Module(new RegFile)
  val rd = Wire(UInt(XLEN.W))
  val rs = Wire(UInt(XLEN.W))
  rf.io.read_addr1 := inst.rd
  rf.io.read_addr2 := inst.rs
  rd := rf.io.out1
  rs := rf.io.out2
  rf.io.write_addr := io.write_rf_addr
  rf.io.write_data := io.write_rf_data

  val source1_sel = Wire(UInt(2.W))
  val source2_sel = Wire(UInt(3.W))

  io.rd_addr := inst.rd
  io.rd := rd
  io.rs := rs

  io.source1 := MuxLookup(source1_sel, rd, Array(
    Zero.id -> 0.U,
    RD.id -> rd,
    PC.id -> io.pc
  ))

  io.source2 := MuxLookup(source2_sel, rs, Array(
    Zero.id -> 0.U,
    One.id -> 1.U,
    Disp6.id -> inst.disp6,
    Imm9.id -> Cat(inst.rs, inst.disp6),
    RS.id -> rs
  ))

  def conOp(op: OP): Unit = {
    io.alith := op.alith.id
    io.pc_w := op.pc_w
    io.rf_w := op.rf_w
    io.mem_w := op.mem_w
    source1_sel := op.rs1.id
    source2_sel := op.rs2.id
  }

  //  ここScalaのfor分でスマートにかける気がするがswitchの仕様を調べないとわからず
  // -> switchの中ではisしか使えないみたいなので、forで展開するのは無理そう
  // -> MuxCaseはちょっと違うし、isマクロを弄るしかないのかな？やりたくねぇ……
  when(inst.op === ADD.op) { val op =  ADD; conOp(op) }
  .elsewhen(inst.op === SUB.op) { val op =  SUB; conOp(op) }
  .elsewhen(inst.op === AND.op) { val op =  AND; conOp(op) }
  .elsewhen(inst.op ===  OR.op) { val op =   OR; conOp(op) }
  .elsewhen(inst.op ===ADDI.op) { val op = ADDI; conOp(op) }
  .elsewhen(inst.op ===SUBI.op) { val op = SUBI; conOp(op) }
  .elsewhen(inst.op ===INCR.op) { val op = INCR; conOp(op) }
  .elsewhen(inst.op ===DECR.op) { val op = DECR; conOp(op) }
  .elsewhen(inst.op === LDI.op) { val op =  LDI; conOp(op) }
  .elsewhen(inst.op ===  LD.op) { val op =   LD; conOp(op) }
  .elsewhen(inst.op ===  ST.op) { val op =   ST; conOp(op) }
  .elsewhen(inst.op === BEQ.op) { val op =  BEQ; conOp(op) }
  .elsewhen(inst.op === BGT.op) { val op =  BGT; conOp(op) }
  .elsewhen(inst.op ===JUMP.op) { val op = JUMP; conOp(op) }
  .otherwise { val op = NOP; conOp(op) }
}

class Inst extends Bundle {
  val op = UInt(4.W)
  val rd = UInt(3.W)
  val rs = UInt(3.W)
  val disp6 = UInt(6.W)
}

// ALUにどの値を流すか決定するのに使用
sealed trait Source { val id: UInt }
sealed trait Source1 extends Source
object Zero  extends Source2 with Source1 { val id = 0.U }
object RD    extends Source1 { val id = 1.U }
object PC    extends Source1 { val id = 2.U }
sealed trait Source2 extends Source
object RS    extends Source2 { val id = 1.U }
object Disp6 extends Source2 { val id = 2.U }
object Imm9  extends Source2 { val id = 3.U }
object One   extends Source2 { val id = 4.U }

sealed trait Alith { val id: UInt }
object AlithADD extends Alith { val id = 0.U }
object AlithSUB extends Alith { val id = 1.U }
object AlithAND extends Alith { val id = 2.U }
object AlithOR  extends Alith { val id = 3.U }

// 命令ごとの挙動を定義, jsonかなにかから定義を自動生成するのもアリな気がしてきたけどこの規模なら問題なし
sealed trait OP {
  val op: UInt
  val alith: Alith
  val rs1: Source1
  val rs2: Source2
  val rf_w: Bool
  val mem_w: Bool
  val pc_w: Bool
}

object  ADD extends OP { val op = "b0001".U; val alith = AlithADD; val rs1 = RD;   val rs2 = RS;    val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object  SUB extends OP { val op = "b0010".U; val alith = AlithSUB; val rs1 = RD;   val rs2 = RS;    val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object  AND extends OP { val op = "b0011".U; val alith = AlithAND; val rs1 = RD;   val rs2 = RS;    val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object   OR extends OP { val op = "b0100".U; val alith = AlithOR;  val rs1 = RD;   val rs2 = RS;    val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object ADDI extends OP { val op = "b0101".U; val alith = AlithADD; val rs1 = RD;   val rs2 = Disp6; val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object SUBI extends OP { val op = "b0110".U; val alith = AlithSUB; val rs1 = RD;   val rs2 = Disp6; val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object INCR extends OP { val op = "b0111".U; val alith = AlithADD; val rs1 = RD;   val rs2 = One;   val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object DECR extends OP { val op = "b1000".U; val alith = AlithSUB; val rs1 = RD;   val rs2 = One;   val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object  LDI extends OP { val op = "b1001".U; val alith = AlithADD; val rs1 = RD;   val rs2 = Imm9;  val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object   LD extends OP { val op = "b1010".U; val alith = AlithADD; val rs1 = RD;   val rs2 = Disp6; val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object   ST extends OP { val op = "b1011".U; val alith = AlithADD; val rs1 = RD;   val rs2 = Disp6; val rf_w = false.B; val mem_w = true.B;  val pc_w = false.B }
object  BEQ extends OP { val op = "b1100".U; val alith = AlithADD; val rs1 = PC;   val rs2 = Disp6; val rf_w = false.B; val mem_w = false.B; val pc_w = true.B  }
object  BGT extends OP { val op = "b1101".U; val alith = AlithADD; val rs1 = PC;   val rs2 = Disp6; val rf_w = false.B; val mem_w = false.B; val pc_w = true.B  }
object JUMP extends OP { val op = "b1110".U; val alith = AlithADD; val rs1 = Zero; val rs2 = Imm9;  val rf_w = false.B; val mem_w = false.B; val pc_w = true.B  }
object  NOP extends OP { val op = "b0000".U; val alith = AlithADD; val rs1 = Zero; val rs2 = Zero;  val rf_w = false.B; val mem_w = false.B; val pc_w = false.B }

