package rabbit_core.test

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import rabbit_core.IF

class IFUnitTester(m: IF) extends PeekPokeTester(m) {
  for(i <- 0 to 10) {
    expect(m.io.pc, i)
    step(1)
  }
}


