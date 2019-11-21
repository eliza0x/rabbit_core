package rabbit_core

import chisel3.iotesters._

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
      Driver(() => new IF, backendName) {
        m: IF => new IFUnitTester(m)
      } should be(true)
    }
    "DE" should s"nothing to do with $backendName" in {
      Driver(() => (new DE(classOf[ConstRF])), backendName) {
        m: DE[ConstRF] => new DEUnitTester(m)
      } should be(true)
    }
  }
}
