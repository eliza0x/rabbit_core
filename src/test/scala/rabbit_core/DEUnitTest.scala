package rabbit_core

import chisel3.iotesters._
import Util._
import rabbit_core.stab_modules.ConstRF
import scala.language.reflectiveCalls

class DEUnitTester(m: DE[ConstRF]) extends PeekPokeTester(m) {
  poke(m.io.pc, 0)
  poke(m.io.inst, "0010_001_010_000000".fromBinaryString)
  poke(m.io.write_rf_data, 0)
  poke(m.io.write_rf_addr, 0)
  step(1)
  expect(m.io.alith, 1)
  expect(m.io.source1, 1)
  expect(m.io.source2, 2)
}
