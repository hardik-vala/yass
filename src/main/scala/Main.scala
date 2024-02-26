import scala.collection.mutable

case class SudokuBoard(
  n: Int,
  board: Array[Array[Int]]
) {
  require(n > 1, f"Board must have a dimentions greater than one")

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

class PuzzleIntegrityException(msg: String) extends Exception(msg)

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
        for (i <- 0 until boardDim) {
          for (j <- 0 until boardDim) {
            if (boardPossibilities(i)(j).size > 1) {
              val cellPossibilities = boardPossibilities(i)(j).toSeq
              for (k <- 0 until cellPossibilities.size) {
                val b = convertToBoard(board.n, boardPossibilities, i, j, cellPossibilities(k))
                try {
                  val result = solve(b)
                  return result
                } catch {
                  case ex: PuzzleIntegrityException => {}
                }
              }
            }
          }
        }
      }

      isBoardUpdated = false
    }

    convertToBoard(board.n, boardPossibilities)
  }

  private def resolveRow(
    boardPossibilities: Array[Array[mutable.Set[Int]]],
    x: Int,
    y: Int): Boolean = {
    val boardDim = boardPossibilities.size
    val cellPossibilities: mutable.Set[Int] = boardPossibilities(x)(y)
    val rowCandidates = mutable.Set((1 to boardDim): _*)
    var isBoardUpdated = false
    for (i <- 0 until boardDim) {
      if (boardPossibilities(x)(i).size == 1 && i != y) {
        val e = boardPossibilities(x)(i).toSeq.head
        
        if (cellPossibilities.contains(e)) {
          cellPossibilities -= e
          isBoardUpdated = true
        }

        if (rowCandidates.contains(e)) {
          rowCandidates -= e
        } else {
          throw new PuzzleIntegrityException(f"Number ${e} is duplicated in row ${x}")
        }
      }
    }

    if (cellPossibilities.isEmpty) {
      throw new PuzzleIntegrityException(f"(${x}, ${y}) has no possibilities")
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
      val columnCandidates = mutable.Set((1 to boardDim): _*)
      for (i <- 0 until boardDim) {
        if (boardPossibilities(i)(y).size == 1 && i != x) {
          val e = boardPossibilities(i)(y).toSeq.head

          if (cellPossibilities.contains(e)) {
            cellPossibilities -= e
            isBoardUpdated = true
          }

          if (columnCandidates.contains(e)) {
            columnCandidates -= e
          } else {
            throw new PuzzleIntegrityException(f"Number ${e} is duplicated in column ${y}")
          }
        }
      }
    }

    if (cellPossibilities.isEmpty) {
      throw new PuzzleIntegrityException(f"(${x}, ${y}) has no possibilities")
    }

    isBoardUpdated
  }

  private def resolveSubsquare(
    boardPossibilities: Array[Array[mutable.Set[Int]]],
    x: Int,
    y: Int,
    n: Int): Boolean = {
    val boardDim = n * n
    val cellPossibilities: mutable.Set[Int] = boardPossibilities(x)(y)
    var isBoardUpdated = false
    if (cellPossibilities.size > 1) {
      val subSquareTopLeftX = (x / n) * n
      val subSquareTopLeftY = (y / n) * n
      val subsquareCandidates = mutable.Set((1 to boardDim): _*)
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

            if (subsquareCandidates.contains(e)) {
              subsquareCandidates -= e
            } else {
              throw new PuzzleIntegrityException(
                f"Number ${e} is duplicated in subsquare containing (${x}, ${y})"
              )
            }
          }
        }
      }
    }

    if (cellPossibilities.isEmpty) {
      throw new PuzzleIntegrityException(f"(${x}, ${y}) has no possibilities")
    }
    
    isBoardUpdated
  }

  private def convertToBoard(
    n: Int, boardPossibilities: Array[Array[mutable.Set[Int]]]): SudokuBoard = {
    val board = boardPossibilities.map(r => r.map(c => {
      if (c.size > 1) {
        0
      } else {
        c.toSeq.head 
      }
    }))
    SudokuBoard(n, board)
  }

  private def convertToBoard(
    n: Int, 
    boardPossibilities: Array[Array[mutable.Set[Int]]],
    x: Int,
    y: Int,
    e: Int): SudokuBoard = {
    val boardPossibilitiesCopy = boardPossibilities.map(r => r.map(c => c))
    boardPossibilitiesCopy(x)(y) = mutable.Set(e)
    convertToBoard(n, boardPossibilitiesCopy)
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
  val board = SudokuBoard(2, Array(
    Array(0, 0, 4, 0),
    Array(3, 0, 2, 0),
    Array(0, 3, 0, 2),
    Array(0, 0, 3, 0),
  ))
  val solver = SudokuSolver()
  val solved = solver.solve(board)
  solved.printToConsole() 
