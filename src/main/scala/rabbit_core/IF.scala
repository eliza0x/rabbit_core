package rabbit_core

import chisel3._

import Properties._

class IF extends Module {
  val io = IO(new Bundle {
    val inst = Output(UInt(XLEN.W))
    val pc = Output(UInt(XLEN.W))
  })
  val pc = RegInit(0.U(XLEN.W))
  val inst_mem = SyncReadMem(Math.pow(2, XLEN).toInt, UInt(XLEN.W))
  pc := pc + 1.U
  io.pc := pc
  io.inst := inst_mem.read(pc)
}
