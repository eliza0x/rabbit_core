package rabbit_core.model

import chisel3._
import rabbit_core.Properties.XLEN

class ALUIO extends Bundle {
  val alu_op = Input(UInt(2.W))
  val source1 = Input(UInt(XLEN.W))
  val source2 = Input(UInt(XLEN.W))
  val alu_out = Output(UInt(XLEN.W))
}

