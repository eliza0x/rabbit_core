package rabbit_core.models

import chisel3._
import chisel3.util.Cat

class Inst extends Bundle {
  val op = UInt(4.W)
  val rd = UInt(3.W)
  val rs = UInt(3.W)
  val disp6 = UInt(6.W)
  def imm9 = Cat(rs, disp6)
}
