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
    val boardDim: Int = board.n * board.n
    val boardPossibilities: Array[Array[Set[Int]]] = board.board.map(r => r.map(c => {
      if (c == 0) {
        (1 to boardDim).toSet  
      } else {
        Set(c)
      }
    }))
    
    var isSolved = false
    while (!isSolved) {
      isSolved = true
      for (i <- 0 until boardDim) {
        for (j <- 0 until boardDim) {
          if (boardPossibilities(i)(j).size > 1) {
            isSolved = false 
          }
        }
      }
    }

    // boardPossibilities.foreach(r => r.foreach(c => println(c)))

    board
  }

}

@main def main(): Unit =
  val board = SudokuBoard(2, Array(
    Array(2, 1, 3, 0),
    Array(4, 3, 0, 2),
    Array(0, 4, 0, 3),
    Array(3, 2, 0, 1),
  ))
  val solver = SudokuSolver()
  val solved = solver.solve(board)
  solved.printToConsole() 
