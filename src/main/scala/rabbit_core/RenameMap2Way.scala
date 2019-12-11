package rabbit_core

import chisel3._
import chisel3.util._
import rabbit_core.Properties._
import scala.language.reflectiveCalls

class RenameMap2Way extends Module {
  val io = IO(new Bundle {
    val in = Vec(4, Output(UInt(RF.W.W)))
    val out = Vec(4, Output(UInt(PRF.W.W)))
  })
  val rename_map = Vec(PRF.CNT, UInt(RF.W.W))
  for (i <- 0 until 4) {
    io.out(i) := rename_map(io.in(i))
  }
}
