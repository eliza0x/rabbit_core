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

class TestSimpleIncrIM extends SkeletonIM(
  "b1001_001_000000101".U, // $1 = 5
  "b0111_001_000000000".U, // $1 = $1 + 1
)

class TestSimpleDecrIM extends SkeletonIM(
  "b1001_001_000000101".U, // $1 = 5
  "b1000_001_000000000".U, // $1 = $1 - 1
)

class TestSimpleLdiIM extends SkeletonIM(
  "b1001_001_000000101".U, // $1 = 5
  "b1001_001_000001010".U, // $1 = 10
  "b1001_001_000001111".U, // $1 = 15
)

// Depends on TestMA
class TestSimpleLdIM extends SkeletonIM(
  "b1001_001_000000101".U,  // $1 = 5
  "b1010_010_001_000101".U, // $2 = DM[$1+5]
  "b0001_000_010_000000".U, // $0 += $2
)

class TestSimpleStIM extends SkeletonIM(
  "b1001_001_000000101".U, // $1 = 5
  "b1001_010_000001010".U, // $2 = 10
  "b1011_010_010_000101".U, // DM[$2+5] = $2
  "b1010_011_001_001010".U, // $3 = DM[$1+10]
  "b0001_000_011_000000".U, // $0 += $3
)

class TestSimpleJumpIM extends SkeletonIM(
  "b1001_001_000000001".U, // 1
  "b1001_001_000000010".U, // 2
  "b1001_001_000000100".U, // 4
  "b1110_000_111111110".U, // pc <- pc - 2
  "b1001_001_000001000".U, // 8
  "b1001_001_000010000".U, // 16
)

// (0 to 10).sum
class TestSumBeqIM extends SkeletonIM(
  "b1001_001_000000000".U,  // 0: $1 = 0
  "b1001_010_000000000".U,  // 1: $2 = 0
  "b1001_011_000001010".U,  // 2: $3 = 10
  "b0101_001_000000001".U,  // 3: $1 += 1
  "b0001_010_001_000000".U, // 4: $2 += $1
  "b1100_001_011_000010".U, // 5: if($1 == $3) pc+= 2
  "b1110_000_111111101".U,  // 6: pc = pc - 3
  "b0001_000_010_000000".U  // 7: $0 += $2
)

// (0 to 10).sum
class TestSumBgtIM extends SkeletonIM(
  "b1001_001_000000000".U,  // 0: $1 = 0
  "b1001_010_000000000".U,  // 1: $2 = 0
  "b1001_011_000001001".U,  // 2: $3 = 9
  "b0101_001_000000001".U,  // 3: $1 += 1
  "b0001_010_001_000000".U, // 4: $2 += $1
  "b1101_001_011_000010".U, // 5: if($1 > $3) pc+= 2
  "b1110_000_111111101".U,  // 6: pc = pc - 3
  "b0001_000_010_000000".U  // 7: $0 += $2
)
