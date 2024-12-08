package day07

data class Calibration(val result: Long, val ops: List<Long>)
typealias Calculation = Pair<Long, List<Pair<Long,Op>>>
typealias Op = (Long, Long) -> Long

fun readInput(): List<Calibration> =
  String.javaClass.getResource("/day07/input")
    .readText()
    .lines()
    .filter { it.isNotBlank() }
    .map { it.split(":")}
    .map { Calibration(it[0].toLong(), it[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }) }


fun generateCombinations(length: Int, ops: List<Op>): List<List<Op>> {
  return if (length == 1) {
    ops.map { listOf(it) }
  } else {
    generateCombinations(length - 1, ops).flatMap { combo ->
      ops.map { combo + it }
    }
  }
}

fun allEquations(nums: List<Long>, ops: List<Op>): List<Calculation> {
  return generateCombinations(nums.size - 1, ops).map {
    nums.drop(1).zip(it)
  }.map {
    nums[0] to it
  }
}

fun part1(input: List<Calibration>, ops: List<Op>): Long {
  return input.filter { cal ->
    val eqs = allEquations(cal.ops, ops)

    eqs.any { eq ->
      val result = eq.second.fold(eq.first) { acc, calc ->
        calc.second(acc, calc.first)
      }
      result == cal.result
    }
  }.sumOf { it.result }
}

val ops1 = listOf<Op>(Long::plus, Long::times)
val ops2 = listOf<Op>(Long::plus, Long::times, { a,b -> "$a$b".toLong()})

fun main() {
  val input = readInput()

  // 213981957647469 -> too low

  println(part1(input, ops1))
  println(part1(input, ops2))
}