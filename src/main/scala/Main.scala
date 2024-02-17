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
    val horizontalRuleLength = (dim + n + 1) * 2 - 1
    board.zipWithIndex.foreach((row, i) => {
      if (i % n == 0) {
        println("-" * horizontalRuleLength)
      }

      row.zipWithIndex.foreach((num, j) => {
        val numStr = if (num == 0 ) f" " else f"$num%1d"
        if (j % n == 0) {
          print(f"| $numStr ")
        } else {
          print(f"$numStr ")
        }
      })
      println("|")
    })

    println("-" * horizontalRuleLength)
  }
}

class SudokuSolver {

  def solve(board: SudokuBoard): SudokuBoard = {
    return board
  }

}

@main def main(): Unit =
  val board = SudokuBoard(2, Array(
    Array(4, 3, 4, 0),
    Array(0, 0, 2, 1),
    Array(1, 4, 0, 3),
    Array(0, 0, 4, 1),
  ))
  val solver = SudokuSolver()
  val solved = solver.solve(board)
  solved.printToConsole() 
