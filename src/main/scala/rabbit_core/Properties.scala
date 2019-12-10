package rabbit_core

object Properties {
  val XLEN = 16
  object RF {
    val CNT = 8 // ISAで定義されるレジスタの数
    val READ_PORTS = 2 // レジスタファイルのリードポートの数
    val WRITE_PORTS = 1 // レジスタファイルの書き込みポートの数
  }
  object PRF {
    val CNT = 16 // 物理的に使用することのできるレジスタの数
  }
  object RM {
    val RESERVE_PORTS = 2 // レジスタの予約をいくつ並列で行うか
    val READ_PORTS = 4 // レジスタの読み出しをいくつ並列で行うか
  }
  // TODO: 仮に1Offer, 1Releaseで実装した
  object FRL { // Free Register List
    val RELEACE_PORTS = 4 // レジスタのロック開放を最大いくつ並列で行うか
    val OFFER_PORTS = 2 // レジスタの割当を最大いくつ並列で行うか
  }
}
