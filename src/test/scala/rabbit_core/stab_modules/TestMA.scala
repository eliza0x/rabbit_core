package rabbit_core.stab_modules

import chisel3._
import rabbit_core.MAIO
import rabbit_core.traits.HasIO

class TestMA extends SkeletonMA((0 until 100).map(_.U))
class SkeletonMA(memInit: Seq[UInt]) extends Module with HasIO[MAIO] {
  val io = IO(new MAIO)
  val mem = RegInit(VecInit(memInit))
  when(io.mem_w) {
    mem(io.alu_out) := io.rd
  }

  when(io.mem_r) {
    io.mem_out := mem(io.alu_out)
  }.otherwise {
    io.mem_out := DontCare
  }
}
