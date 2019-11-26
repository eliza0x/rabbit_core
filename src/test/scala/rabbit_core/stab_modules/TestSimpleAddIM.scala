package rabbit_core.stab_modules

import rabbit_core.Util._
import chisel3._

class TestSimpleAddIM extends SkeletonIM(
  "1001_001_000000001".fromBinaryString.U,
  "1001_010_000000010".fromBinaryString.U,
  "0001_001_002_000000".fromBinaryString.U,
)
