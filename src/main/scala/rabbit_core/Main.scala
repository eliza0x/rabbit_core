package rabbit_core

import chisel3._

object Main extends App {
  implicit val Im = () => new InstMemory
  implicit val Rf = () => new RegFile
  implicit val Alu = () => new ALU
  implicit val If = () => new IF[InstMemory]
  implicit val De = () => new DE[RegFile]
  implicit val Ex = () => new EX[ALU]
  implicit val Ma = () => new MA
  chisel3.Driver.execute(args, () => new Hart[IF[InstMemory], DE[RegFile], EX[ALU], MA])
}
