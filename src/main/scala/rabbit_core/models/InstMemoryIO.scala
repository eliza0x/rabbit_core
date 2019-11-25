package rabbit_core.models

import chisel3._
import rabbit_core.Properties._

class InstMemoryIO extends Bundle {
  val pc = Input(UInt(XLEN.W))
  val inst = Output(UInt(XLEN.W))
}
