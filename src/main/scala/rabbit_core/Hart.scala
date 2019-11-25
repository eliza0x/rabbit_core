package rabbit_core

import chisel3._
import rabbit_core.traits.HasIO
import rabbit_core.Properties._

import scala.reflect.ClassTag

class HartIO extends Bundle {
  val out = Output(UInt(XLEN.W))
}

// class Hart[
//   A <: Module with HasIO[IFIO],
//   B <: Module with HasIO[DEIO],
//   C <: Module with HasIO[EXIO],
//   D <: Module with HasIO[MAIO]
// ](
//  val M0: Class[IF[InstMemory]],
//  val M1: Class[DE[RegFile]],
//  val M2: Class[EX[ALU]],
//  val M3: Class[MA]

class Hart[M <: Module with HasIO[IFIO]](implicit val IF: ClassTag[M]) extends Module with HasIO[HartIO] {
  val io = IO(new HartIO)
  val mif = Module(IF.runtimeClass.newInstance.asInstanceOf[M])
  val mde = Module(new DE[RegFile])
  val mex = Module(new EX[ALU])
  val mma = Module(new MA)

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
}
