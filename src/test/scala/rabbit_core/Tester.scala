package rabbit_core

import chisel3.iotesters._
import scala.language.reflectiveCalls

class FRL2WayUnitTest(m: FreeRegisterList2Way) extends PeekPokeTester(m) {
  for (i <- 0 until 2) {
    poke(m.io.release_required(i), false)
    poke(m.io.release_prf_addr(i), 15)
  }
  poke(m.io.reserve_required(0), true)
  poke(m.io.reserve_required(1), true)
  expect(m.io.reserved_prf_addr(0), 0)
  expect(m.io.reserved_prf_addr(1), 1)
  step(1)
  expect(m.io.reserved_prf_addr(0), 2)
  expect(m.io.reserved_prf_addr(1), 3)
  step(1)
  expect(m.io.reserved_prf_addr(0), 4)
  expect(m.io.reserved_prf_addr(1), 5)
  poke(m.io.reserve_required(0), true)
  poke(m.io.reserve_required(1), false)
  step(1)
  expect(m.io.reserved_prf_addr(0), 5)
  expect(m.io.reserved_prf_addr(1), 6)
  poke(m.io.reserve_required(0), false)
  poke(m.io.reserve_required(1), true)
  step(1)
  expect(m.io.reserved_prf_addr(0), 6)
  expect(m.io.reserved_prf_addr(1), 7)
  poke(m.io.reserve_required(0), false)
  poke(m.io.reserve_required(1), false)
  step(1)
  expect(m.io.reserved_prf_addr(0), 6)
  expect(m.io.reserved_prf_addr(1), 7)
  poke(m.io.reserve_required(0), true)
  poke(m.io.reserve_required(1), true)
  step(1)
  expect(m.io.reserved_prf_addr(0), 8)
  expect(m.io.reserved_prf_addr(1), 9)
}

class Tester extends ChiselFlatSpec {
  private val backendNames = Array("firrtl")
  for (backendName <- backendNames) {
    behavior of "IF"
    it should s"count up pc with $backendName" in {
      chisel3.iotesters.Driver(() => new FreeRegisterList2Way, backendName) {
        m: FreeRegisterList2Way => new FRL2WayUnitTest(m)
      } should be(true)
    }
  }
}
