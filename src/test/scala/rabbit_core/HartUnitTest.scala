package rabbit_core

import rabbit_core.stab_modules._
import chisel3.iotesters._

import scala.language.reflectiveCalls

class HartSimpleAddUnitTest(hart: Hart[IF[TestSimpleAddIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 2)
  step(1)
  expect(hart.io.out, 7)
}

class HartSimpleSubUnitTest(hart: Hart[IF[TestSimpleSubIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 2)
  step(1)
  expect(hart.io.out, 3)
}

class HartSimpleAndUnitTest(hart: Hart[IF[TestSimpleAndIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 2)
  step(1)
  expect(hart.io.out, 0)
}

class HartSimpleOrUnitTest(hart: Hart[IF[TestSimpleOrIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 2)
  step(1)
  expect(hart.io.out, 7)
}

class HartSimpleAddiUnitTest(hart: Hart[IF[TestSimpleAddiIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 7)
}

class HartSimpleSubiUnitTest(hart: Hart[IF[TestSimpleSubiIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 3)
}

class HartSimpleJumpUnitTest(hart: Hart[IF[TestSimpleJumpIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  for (i <- 0 until 10) {
    step(1)
    if (i%2 == 0) {
      expect(hart.io.out, 2)
    } else {
      expect(hart.io.out, 4)
    }
  }
}
