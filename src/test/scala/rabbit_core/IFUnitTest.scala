package rabbit_core

import chisel3.iotesters._
import rabbit_core.stab_modules.TestSequentialAccessIM
import scala.language.reflectiveCalls

class IFUnitTester(m: IF[TestSequentialAccessIM]) extends PeekPokeTester(m) {
  for(i <- 0 to 10) {
    expect(m.io.pc, i)
    poke(m.io.pc_w, false)
    step(1)
  }
}

class IFJumpUnitTester(m: IF[TestSequentialAccessIM]) extends PeekPokeTester(m) {
  expect(m.io.pc, 0)
  poke(m.io.pc_w, false)
  poke(m.io.alu_out, 10)
  step(1)
  expect(m.io.pc, 1)
  poke(m.io.pc_w, true)
  poke(m.io.alu_out, 100)
  step(1)
  expect(m.io.pc, 100)
  poke(m.io.pc_w, false)
  poke(m.io.alu_out, 1000)
  step(1)
  expect(m.io.pc, 101)
  poke(m.io.pc_w, true)
  poke(m.io.alu_out, 10000)
  step(1)
  expect(m.io.pc, 10000)
}
