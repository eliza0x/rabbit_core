package rabbit_core

import rabbit_core.stab_modules.TestSequentialAccessIM
import chisel3.iotesters._

import scala.language.reflectiveCalls

class HartUnitTest(hart: Hart[IF[InstMemory], DE[RegFile], EX[ALU], MA]) extends PeekPokeTester(hart) {
  step(10)
}
