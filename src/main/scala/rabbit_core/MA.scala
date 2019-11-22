package rabbit_core

import chisel3._
import rabbit_core.Properties._

class MA extends Module {
  val io = IO(new Bundle {
    val mem_w = Input(Bool())
    val rd = Output(UInt(XLEN.W))
    val alu_out = Output(UInt(XLEN.W))
    val mem_out = Output(UInt(XLEN.W))
  })
  val mem = SyncReadMem(Math.pow(2, XLEN).toInt, UInt(XLEN.W))
  when (io.mem_w) {
    mem.write(io.alu_out, io.rd)
    io.mem_out := DontCare
  } .otherwise {
    io.mem_out := mem.read(io.alu_out)
  }
}
