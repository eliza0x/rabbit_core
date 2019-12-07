package rabbit_core

import chisel3._
import rabbit_core.traits.HasIO
import rabbit_core.Properties._

class HartIO extends Bundle {
  val out = Output(UInt(XLEN.W))
}

class Hart[
  IFM <: Module with HasIO[IFIO],
  DEM <: Module with HasIO[DEIO],
  EXM <: Module with HasIO[EXIO],
  MAM <: Module with HasIO[MAIO],
] (
   implicit val IF: () => IFM,
   implicit val DE: () => DEM,
   implicit val EX: () => EXM,
   implicit val MA: () => MAM,
) extends Module with HasIO[HartIO] {
  val io = IO(new HartIO)
  val mif: IFM = Module(IF())
  val mde: DEM = Module(DE())
  val mex: EXM = Module(EX())
  val mma: MAM = Module(MA())

  mif.io.pc_w := mex.io.pc_w
  mif.io.alu_out := mex.io.alu_out

  mde.io.inst := mif.io.inst
  mde.io.pc := mif.io.pc
  mde.io.in_rf_w := mde.io.rf_w
  mde.io.in_mem_r := mde.io.mem_r
  mde.io.in_rd_addr := mde.io.rd_addr
  mde.io.alu_out := mex.io.alu_out
  mde.io.mem_out := mma.io.mem_out

  mex.io.rd := mde.io.rd
  mex.io.rs := mde.io.rs
  mex.io.alu_op := mde.io.alith
  mex.io.source1 := mde.io.source1
  mex.io.source2 := mde.io.source2
  mex.io.cond_type := mde.io.cond_type

  mma.io.mem_w := mde.io.mem_w
  mma.io.mem_r := mde.io.mem_r
  mma.io.rd := mde.io.rd
  mma.io.alu_out := mex.io.alu_out

  io.out := mex.io.alu_out
  // printf("pc = %d, out = %d\n",mif.io.pc, io.out)
}
