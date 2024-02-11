case class SudokuBoard(
  n: Int,
  board: Array[Array[Int]]
) {
  val dim = n * n
  require(board.length == dim, f"Board must have ${dim} rows")

  board.foreach{ row => 
    require(row.length == dim, f"Each row must have ${dim} numbers")
  }
}

@main def main(): Unit =
  println("Hello world!")
