package rabbit_core

import chisel3._
import chisel3.util._
import rabbit_core.`trait`.HasIO
import rabbit_core.Properties._
import rabbit_core.model.ALUIO

class ALU extends HasIO[ALUIO] {
  val io = IO(new ALUIO)
  io.alu_out := MuxLookup(io.alith, 0.U, Array(
    AlithADD.id -> io.source1.+(io.source2),
    AlithSUB.id -> io.source1.-(io.source2),
    AlithAND.id -> io.source1.&(io.source2),
    AlithOR.id  -> io.source1.|(io.source2)
  ))
}
