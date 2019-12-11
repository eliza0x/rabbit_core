package rabbit_core

import chisel3._
import chisel3.util._
import rabbit_core.Properties._

class FreeRegisterList2Way extends Module {
  val PARALLEL_WIDTH = 2
  val io = IO(new Bundle {
    val release_required: Vec[Bool] = Vec(PARALLEL_WIDTH, Input(Bool()))
    val release_prf_addr: Vec[UInt] = Vec(PARALLEL_WIDTH, Input(UInt(PRF.W.W)))
    val reserve_required: Vec[Bool] = Vec(PARALLEL_WIDTH, Input(Bool()))
    val reserved_prf_addr: Vec[UInt] = Vec(PARALLEL_WIDTH, Output(UInt(PRF.W.W)))
  })
  val frl: Vec[UInt] = Reg(Vec(PRF.CNT+PARALLEL_WIDTH, UInt(PRF.W.W))) // 今空いているレジスタのリスト(Queue)
  val frl_top: UInt = RegInit((PRF.CNT+1).U(PRF.W.W)) // frl queueのtop

  val reserved_required_cnt: UInt = io.reserve_required.count((x: Bool) => x).asUInt()
  // .cnt methodが狙った動きをしてくれるかわからなくて恐ろしいのでデバッグプリントを仕込んでおく
  print(".cnt method: ")
  println(
    io.reserve_required.count(_) ===
      Mux(io.reserve_required(0), 1.U(2.W), 0.U(2.W))+Mux(io.reserve_required(1), 1.U(2.W), 0.U(2.W))
  )

  val release_required_cnt: UInt = io.release_required.count((x: Bool) => x).asUInt()

  // Queueの先頭n個をreserve
  for (i <- 0 until PARALLEL_WIDTH)
    io.reserved_prf_addr(i) := frl(i)

  for (i <- 0 until PRF.CNT) {
    // こう書くことで値の取りうる範囲を明示できて良いのでは？などと考えた
    frl(i) := MuxLookup(reserved_required_cnt, frl(i), Seq(
      0.U -> frl(i),
      1.U -> frl(i+1),
      2.U -> frl(i+2),
    ))
  }

  // TODO: 無効な値を無視して詰める処理、ここ分かりづらいのいい感じに抽象化するかモジュールを作りたい
  // これは2要素だから良いけど3以降がヤバイ, ソーティングネットワークで代用できるはず
  val packed_release_prf_addr: Vec[UInt] = Wire(Vec(PARALLEL_WIDTH, UInt(PRF.W.W)))
  packed_release_prf_addr(0) :=
    Mux(io.release_required(0), io.release_prf_addr(0), io.release_prf_addr(1))
  packed_release_prf_addr(1) :=
    Mux(io.release_required(0), io.release_prf_addr(1), 0.U)

  // QueueのtopにRelease
  for (i <- 0 until PARALLEL_WIDTH) {
    frl(frl_top+i.U) := packed_release_prf_addr(i)
  }
  frl_top := frl_top+release_required_cnt
}

