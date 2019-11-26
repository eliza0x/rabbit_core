package rabbit_core.stab_modules

import chisel3._

class TestSimpleAddIM extends SkeletonIM(
  "b1001_001_000000101".U,   // $1 = 5
  "b1001_010_000000010".U,   // $2 = 2
  "b0001_001_010_00_0000".U, // $1 = $1 + $2
)

class TestSimpleSubIM extends SkeletonIM(
  "b1001_001_000000101".U,   // $1 = 5
  "b1001_010_000000010".U,   // $2 = 2
  "b0010_001_010_00_0000".U, // $1 = $1 - $2
)

class TestSimpleAndIM extends SkeletonIM(
  "b1001_001_000000101".U,   // $1 = 5
  "b1001_010_000000010".U,   // $2 = 2
  "b0011_001_010_00_0000".U, // $1 = $1 & $2
)

class TestSimpleOrIM extends SkeletonIM(
  "b1001_001_000000101".U,   // $1 = 5
  "b1001_010_000000010".U,   // $2 = 2
  "b0100_001_010_00_0000".U, // $1 = $1 | $2
)

class TestSimpleAddiIM extends SkeletonIM(
  "b1001_001_000000101".U, // $1 = 5
  "b0101_001_000000010".U, // $1 = $1 + 2
)

class TestSimpleSubiIM extends SkeletonIM(
  "b1001_001_000000101".U, // $1 = 5
  "b0110_001_000000010".U, // $1 = $1 - 2
)
