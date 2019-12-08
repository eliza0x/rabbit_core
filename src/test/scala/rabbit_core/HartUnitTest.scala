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

class HartSimpleIncrUnitTest(hart: Hart[IF[TestSimpleIncrIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 6)
}

class HartSimpleDecrUnitTest(hart: Hart[IF[TestSimpleDecrIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 4)
}

class HartSimpleLdiUnitTest(hart: Hart[IF[TestSimpleLdiIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 10)
  step(1)
  expect(hart.io.out, 15)
}

class HartSimpleLdUnitTest(hart: Hart[IF[TestSimpleLdIM], DE[RegFile], EX[ALU], TestMA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 10)
  step(1)
  expect(hart.io.out, 10)
}

class HartSimpleStUnitTest(hart: Hart[IF[TestSimpleStIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  expect(hart.io.out, 5)
  step(1)
  expect(hart.io.out, 10)
  step(1)
  expect(hart.io.out, 15)
  step(1)
  expect(hart.io.out, 15)
  step(1)
  expect(hart.io.out, 10)
}

class HartSimpleJumpUnitTest(hart: Hart[IF[TestSimpleJumpIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  for (i <- 0 until 10) {
    step(1)
    expect(hart.io.out, 2)
    step(1)
    expect(hart.io.out, 4)
    step(1)
    expect(hart.io.out, 1)
  }
}

class HartSumUnitTest(hart: Hart[IF[TestSumIM], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  val sumCnt: Int = {
    var cnt = 3 // ldi * 3
    var r1 = 0
    do {
      r1 += 1
      cnt += 4
    } while(r1 < 10)
    cnt
  }
  step(sumCnt)
  expect(hart.io.out, (0 to 10).sum)
}
