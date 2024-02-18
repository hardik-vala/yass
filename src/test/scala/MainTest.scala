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
    assertEquals(b, solved)
  }
}
