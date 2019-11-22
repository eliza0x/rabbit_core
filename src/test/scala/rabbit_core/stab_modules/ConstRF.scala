package rabbit_core.stab_modules

import chisel3._
import rabbit_core.`trait`.HasIO
import rabbit_core.model.RegFileIO
import rabbit_core.Properties._

// 適当な定数を返すRegFile
class ConstRF extends HasIO[RegFileIO] {
  val io: RegFileIO = IO(new RegFileIO)
  val rf = VecInit((0 until RF_CNT).map(_.U))
  io.out1 := rf(io.read_addr1)
  io.out2 := rf(io.read_addr2)
}

