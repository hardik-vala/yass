import scala.collection.mutable

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

  override def equals(obj: Any): Boolean = {
    obj match {
      case that: SudokuBoard => n == that.n && boardDeepEqual(board, that.board) 
      case _ => false  
    }
  }

  private def boardDeepEqual(a: Array[Array[Int]], b: Array[Array[Int]]): Boolean = {
    a.length == b.length && 
      a.zip(b).forall { case (row1, row2) => 
        row1.length == row2.length &&
          row1.zip(row2).forall { case (x1, x2) =>
            x1 == x2
          }
      }
  }
}

class StalledProgressException extends Exception

class SudokuSolver {

  def solve(board: SudokuBoard): SudokuBoard = {
    val boardDim: Int = board.n * board.n
    val boardPossibilities: Array[Array[mutable.Set[Int]]] = board.board.map(r => r.map(c => {
      if (c == 0) {
        mutable.Set((1 to boardDim): _*)
      } else {
        mutable.Set(c)
      }
    }))
    
    var isSolved = false
    var isBoardUpdated = false
    while (!isSolved) {
      isSolved = true
      for (i <- 0 until boardDim) {
        for (j <- 0 until boardDim) {
          if (boardPossibilities(i)(j).size > 1) {
            isSolved = false
            isBoardUpdated ||= resolveRow(boardPossibilities, i, j)
            isBoardUpdated ||= resolveColumn(boardPossibilities, i, j)
            isBoardUpdated ||= resolveSubsquare(boardPossibilities, i, j, board.n)
          }
        }
      }

      if (!isBoardUpdated && !isSolved) {
        throw new StalledProgressException
      }

      isBoardUpdated = false
    }

    SudokuBoard(board.n, convertToBoard(boardPossibilities))
  }

  private def resolveRow(
    boardPossibilities: Array[Array[mutable.Set[Int]]],
    x: Int,
    y: Int): Boolean = {
    val boardDim = boardPossibilities.size
    val cellPossibilities: mutable.Set[Int] = boardPossibilities(x)(y)
    var isBoardUpdated = false
    for (i <- 0 until boardDim) {
      if (boardPossibilities(x)(i).size == 1 && i != y) {
        val e = boardPossibilities(x)(i).toSeq.head
        if (cellPossibilities.contains(e)) {
          cellPossibilities -= e
          isBoardUpdated = true
        }
      }
    }
    isBoardUpdated
  }

  private def resolveColumn(
    boardPossibilities: Array[Array[mutable.Set[Int]]],
    x: Int,
    y: Int): Boolean = {
    val cellPossibilities: mutable.Set[Int] = boardPossibilities(x)(y)
    var isBoardUpdated = false
    if (cellPossibilities.size > 1) {
      val boardDim = boardPossibilities.size
      for (i <- 0 until boardDim) {
        if (boardPossibilities(i)(y).size == 1 && i != x) {
          val e = boardPossibilities(i)(y).toSeq.head
          if (cellPossibilities.contains(e)) {
            cellPossibilities -= e
            isBoardUpdated = true
          }
        }
      }
    }
    isBoardUpdated
  }

  private def resolveSubsquare(
    boardPossibilities: Array[Array[mutable.Set[Int]]],
    x: Int,
    y: Int,
    n: Int): Boolean = {
    val cellPossibilities: mutable.Set[Int] = boardPossibilities(x)(y)
    var isBoardUpdated = false
    if (cellPossibilities.size > 1) {
      val subSquareTopLeftX = (x / n) * n
      val subSquareTopLeftY = (y / n) * n
      for (iDelta <- 0 until n) {
        val i = subSquareTopLeftX + iDelta
        for (jDelta <- 0 until n) {
          val j = subSquareTopLeftY + jDelta
          if (boardPossibilities(i)(j).size == 1 && i != x && j != y) {
            val e = boardPossibilities(i)(j).toSeq.head
            if (cellPossibilities.contains(e)) {
              cellPossibilities -= e
              isBoardUpdated = true
            }
          }
        }
      }
    }
    isBoardUpdated
  }

  private def convertToBoard(
    boardPossibilities: Array[Array[mutable.Set[Int]]]): Array[Array[Int]] = {
    boardPossibilities.map(r => r.map(c => c.toSeq.head))
  }

  private def printToConsole(boardPossibilities: Array[Array[mutable.Set[Int]]]): Unit = {
    val boardDim = boardPossibilities.size
    for (i <- 0 until boardDim) {
      for (j <- 0 until boardDim) {
        println(f"(${i}, ${j}) -> {${boardPossibilities(i)(j).mkString(", ")}}")
      }
    }
  }
}

@main def main(): Unit =
  val board = SudokuBoard(3, Array(
    Array(7, 0, 9, 3, 5, 6, 0, 1, 0),
    Array(5, 0, 0, 2, 0, 1, 8, 9, 7),
    Array(4, 2, 1, 0, 0, 0, 0, 0, 5),
    Array(6, 4, 0, 0, 3, 0, 0, 8, 0),
    Array(9, 7, 0, 4, 6, 5, 1, 0, 3),
    Array(3, 1, 2, 0, 9, 0, 4, 0, 0),
    Array(2, 6, 4, 5, 1, 3, 0, 0, 0),
    Array(0, 9, 7, 6, 0, 4, 5, 0, 1),
    Array(1, 5, 0, 0, 0, 7, 0, 4, 2),
  ))
  val solver = SudokuSolver()
  val solved = solver.solve(board)
  solved.printToConsole() 
