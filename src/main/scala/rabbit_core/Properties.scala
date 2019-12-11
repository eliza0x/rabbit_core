package rabbit_core

import chisel3.util.log2Ceil

object Properties {
  val XLEN = 16
  object RF {
    val CNT = 8 // ISAで定義されるレジスタの数
    val W = log2Ceil(CNT)
  }
  object PRF {
    val CNT = 16 // 物理的に使用することのできるレジスタの数
    val W = log2Ceil(CNT)
  }
}
