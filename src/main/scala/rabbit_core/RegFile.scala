package rabbit_core

import chisel3._
import chisel3.util._
import rabbit_core.Properties._
import rabbit_core.traits.HasIO

// TODO: うまいボトルネックにならないような一般化した書き方が分からない
// 1クロックでn個スライドするQueueといったものがハードウェアで効率よく定義できるか怪しい
// 難しいのでとりあえず1Release, 1Offerで書いておく
class FreeRegisterList extends Module {
  val io = IO(new Bundle {
    // 開放するレジスタを表す
    val releace_required = Input(Bool())
    val releace = Input(UInt(log2Ceil(PRF.CNT).W))
    // を必要としていることを示す
    val reserve_required = Input(Bool())
    val reserved_prf_addr = Output(UInt(log2Ceil(PRF.CNT).W))
  })
  // 今空いているレジスタのリスト(Queue)
  val frl = Reg(Vec(PRF.CNT+1, UInt(log2Ceil(PRF.CNT).W)))

  // frl queueのtopが今どこか示す
  val frl_top = RegInit(PRF.CNT.U(log2Ceil(PRF.CNT).W))

  // Queueの先頭n個をOffer
  io.reserved_prf_addr(0) := frl(0)
  for (i <- 0 until PRF.CNT) {
    frl(i) := Mux(io.reserve_required, frl(i+1), frl(i))
  }
  // QueueのtopにRelease
  when (io.releace_required) {
    frl(frl_top) := io.releace
  }

  when(io.releace_required && io.reserve_required) {
    frl_top := frl_top + 1.U - 1.U
  } .elsewhen(io.releace_required) {
    frl_top := frl_top + 1.U
  } .elsewhen(io.reserve_required) {
    frl_top := frl_top - 1.U
  } .otherwise {
    frl_top := frl_top
  }
}

class RenameMap extends Module {
  val io = IO(new Bundle {
    val reserve = Input(Bool())
    val reserved_prf_num = Output(UInt(log2Ceil(PRF.CNT).W))
    val read = Vec(RM.READ_PORTS, Input(UInt(log2Ceil(PRF.CNT).W)))
    val value = Vec(RM.READ_PORTS, Output(UInt(XLEN.W)))
  })
  val free_list = new FreeRegisterList
  val rename_map = Vec(PRF.CNT, UInt(log2Ceil(RF.CNT).W))

  for (i <- 0 until RM.READ_PORTS) {
    io.value := rename_map(io.read(i))
  }
  free_list.io.reserve_required := io.reserve
  io.reserved_prf_num := free_list.io.reserved_prf_addr
}

class RFWrite extends Bundle {
  val valid = Bool()
  val addr = UInt(log2Ceil(PRF.CNT).W)
  val data = UInt(XLEN.W)
}

class RFRead extends Bundle {
  val addr = UInt(log2Ceil(PRF.CNT).W)
}

class PhysicalRegFileIO extends Bundle {
  val write = Vec(RF.WRITE_PORTS, Input(new RFWrite))
  val read = Vec(RF.READ_PORTS, Input(new RFRead))
  val out = Vec(RF.READ_PORTS, Output(UInt(XLEN.W)))
}

class PhysicalRegFile(
  init: Seq[UInt] = Seq.fill(PRF.CNT)(0.U)
) extends Module with HasIO[PhysicalRegFileIO] {
  val io = IO(new PhysicalRegFileIO)

  val prf = RegInit(VecInit(init))
  for(i <- 0 until RF.READ_PORTS) {
    io.out(i) := prf(i)
  }

  for(i <- 0 until RF.WRITE_PORTS) {
    when(io.write(i).valid) {
      prf(io.write(i).addr) := io.write(i).data
    }
  }
  prf(0) := 0.U
}
