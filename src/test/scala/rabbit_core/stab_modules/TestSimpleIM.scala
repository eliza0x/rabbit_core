package rabbit_core.stab_modules

import chisel3._

class TestSimpleAddIM extends SkeletonIM(
  "b1001_001_000000001".U,
  "b1001_010_000000010".U,
  "b0001_001_010_00_0000".U,
)

class TestSimpleSubIM extends SkeletonIM(
  "b1001_001_000000001".U,
  "b1001_010_000000010".U,
  "b0001_001_010_00_0000".U,
)

class TestSimpleAndIM extends SkeletonIM(
  "b1001_001_000000001".U,
  "b1001_010_000000010".U,
  "b0001_001_010_00_0000".U,
)

class TestSimpleOrIM extends SkeletonIM(
  "b1001_001_000000001".U,
  "b1001_010_000000010".U,
  "b0001_001_010_00_0000".U,
)
