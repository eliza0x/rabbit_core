package rabbit_core

import chisel3._
import Properties._
import rabbit_core.traits.HasIO
import rabbit_core.models.InstMemoryIO

import scala.language.reflectiveCalls
import scala.reflect.ClassTag

class IFIO extends Bundle {
  val pc_w = Input(Bool())
  val alu_out = Input(UInt(XLEN.W))
  val inst = Output(UInt(XLEN.W))
  val pc = Output(UInt(XLEN.W))
}

class IF[M <: Module with HasIO[InstMemoryIO]](implicit val IM: ClassTag[M]) extends Module with HasIO[IFIO] {
  val io = IO(new IFIO)
  val pc = RegInit(0.U(XLEN.W))
  val mem = Module(IM.runtimeClass.getConstructor().newInstance().asInstanceOf[M])
  mem.io.pc := pc
  io.inst  := mem.io.inst

  pc := Mux(io.pc_w, io.alu_out, pc + 1.U)
  io.pc := pc
}
