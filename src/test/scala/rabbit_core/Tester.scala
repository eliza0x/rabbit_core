package rabbit_core

import chisel3.iotesters._
import rabbit_core.stab_modules.{ConstRF, TestSequentialAccessIM}

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly example.test.GCDTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly example.test.GCDTester'
  * }}}
  */

class Tester extends ChiselFlatSpec {
  // private val backendNames = Array("firrtl", "verilator")
  private val backendNames = Array("firrtl")
  for (backendName <- backendNames) {
    "IF" should s"count up pc with $backendName" in {
      Driver(() => new IF(classOf[TestSequentialAccessIM]), backendName) {
        m: IF[TestSequentialAccessIM] => new IFUnitTester(m)
      } should be(true)
    }
    "DE" should s"nothing to do with $backendName" in {
      Driver(() => (new DE(classOf[ConstRF])), backendName) {
        m: DE[ConstRF] => new DEUnitTester(m)
      } should be(true)
    }
    "EX" should s"10 + 20 = 30 with $backendName" in {
      Driver(() => (new EX(classOf[ALU])), backendName) {
        m: EX[ALU] => new EXUnitTest(m)
      } should be(true)
    }
    "MA" should s"load and store with $backendName" in {
      Driver(() => (new MA()), backendName) {
        m: MA => new MAUnitTest(m)
      } should be(true)
    }
  }
}
