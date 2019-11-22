package rabbit_core

import chisel3._
import Properties._
import rabbit_core.`trait`.HasIO
import rabbit_core.model.InstMemoryIO
import scala.language.reflectiveCalls

class IF[M <: HasIO[InstMemoryIO]](IM: Class[M]) extends Module {
  val io = IO(new Bundle {
    val pc_w = Input(Bool())
    val alu_out = Input(UInt(XLEN.W))

    val inst = Output(UInt(XLEN.W))
    val pc = Output(UInt(XLEN.W))
  })
  val pc = RegInit(0.U(XLEN.W))
  val mem = Module(IM.newInstance())
  mem.io.pc := pc
  io.inst  := mem.io.inst

  pc := Mux(io.pc_w, io.alu_out, pc + 1.U)
  printf("io.pc: %d\n", io.pc)
  io.pc := pc
}
