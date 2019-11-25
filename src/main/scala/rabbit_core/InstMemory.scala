package rabbit_core

import chisel3._
import rabbit_core.Properties._
import rabbit_core.traits._
import rabbit_core.models._

class InstMemory extends Module with HasIO[InstMemoryIO] {
  val io = IO(new InstMemoryIO)
  val mem = SyncReadMem(Math.pow(2, XLEN).toInt, UInt(XLEN.W))
  io.inst := mem(io.pc)
}

