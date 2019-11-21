package rabbit_core


object Util {
  implicit class StringLike(s: String) {
    def fromBinaryString: Int = {
      s.filter(c => c == '0' || c == '1').reverse.zipWithIndex.map {
        case (n, i) => if (n == '1') math.pow(2, i) else 0
      }.sum.toInt
    }
  }
}
