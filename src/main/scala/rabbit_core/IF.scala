package rabbit_core

import chisel3._
import Properties._
import rabbit_core.`trait`.HasIO
import rabbit_core.model.InstMemoryIO
import scala.language.reflectiveCalls

class IF[M <: HasIO[InstMemoryIO]](IM: Class[M]) extends Module {
  val io = IO(new Bundle {
    val inst = Output(UInt(XLEN.W))
    val pc = Output(UInt(XLEN.W))
  })
  val pc = RegInit(0.U(XLEN.W))
  val mem = Module(IM.newInstance())
  mem.io.pc := pc
  io.inst  := mem.io.inst

  pc := pc + 1.U
  io.pc := pc
}
