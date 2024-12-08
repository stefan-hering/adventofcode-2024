package day08

fun readInput(): List<String> =
  String.javaClass.getResource("/day08/input")
    .readText()
    .lines()
    .filter { it.isNotBlank() }

infix operator fun Pair<Int,Int>.minus(other: Pair<Int,Int>): Pair<Int,Int> {
  return this.first - other.first to this.second - other.second
}

infix operator fun Pair<Int,Int>.plus(other: Pair<Int,Int>): Pair<Int,Int> {
  return this.first + other.first to this.second + other.second
}

fun markAntinodes1(grid: List<CharArray>, antennas: List<List<Pair<Int,Int>>>) {
  antennas.forEach {
    it.flatMap { a ->
      it.mapNotNull { b -> if(a!=b) a to b else null }
    }.forEach { (a,b) ->
      val distance = a - b

      listOf(a + distance, b - distance).forEach {
        if (grid.withinBounds(it)) {
          grid[it.first][it.second] = 'X'
        }
      }
    }
  }
}

fun List<CharArray>.withinBounds(point: Pair<Int,Int>)
  = point.first in indices && point.second in this[0].indices


fun markAntinodes2(grid: List<CharArray>, antennas: List<List<Pair<Int,Int>>>) {
  antennas.forEach {
    it.flatMap { a ->
      it.mapNotNull { b -> if(a!=b) a to b else null }
    }.forEach { (a,b) ->
      grid[a.first][a.second] = 'X'

      val distance = a - b
      var x = a - distance
      while(grid.withinBounds(x)) {
        grid[x.first][x.second] = 'X'
        x -= distance
      }
      x = a + distance
      while(grid.withinBounds(x)) {
        grid[x.first][x.second] = 'X'
        x += distance
      }
    }
  }
}

fun main() {
  val input = readInput()
  val charIndexes = input
    .flatMapIndexed { x, line ->
      line.mapIndexed { y, char ->
        char to y
      }.groupBy({it.first},{x to it.second})
        .asSequence()
    }.filter { it.key != '.' }
    .groupBy({ it.key }, { it.value })
    .map { it.key to it.value.flatten() }

  var antinodes = input.indices.map { CharArray(input[0].length) { '.' } }
  markAntinodes1(antinodes, charIndexes.map { it.second })
  println(antinodes.sumOf { it.count { it == 'X' } })

  antinodes = input.indices.map { CharArray(input[0].length) { '.' } }
  markAntinodes2(antinodes, charIndexes.map { it.second })
  println(antinodes.sumOf { it.count { it == 'X' } })
}