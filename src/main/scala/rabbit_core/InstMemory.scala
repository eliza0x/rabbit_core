package rabbit_core

import chisel3._
import rabbit_core.Properties._
import rabbit_core.`trait`._
import rabbit_core.model._

class InstMemory extends Module with HasIO[InstMemoryIO] {
  val io = IO(new InstMemoryIO)
  val mem = SyncReadMem(Math.pow(2, XLEN).toInt, UInt(XLEN.W))
  io.inst := mem(io.pc)
}

