// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MainTest extends munit.FunSuite {
  test("Valid Sudoku board") {
    SudokuBoard(2, Array(
      Array.fill(4)(0),
      Array.fill(4)(0),
      Array.fill(4)(0),
      Array.fill(4)(0),
    ))
  }

  test("Invalid Sudoku board throws exception") {
    intercept[IllegalArgumentException] {
      SudokuBoard(3, Array(
        Array.fill(4)(0),
        Array.fill(4)(0),
        Array.fill(4)(0),
        Array.fill(4)(0),
      ))
    }
  }

  test("Solved Sudoku board") {
    val b = SudokuBoard(2, Array(
      Array(2, 1, 3, 4),
      Array(4, 3, 1, 2),
      Array(1, 4, 2, 3),
      Array(3, 2, 4, 1),
    ))
    val solver = SudokuSolver()
    val solved = solver.solve(b)
    assertEquals(b.equals(solved), true)
  }

  test("Unsolvable Sudoku board throws exception") {
    val b = SudokuBoard(2, Array(
      Array.fill(4)(0),
      Array.fill(4)(0),
      Array.fill(4)(0),
      Array.fill(4)(0),
    ))
    val solver = SudokuSolver()
    intercept[StalledProgressException] {
      solver.solve(b)
    }
  }

  test("Solve Sudoku board using row elimination only") {
    val b = SudokuBoard(2, Array(
      Array(2, 1, 0, 4),
      Array(4, 3, 0, 2),
      Array(1, 4, 0, 3),
      Array(3, 2, 0, 1),
    ))
    val solver = SudokuSolver()
    val solved = solver.solve(b)

    val expected = SudokuBoard(2, Array(
      Array(2, 1, 3, 4),
      Array(4, 3, 1, 2),
      Array(1, 4, 2, 3),
      Array(3, 2, 4, 1),
    ))
    assertEquals(expected.equals(solved), true)
  }

  test("Solve Sudoku board using column elimination only") {
    val b = SudokuBoard(2, Array(
      Array(2, 1, 3, 4),
      Array(0, 0, 0, 0),
      Array(1, 4, 2, 3),
      Array(3, 2, 4, 1),
    ))
    val solver = SudokuSolver()
    val solved = solver.solve(b)

    val expected = SudokuBoard(2, Array(
      Array(2, 1, 3, 4),
      Array(4, 3, 1, 2),
      Array(1, 4, 2, 3),
      Array(3, 2, 4, 1),
    ))
    assertEquals(expected.equals(solved), true)
  }

  test("Solve Sudoku board using row and column elimination only") {
    val b = SudokuBoard(2, Array(
      Array(0, 3, 0, 4),
      Array(0, 0, 1, 3),
      Array(3, 1, 4, 2),
      Array(4, 0, 3, 0),
    ))
    val solver = SudokuSolver()
    val solved = solver.solve(b)

    val expected = SudokuBoard(2, Array(
      Array(1, 3, 2, 4),
      Array(2, 4, 1, 3),
      Array(3, 1, 4, 2),
      Array(4, 2, 3, 1),
    ))
    assertEquals(expected.equals(solved), true)
  }

  test("Solve Sudoku board using row, column, and subsquare elimination #1") {
    val b = SudokuBoard(2, Array(
      Array(0, 3, 0, 0),
      Array(0, 0, 1, 3),
      Array(3, 1, 4, 2),
      Array(4, 0, 3, 0),
    ))
    val solver = SudokuSolver()
    val solved = solver.solve(b)

    val expected = SudokuBoard(2, Array(
      Array(1, 3, 2, 4),
      Array(2, 4, 1, 3),
      Array(3, 1, 4, 2),
      Array(4, 2, 3, 1),
    ))
    assertEquals(expected.equals(solved), true)
  }

  test("Solve Sudoku board using row, column, and subsquare elimination #2") {
    val b = SudokuBoard(2, Array(
      Array(1, 0, 4, 0),
      Array(3, 0, 2, 0),
      Array(0, 3, 0, 2),
      Array(0, 0, 3, 0),
    ))
    val solver = SudokuSolver()
    val solved = solver.solve(b)

    val expected = SudokuBoard(2, Array(
      Array(1, 2, 4, 3),
      Array(3, 4, 2, 1),
      Array(4, 3, 1, 2),
      Array(2, 1, 3, 4)
    ))
    assertEquals(expected.equals(solved), true)
  }
}
