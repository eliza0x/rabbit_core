package rabbit_core.stab_modules

import chisel3._
import rabbit_core.traits._
import rabbit_core.models._

class SkeletonIM(insts: UInt*) extends Module with HasIO[InstMemoryIO] {
  val io = IO(new InstMemoryIO)
  val mem = VecInit(insts)
  io.inst := mem(io.pc)
}