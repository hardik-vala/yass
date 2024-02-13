case class SudokuBoard(
  n: Int,
  board: Array[Array[Int]]
) {
  val dim = n * n
  require(board.length == dim, f"Board must have ${dim} rows")

  board.foreach{ row => 
    require(row.length == dim, f"Each row must have ${dim} numbers")
  }

  def printToConsole(): Unit = {
    board.foreach { row =>
      row.foreach(num => print(f"$num%1d ")) 
      println()
    }
  }
}

class SudokuSolver {

  def solve(board: SudokuBoard): SudokuBoard = {
    return board
  }

}

@main def main(): Unit =
  val b = SudokuBoard(2, Array(
    Array(4, 3, 4, 4),
    Array(4, 4, 2, 1),
    Array(1, 4, 4, 3),
    Array(4, 4, 4, 1),
  ))
  val solver = SudokuSolver()
  val solved = solver.solve(b)
  solved.printToConsole() 
