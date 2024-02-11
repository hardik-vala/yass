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
}
