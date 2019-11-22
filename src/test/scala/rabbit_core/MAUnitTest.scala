package rabbit_core

import chisel3.iotesters.PeekPokeTester
import scala.language.reflectiveCalls

class MAUnitTest(m: MA) extends PeekPokeTester(m) {
  poke(m.io.alu_out, 10)
  poke(m.io.mem_w, true)
  poke(m.io.mem_r, false)
  poke(m.io.rd, 20)
  step(1)
  poke(m.io.alu_out, 10)
  poke(m.io.mem_w, false)
  poke(m.io.mem_r, true)
  step(1)
  expect(m.io.mem_out, 20)
}
