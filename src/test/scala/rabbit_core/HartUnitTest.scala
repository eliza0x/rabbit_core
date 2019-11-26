package rabbit_core

import rabbit_core.stab_modules.TestSimpleAddIM
import chisel3.iotesters._

import scala.language.reflectiveCalls

class HartSimpleAddUnitTest(hart: Hart[IF[TestSimpleAddIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  step(1)
  expect(hart.io.out, 1)
  step(1)
  expect(hart.io.out, 2)
  step(1)
  expect(hart.io.out, 3)
}
