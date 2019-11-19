package rabbit_core

import Properties._
import chisel3._
import chisel3.util._

object Main extends App {
  chisel3.Driver.execute(Array[String](), () => new DE())
}

class DE extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(XLEN.W))
    val pc = Input(UInt(XLEN.W))

    val pc_w = Output(Bool())
    val rf_w = Output(Bool())
    val mem_w = Output(Bool())
    val rd_addr = Output(UInt(4.W))
    val rd = Output(UInt(XLEN.W))
    val rs = Output(UInt(XLEN.W))
    val source1 = Output(UInt(XLEN.W))
    val source2 = Output(UInt(XLEN.W))
  })
  val reg_file = Reg(Vec(log2Ceil(XLEN), UInt(XLEN.W)))
  val source1_sel = Wire(UInt(1.W))
  val source2_sel = Wire(UInt(3.W))
  val inst = Wire(new Inst)
  inst := io.inst.asTypeOf(new Inst)

  io.rd_addr := inst.rd
  io.rd := reg_file(inst.rd)
  io.rs := reg_file(inst.rs)

  io.source1 := MuxLookup(source1_sel, reg_file(inst.rd), Array(
    RD.id -> reg_file(inst.rd),
    PC.id -> io.pc
  ))

  io.source2 := MuxLookup(source2_sel, reg_file(inst.rs), Array(
    Disp6.id -> inst.disp6,
    Imm9.id -> Cat(inst.rs, inst.disp6),
    One.id -> 1.U,
    Zero.id -> 0.U,
    RS.id -> reg_file(inst.rs)
  ))

  // デフォルトの挙動をNOPにしておく
  val op = NOP; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id

  //  ここScalaのfor分でスマートにかける気がするがswitchの仕様を調べないとわからず
  switch(inst.op) {
    is( ADD.op) { val op =  ADD; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is( SUB.op) { val op =  SUB; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is( AND.op) { val op =  AND; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is(  OR.op) { val op =   OR; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is(ADDI.op) { val op = ADDI; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is(SUBI.op) { val op = SUBI; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is(INCR.op) { val op = INCR; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is(DECR.op) { val op = DECR; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is( LDI.op) { val op =  LDI; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is(  LD.op) { val op =   LD; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is(  ST.op) { val op =   ST; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is( BEQ.op) { val op =  BEQ; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is( BGT.op) { val op =  BGT; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
    is(JUMP.op) { val op = JUMP; io.pc_w := op.pc_w; io.rf_w := op.rf_w; io.mem_w := op.mem_w; source1_sel := op.rs1.id; source2_sel := op.rs2.id }
  }
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
object RD    extends Source1 { val id = 0.U }
object PC    extends Source1 { val id = 1.U }
sealed trait Source2 extends Source
object RS    extends Source2 { val id = 0.U }
object Disp6 extends Source2 { val id = 1.U }
object Imm9  extends Source2 { val id = 2.U }
object One   extends Source2 { val id = 3.U }
object Zero  extends Source2 { val id = 4.U }

// 命令ごとの挙動を定義, jsonかなにかから定義を自動生成するのもアリな気がしてきたけどこの規模なら問題なし
sealed trait OP {
  val op: UInt
  val rs1: Source1
  val rs2: Source2
  val rf_w: Bool
  val mem_w: Bool
  val pc_w: Bool
}

object  ADD extends OP { val op = "b0001".U; val rs1 = RD; val rs2 = RS;    val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object  SUB extends OP { val op = "b0010".U; val rs1 = RD; val rs2 = RS;    val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object  AND extends OP { val op = "b0011".U; val rs1 = RD; val rs2 = RS;    val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object   OR extends OP { val op = "b0100".U; val rs1 = RD; val rs2 = RS;    val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object ADDI extends OP { val op = "b0101".U; val rs1 = RD; val rs2 = Disp6; val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object SUBI extends OP { val op = "b0110".U; val rs1 = RD; val rs2 = Disp6; val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object INCR extends OP { val op = "b0111".U; val rs1 = RD; val rs2 = One;   val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object DECR extends OP { val op = "b1000".U; val rs1 = RD; val rs2 = One;   val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object  LDI extends OP { val op = "b1001".U; val rs1 = RD; val rs2 = Imm9;  val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object   LD extends OP { val op = "b1010".U; val rs1 = RD; val rs2 = Disp6; val rf_w = true.B;  val mem_w = false.B; val pc_w = false.B }
object   ST extends OP { val op = "b1011".U; val rs1 = RD; val rs2 = Disp6; val rf_w = false.B; val mem_w = true.B;  val pc_w = false.B }
object  BEQ extends OP { val op = "b1100".U; val rs1 = PC; val rs2 = Disp6; val rf_w = false.B; val mem_w = false.B; val pc_w = true.B  }
object  BGT extends OP { val op = "b1101".U; val rs1 = PC; val rs2 = Disp6; val rf_w = false.B; val mem_w = false.B; val pc_w = true.B  }
object JUMP extends OP { val op = "b1110".U; val rs1 = RD; val rs2 = Imm9;  val rf_w = false.B; val mem_w = false.B; val pc_w = true.B  }
object  NOP extends OP { val op = "b0000".U; val rs1 = RD; val rs2 = Zero;  val rf_w = false.B; val mem_w = false.B; val pc_w = false.B }

