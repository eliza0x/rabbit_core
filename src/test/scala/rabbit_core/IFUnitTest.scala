package rabbit_core

import chisel3.iotesters._
import rabbit_core.stab_modules.TestSequentialAccessIM
import scala.language.reflectiveCalls

class IFUnitTester(m: IF[TestSequentialAccessIM]) extends PeekPokeTester(m) {
  for(i <- 0 to 10) {
    expect(m.io.pc, i)
    step(1)
    expect(m.io.inst, i)
  }
}


