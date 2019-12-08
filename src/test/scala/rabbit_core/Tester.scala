package rabbit_core

import chisel3.iotesters._
import rabbit_core.stab_modules._
import scala.language.implicitConversions

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
  implicit val SimpleSubIM = () => new TestSimpleSubIM
  implicit val SimpleAndIM = () => new TestSimpleAndIM
  implicit val SimpleOrIM = () => new TestSimpleOrIM
  implicit val SimpleAddiIM = () => new TestSimpleAddiIM
  implicit val SimpleSubiIM = () => new TestSimpleSubiIM
  implicit val SimpleIncrIM = () => new TestSimpleIncrIM
  implicit val SimpleDecrIM = () => new TestSimpleDecrIM
  implicit val SimpleLdiIM = () => new TestSimpleLdiIM
  implicit val SimpleLdIM = () => new TestSimpleLdIM
  implicit val SimpleStIM = () => new TestSimpleStIM
  implicit val SimpleJumpIM = () => new TestSimpleJumpIM
  implicit val ConstRf = () => new ConstRF
  implicit val Rf = () => new RegFile
  implicit val Alu = () => new ALU
  implicit val TestIM = () => new TestMA
  implicit val IfSeqIM = () => new IF[TestSequentialAccessIM]
  implicit val IfAddIM = () => new IF[TestSimpleAddIM]
  implicit val IfSubIM = () => new IF[TestSimpleSubIM]
  implicit val IfAndIM = () => new IF[TestSimpleAndIM]
  implicit val IfOrIM = () => new IF[TestSimpleOrIM]
  implicit val IfAddiIM = () => new IF[TestSimpleAddiIM]
  implicit val IfSubiIM = () => new IF[TestSimpleSubiIM]
  implicit val IfIncrIM = () => new IF[TestSimpleIncrIM]
  implicit val IfDecrIM = () => new IF[TestSimpleDecrIM]
  implicit val IfLdiIM = () => new IF[TestSimpleLdiIM]
  implicit val IfLdIM = () => new IF[TestSimpleLdIM]
  implicit val IfStIM = () => new IF[TestSimpleStIM]
  implicit val IfJumpIM = () => new IF[TestSimpleJumpIM]
  implicit val DeRf = () => new DE[RegFile]
  implicit val DeConstRF = () => new DE[ConstRF]
  implicit val Ex = () => new EX[ALU]
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
    it should s"simple sub test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleSubIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleSubUnitTest(m)
      } should be(true)
    }
    it should s"simple and test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleAndIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleAndUnitTest(m)
      } should be(true)
    }
    it should s"simple or test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleOrIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleOrUnitTest(m)
      } should be(true)
    }
    it should s"simple addi test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleAddiIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleAddiUnitTest(m)
      } should be(true)
    }
    it should s"simple subi test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleSubiIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleSubiUnitTest(m)
      } should be(true)
    }
    it should s"simple incr test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleIncrIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleIncrUnitTest(m)
      } should be(true)
    }
    it should s"simple decr test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleDecrIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleDecrUnitTest(m)
      } should be(true)
    }
    it should s"simple ldi test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleLdiIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleLdiUnitTest(m)
      } should be(true)
    }
    it should s"simple ld test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleLdIM], DE[RegFile], EX[ALU], TestMA], backendName) {
        m => new HartSimpleLdUnitTest(m)
      } should be(true)
    }
    it should s"simple ld/st test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleStIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleStUnitTest(m)
      } should be(true)
    }
    it should s"simple jump test with $backendName" in {
      Driver(() => new Hart[IF[TestSimpleJumpIM], DE[RegFile], EX[ALU], MA], backendName) {
        m => new HartSimpleJumpUnitTest(m)
      } should be(true)
    }
  }
}
