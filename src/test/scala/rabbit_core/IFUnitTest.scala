// See README.md for license details.

package rabbit_core

package gcd.test

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class IFUnitTester(f: IF) extends PeekPokeTester(f) {
  for(i <- 0 to 10) {
    expect(f.io.pc, i)
    step(1)
  }
}

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
class IFTester extends ChiselFlatSpec {
  // private val backendNames = Array("firrtl", "verilator")
  private val backendNames = Array("firrtl")
  for ( backendName <- backendNames ) {
    "IF" should s"with $backendName" in {
      Driver(() => new IF, backendName) {
        c: IF => new IFUnitTester(c)
      } should be (true)
    }
  }
}
