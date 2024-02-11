// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("func that succeeds") {
    val obtained = func()
    val expected = 42
    assertEquals(obtained, expected)
  }
}
