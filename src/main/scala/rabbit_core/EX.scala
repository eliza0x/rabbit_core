package rabbit_core

import chisel3._
import chisel3.util.MuxLookup
import rabbit_core.Properties.XLEN
import rabbit_core.`trait`.HasIO
import rabbit_core.model.ALUIO

class EX[M <: HasIO[ALUIO]](ALU: Class[M]) extends Module {
  val io = IO(new Bundle {
    val rd = Input(UInt(XLEN.W))
    val rs = Input(UInt(XLEN.W))
    val alu_op = Input(UInt(2.W))
    val source1 = Input(UInt(XLEN.W))
    val source2 = Input(UInt(XLEN.W))
    val cond_type = Input(UInt(XLEN.W))
    val alu_out = Output(UInt(XLEN.W))
    val cond_valid = Output(Bool())
  })
  val alu = Module(ALU.newInstance())
  alu.io.alu_op := io.alu_op
  alu.io.source1 := io.source1
  alu.io.source2 := io.source2
  io.alu_out := alu.io.alu_out

  io.cond_valid := MuxLookup(io.cond_type, DontCare, Array(
    EQ.id -> io.source1.===(io.source2),
    GT.id -> io.source1.>(io.source2)
  ))
}
