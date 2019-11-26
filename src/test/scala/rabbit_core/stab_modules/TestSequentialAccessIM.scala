package rabbit_core.stab_modules

import chisel3._

class TestSequentialAccessIM extends SkeletonIM((0 until 255).map(_.U): _*)
