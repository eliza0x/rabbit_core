package rabbit_core

import chisel3._
import chisel3.util._
import rabbit_core.traits.HasIO
import rabbit_core.models.ALUIO

class ALU extends Module with HasIO[ALUIO] {
  val io = IO(new ALUIO)
  io.alu_out := MuxLookup(io.alu_op, 0.U, Array(
    ALUADD.id -> io.source1.+(io.source2),
    ALUSUB.id -> io.source1.-(io.source2),
    ALUAND.id -> io.source1.&(io.source2),
    ALUOR.id  -> io.source1.|(io.source2)
  ))
}
