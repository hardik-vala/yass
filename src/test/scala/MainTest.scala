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

  test("Solve 9x9 Sudoku board with one missing entry") {
    val b = SudokuBoard(3, Array(
      Array(8, 2, 3, 7, 5, 1, 4, 6, 9),
      Array(9, 4, 5, 6, 8, 2, 1, 3, 7),
      Array(6, 1, 7, 3, 4, 9, 5, 2, 8),
      Array(7, 5, 4, 1, 6, 3, 8, 9, 2),
      Array(1, 9, 2, 4, 0, 8, 3, 5, 6),
      Array(3, 8, 6, 9, 2, 5, 7, 4, 1),
      Array(4, 6, 9, 8, 3, 7, 2, 1, 5),
      Array(5, 7, 1, 2, 9, 4, 6, 8, 3),
      Array(2, 3, 8, 5, 1, 6, 9, 7, 4),
    ))
    val solver = SudokuSolver()
    val solved = solver.solve(b)

    val expected = SudokuBoard(3, Array(
      Array(8, 2, 3, 7, 5, 1, 4, 6, 9),
      Array(9, 4, 5, 6, 8, 2, 1, 3, 7),
      Array(6, 1, 7, 3, 4, 9, 5, 2, 8),
      Array(7, 5, 4, 1, 6, 3, 8, 9, 2),
      Array(1, 9, 2, 4, 7, 8, 3, 5, 6),
      Array(3, 8, 6, 9, 2, 5, 7, 4, 1),
      Array(4, 6, 9, 8, 3, 7, 2, 1, 5),
      Array(5, 7, 1, 2, 9, 4, 6, 8, 3),
      Array(2, 3, 8, 5, 1, 6, 9, 7, 4),
    ))
    assertEquals(expected.equals(solved), true)
  }

  // Generated using https://sudokutodo.com/generator.
  test("Solve 9x9 Sudoku board (easy)") {
    val b = SudokuBoard(3, Array(
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
    val solved = solver.solve(b)

    val expected = SudokuBoard(3, Array(
      Array(7, 8, 9, 3, 5, 6, 2, 1, 4),
      Array(5, 3, 6, 2, 4, 1, 8, 9, 7),
      Array(4, 2, 1, 8, 7, 9, 3, 6, 5),
      Array(6, 4, 5, 1, 3, 2, 7, 8, 9),
      Array(9, 7, 8, 4, 6, 5, 1, 2, 3),
      Array(3, 1, 2, 7, 9, 8, 4, 5, 6),
      Array(2, 6, 4, 5, 1, 3, 9, 7, 8),
      Array(8, 9, 7, 6, 2, 4, 5, 3, 1),
      Array(1, 5, 3, 9, 8, 7, 6, 4, 2),
    ))
    assertEquals(expected.equals(solved), true)
  }
}
