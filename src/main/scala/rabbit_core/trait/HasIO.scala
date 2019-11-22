package rabbit_core.`trait`

import chisel3._

// M型のIO Interfaceを備えたModuleを定義するためのモジュール
trait HasIO[M <: Record] extends Module {
  val io: M
}
