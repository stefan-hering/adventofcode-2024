package day06

typealias Maze = Array<CharArray>
typealias Position = Pair<Int, Int>

fun readInput(): Maze =
  String.javaClass.getResource("/day06/input")
    .readText()
    .lines()
    .filter { it.isNotBlank() }
    .map { it.toCharArray() }
    .toTypedArray()

// TODO this got really messy since the first part step function did 2 steps in one on turning

fun setNewDirection(state: Maze, moveTo: Position, direction: Char, from: Position): Position? =
  if(moveTo.first < 0 || moveTo.second < 0 || moveTo.first >= state.size || moveTo.second >= state[0].size) {
    null
  } else if(state[moveTo.first][moveTo.second] == '#'){
    // turn in place
    state[from.first][from.second] = when (direction) {
      '^' -> '>'
      'v' -> '<'
      '>' -> 'v'
      else -> '^'
    }
    from
  } else {
    state[moveTo.first][moveTo.second] = direction
    moveTo
  }


fun step(state: Maze, guard: Position): Position? = guard.let { (x, y) ->
  val direction = state[x][y]
  val move = when (direction) {
    '^' -> x - 1 to y
    'v' -> x + 1 to y
    '>' -> x to y + 1
    else -> x to y - 1
  }
  state[x][y] = 'X'
  if(move.first < 0 || move.second < 0 || move.first >= state.size || move.second >= state[0].size) {
    null
  } else {
    if(listOf('.','X').contains(state[move.first][move.second])) {
      state[move.first][move.second] = direction
      move.first to move.second
    } else {
      when (direction) {
        '^' -> setNewDirection(state, (x to y + 1), '>', x to y)
        'v' -> setNewDirection(state, (x to y - 1), '<', x to y)
        '>' -> setNewDirection(state, (x + 1 to y), 'v', x to y)
        else -> setNewDirection(state, (x - 1 to y), '^', x to y)
      }
    }
  }
}

fun isCircle(state: Maze, start: Position): Boolean {
  val positions = mutableMapOf(start to 1)
  var next = start
  while (true) {
    next = step(state, next) ?: return false
    if(positions.contains(next)) {
      positions[next] = positions[next]!! + 1
    } else { positions[next] = 1 }

    if(positions[next] == 50) {
      return true
    }
  }
}


fun main() {
  val input = readInput()

  val start = input.indices.firstNotNullOf { x ->
    input[0].indices.firstNotNullOfOrNull { y ->
      if (listOf('^', '>', '<', 'v').contains(input[x][y])) {
        x to y
      } else null
    }
  }

  var next = start
  while(step(input, next)?.also { next = it } != null)
    ;

  input.sumOf {
    it.filter { it == 'X' }.count()
  }.also(::println)

  val cleanMaze = readInput()

  val obstaclePositions = input.indices.flatMap { x ->
    input[0].indices.mapNotNull { y ->
      if (input[x][y] == 'X') x to y else null
    }
  }.filter {
    it != start
  }.count { obstacle ->
    val newObstacle = cleanMaze.map { it.copyOf() }
    newObstacle[obstacle.first][obstacle.second] = '#'
    isCircle(newObstacle.toTypedArray(), start)
  }
  println(obstaclePositions)
}