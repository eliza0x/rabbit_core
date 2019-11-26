package rabbit_core

import chisel3._
import chisel3.util.MuxLookup
import rabbit_core.Properties.XLEN

import rabbit_core.traits.HasIO
import rabbit_core.models.ALUIO

import scala.language.reflectiveCalls
import scala.reflect.ClassTag

class EXIO extends Bundle {
  val rd = Input(UInt(XLEN.W))
  val rs = Input(UInt(XLEN.W))
  val alu_op = Input(UInt(2.W))
  val source1 = Input(UInt(XLEN.W))
  val source2 = Input(UInt(XLEN.W))
  val cond_type = Input(UInt(2.W))
  val alu_out = Output(UInt(XLEN.W))
  val pc_w = Output(Bool())
}

class EX[M <: Module with HasIO[ALUIO]](
  implicit val ALU: () => M
) extends Module with HasIO[EXIO] {
  val io = IO(new EXIO)
  val alu = Module(ALU())
  alu.io.alu_op := io.alu_op
  alu.io.source1 := io.source1
  alu.io.source2 := io.source2
  io.alu_out := alu.io.alu_out

  io.pc_w := MuxLookup(io.cond_type, DontCare, Array(
    N.id  -> false.B,
    EQ.id -> io.source1.===(io.source2),
    GT.id -> io.source1.>(io.source2),
    J.id  -> true.B
  ))
}
