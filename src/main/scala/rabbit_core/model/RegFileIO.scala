package rabbit_core.model

import chisel3._
import chisel3.util.log2Ceil
import rabbit_core.Properties._

class RegFileIO extends Bundle {
  val write_addr = Input(UInt(log2Ceil(XLEN).W))
  val write_data = Input(UInt(XLEN.W))
  val read_addr1 = Input(UInt(log2Ceil(RF_CNT).W))
  val read_addr2 = Input(UInt(log2Ceil(RF_CNT).W))
  val out1 = Output(UInt(XLEN.W))
  val out2 = Output(UInt(XLEN.W))
}
