package rabbit_core

import chisel3._
import chisel3.util._
import rabbit_core.Properties._
import scala.language.reflectiveCalls

class FreeRegisterList2Way extends Module {
  val PARALLEL_WIDTH = 2
  val io = IO(new Bundle {
    val release_required: Vec[Bool] = Vec(PARALLEL_WIDTH, Input(Bool()))
    val release_prf_addr: Vec[UInt] = Vec(PARALLEL_WIDTH, Input(UInt(PRF.W.W)))
    val reserve_required: Vec[Bool] = Vec(PARALLEL_WIDTH, Input(Bool()))
    val reserved_prf_addr: Vec[UInt] = Vec(PARALLEL_WIDTH, Output(UInt(PRF.W.W)))
  })
  // val FRL_SIZE = PRF.CNT - 1
  val FRL_SIZE = PRF.CNT
  val frl = RegInit(VecInit((0 until FRL_SIZE).map(_.U))) // 今空いているレジスタのリスト(Queue)
  val frl_top: UInt = RegInit((FRL_SIZE-2).U(PRF.W.W)) // frl queueのtop

  val reserved_required_cnt: UInt = io.reserve_required.count((x: Bool) => x).asUInt()
  // .cnt methodが狙った動きをしてくれるかわからなくて恐ろしいのでデバッグプリントを仕込んでおく
  // print(".cnt method: ")
  // println(
  //   io.reserve_required.count(_) ===
  //     Mux(io.reserve_required(0), 1.U(2.W), 0.U(2.W))+Mux(io.reserve_required(1), 1.U(2.W), 0.U(2.W))
  // )

  val release_required_cnt: UInt = io.release_required.count((x: Bool) => x).asUInt()

  // Queueの先頭n個をreserve
  for (i <- 0 until PARALLEL_WIDTH) {
    io.reserved_prf_addr(i) := frl(i)
  }

  // TODO: 無効な値を無視して詰める処理、ここ分かりづらいのいい感じに抽象化するかモジュールを作りたい
  // これは2要素だから良いけど3以降がヤバイ, ソーティングネットワークで代用できるはず
  // ソーティングネットワークを使うとFRL自体をPRFに統合できる(PRFのbusyタグをみてソートすれば良い)
  // 規模がでかくなるからダメか？
  val packed_release_prf_addr: Vec[UInt] = Wire(Vec(PARALLEL_WIDTH, UInt(PRF.W.W)))
  packed_release_prf_addr(0) :=
    Mux(io.release_required(0), io.release_prf_addr(0), io.release_prf_addr(1))
  packed_release_prf_addr(1) :=
    Mux(io.release_required(0), io.release_prf_addr(1), 0.U)

  for (i <- 0 until PRF.CNT-2) {
    // こう書くことで値の取りうる範囲を明示できて良いのでは？などと考えた
    // キューのシフトと新しい要素の追加を行っている
    frl(i) := MuxCase(frl(i), Seq(
      (i.U === frl_top)               -> packed_release_prf_addr(0),
      (i.U === frl_top+1.U)           -> packed_release_prf_addr(1),
      (reserved_required_cnt === 0.U) -> frl(i),
      (reserved_required_cnt === 1.U) -> frl(i+1),
      (reserved_required_cnt === 2.U) -> frl(i+2),
    ))
  }

  frl_top := frl_top+release_required_cnt
}

