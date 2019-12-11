package rabbit_core

import chisel3._
import chisel3.util.{Cat, log2Ceil}
import rabbit_core.Properties._
import scala.language.reflectiveCalls

class RFWrite extends Bundle {
  val valid = Bool()
  val addr = UInt(log2Ceil(PRF.CNT).W)
  val data = UInt(XLEN.W)
}

class PhysicalRegFile(
  init: Seq[UInt] = Seq.fill(PRF.CNT)(0.U)
) extends Module {
  val io = IO(new Bundle {
    val write: Vec[RFWrite] = Vec(2, Input(new RFWrite))
    val read: Vec[UInt] = Vec(4, Input(UInt(PRF.W.W)))
    val out: Vec[UInt] = Vec(4, Output(UInt(XLEN.W)))
  })
  val prf = RegInit(VecInit(init))
  val reserved = RegInit(VecInit(Seq.fill(PRF.CNT)(false.B)))

  for(i <- 0 until 4) {
    io.out(i) := prf(io.read(i))
  }

  for(i <- 0 until 2) {
    when(io.write(i).valid) {
      prf(io.write(i).addr) := io.write(i).data
      reserved(io.write(i).addr) := false.B
    }
  }
  prf(0) := 0.U
  reserved(0) := false.B
}