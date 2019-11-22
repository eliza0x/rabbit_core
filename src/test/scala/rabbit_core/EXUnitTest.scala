package rabbit_core

import chisel3.iotesters._
import scala.language.reflectiveCalls

class EXUnitTest(m: EX[ALU]) extends PeekPokeTester(m) {
  poke(m.io.source1, 10)
  poke(m.io.source2, 20)
  poke(m.io.alu_op, ALUADD.id)
  step(1)
  expect(m.io.alu_out, 30)
}
