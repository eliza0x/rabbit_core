package rabbit_core

import chisel3._
import rabbit_core.Properties._
import rabbit_core.traits.HasIO

import scala.language.reflectiveCalls

class MAIO extends Bundle {
  val mem_w = Input(Bool())
  val mem_r = Input(Bool())
  val rd = Input(UInt(XLEN.W))
  val alu_out = Input(UInt(XLEN.W))
  val mem_out = Output(UInt(XLEN.W))
}

class MA extends Module with HasIO[MAIO] {
  val io = IO(new MAIO)
  val mem = Mem(Math.pow(2, XLEN).toInt, UInt(XLEN.W))
  when (io.mem_w) {
    mem.write(io.alu_out, io.rd)
  }

  when (io.mem_r) {
    io.mem_out := mem.read(io.alu_out)
  } .otherwise {
    io.mem_out := DontCare
  }
}
