package rabbit_core

import chisel3._
import rabbit_core.Properties._
import rabbit_core.`trait`.HasIO
import rabbit_core.model.RegFileIO

class RegFile extends HasIO[RegFileIO] {
  val io: RegFileIO = IO(new RegFileIO)
  val reg_file = Reg(Vec(RF_CNT, UInt(XLEN.W)))
  when (io.write_addr =/= 0.U) {
    reg_file(io.write_addr) := io.write_data
  }
  reg_file(0) := 0.U
  io.out1 := reg_file(io.read_addr1)
  io.out2 := reg_file(io.read_addr2)
}
