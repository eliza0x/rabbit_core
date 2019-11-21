package rabbit_core

import chisel3._
import chisel3.util._
import Properties._

// 適当な定数を返すRegFile
class ConstRF extends HasIO[RegFileIO] {
  val io: RegFileIO = IO(new RegFileIO)
  val rf = VecInit((0 until RF_CNT).map(_.U))
  io.out1 := rf(io.read_addr1)
  io.out2 := rf(io.read_addr2)
}

