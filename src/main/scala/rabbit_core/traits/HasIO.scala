package rabbit_core.traits

import chisel3._

// M型のIO Interfaceを備えたModuleを定義するためのモジュール
trait HasIO[M <: Record] {
  val io: M
}
