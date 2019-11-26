package rabbit_core

import chisel3.iotesters._
import rabbit_core.stab_modules.{ConstRF, TestSequentialAccessIM, TestSimpleAddIM}

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly example.test.GCDTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly example.test.GCDTester'
  * }}}
  */

object ImplicitInstances {
  implicit val Im = () => new InstMemory
  implicit val SeqIu = () => new TestSequentialAccessIM
  implicit val SimpleAddIM = () => new TestSimpleAddIM
  implicit val ConstRf = () => new ConstRF
  implicit val Rf = () => new RegFile
  implicit val Alu = () => new ALU
  implicit val IfIm = () => new IF[InstMemory]
  implicit val IfSeqim = () => new IF[TestSequentialAccessIM]
  implicit val IfSimAdd = () => new IF[TestSimpleAddIM]
  implicit val DeRf = () => new DE[RegFile]
  implicit val DeConstRf = () => new DE[ConstRF]
  implicit val ExAlu = () => new EX[ALU]
  implicit val Ma = () => new MA
}

class Tester extends ChiselFlatSpec {
  import rabbit_core.ImplicitInstances._

  // private val backendNames = Array("firrtl", "verilator")
  private val backendNames = Array("firrtl")
  for (backendName <- backendNames) {
    behavior of "IF"
    it should s"count up pc with $backendName" in {
      Driver(() => new IF[TestSequentialAccessIM], backendName) {
        m: IF[TestSequentialAccessIM] => new IFUnitTester(m)
      } should be(true)
    }
    it should s"jump and branch with $backendName" in {
      Driver(() => new IF[TestSequentialAccessIM], backendName) {
        m: IF[TestSequentialAccessIM] => new IFJumpUnitTester(m)
      } should be(true)
    }

    behavior of "DE"
    it should s"nothing to do with $backendName" in {
      Driver(() => new DE[ConstRF], backendName) {
        m: DE[ConstRF] => new DEUnitTester(m)
      } should be(true)
    }

    behavior of "MA"
    it should s"10 + 20 = 30 with $backendName" in {
      Driver(() => new EX[ALU], backendName) {
        m: EX[ALU] => new EXUnitTest(m)
      } should be(true)
    }

    behavior of "MA"
    it should s"load and store with $backendName" in {
      Driver(() => new MA, backendName) {
        m: MA => new MAUnitTest(m)
      } should be(true)
    }

    behavior of "Hart"
    it should s"simple add test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleAddIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleAddUnitTest(m)
      } should be(true)
    }
  }
}
