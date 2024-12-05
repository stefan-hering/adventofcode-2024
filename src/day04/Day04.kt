package day04

fun readInput(): List<String> =
  String.javaClass.getResource("/day04/input")
    .readText()
    .lines()

fun transpose(input: List<String>) =
  input[0].indices.map { i -> input[i].indices.map { j -> input[j][i] } }
    .map { it.joinToString("") }


fun part1(input: List<String>): Int {
  val countXMAS = { s: String -> s.windowed(4).count { it == "XMAS" || it == "SAMX" } }

  val horizontal = input.map(countXMAS).sum()
  val vertical = transpose(input).map(countXMAS).sum()

  // diagonals, TODO this should not need 4 loops
  var sum = 0
  for(x in input.indices) {
    var diagonalString = ""
    for(y in input[0].indices) {
      if(input.size > x+y) {
        diagonalString += input[x + y][y]
      }
    }
    sum += countXMAS(diagonalString)
  }

  for(y in input[0].indices.drop(1)) {
    var diagonalString = ""
    for(x in input.indices) {
      if(input[0].length > x+y) {
        diagonalString += input[x][y+x]
      }
    }
    sum += countXMAS(diagonalString)
  }

  for(x in input.indices) {
    var diagonalString = ""
    for(y in input[0].indices.reversed()) {
      if(input[0].length > x + input[0].length - 1 - y) {
        diagonalString += input[x + input[0].length - 1 - y][y]
      }
    }
    sum += countXMAS(diagonalString)
  }

  for(y in input[0].indices.reversed().drop(1)) {
    var diagonalString = ""
    for(x in input.indices) {
      if(0 <= y - x) {
        diagonalString += input[x][y - x]
      }
    }
    sum += countXMAS(diagonalString)
  }

  return horizontal + vertical + sum
}

fun part2(input: List<String>): Int {
  return input.windowed(3).map { lineWindow ->
    lineWindow[0].indices.windowed(3).map { rowWindow ->
      val xMas = lineWindow.map { it.substring(rowWindow[0], rowWindow[2]+1) }

      if(xMas[1][1] == 'A' &&
        (
            xMas[0][0] == 'M' && xMas[2][0] == 'M' && xMas[0][2] == 'S' && xMas[2][2] == 'S' ||
            xMas[0][0] == 'M' && xMas[2][0] == 'S' && xMas[0][2] == 'M' && xMas[2][2] == 'S' ||
            xMas[0][0] == 'S' && xMas[2][0] == 'M' && xMas[0][2] == 'S' && xMas[2][2] == 'M' ||
            xMas[0][0] == 'S' && xMas[2][0] == 'S' && xMas[0][2] == 'M' && xMas[2][2] == 'M')
        ) {
        1
      } else 0
    }.sum()
  }.sum()
}

fun main() {
  val input = readInput()

  println(part1(input))
  println(part2(input))
}

