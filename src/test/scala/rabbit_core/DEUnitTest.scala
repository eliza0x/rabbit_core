package rabbit_core.test

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import rabbit_core.DE

class DEUnitTester(m: DE) extends PeekPokeTester(m) {
  for(i <- 0 to 10) {
    poke(m.io.inst, 0)
    poke(m.io.pc, 0)
    step(1)
    // expect()
  }
}
