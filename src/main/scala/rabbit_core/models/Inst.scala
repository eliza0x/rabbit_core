package rabbit_core.models

import chisel3._

class Inst extends Bundle {
  val op = UInt(4.W)
  val rd = UInt(3.W)
  val rs = UInt(3.W)
  val disp6 = UInt(6.W)
}
// imm9はCat(rs, disp6) で定義する
