package rabbit_core.stab_modules

import chisel3._
import rabbit_core.traits._
import rabbit_core.models._

class TestSequentialAccessIM extends SkeletonIM((0 until 255).map(_.U))

class SkeletonIM(insts: Seq[UInt]) extends Module with HasIO[InstMemoryIO] {
  val io = IO(new InstMemoryIO)
  val mem = VecInit(insts)
  io.inst := RegNext(mem(io.pc))
}