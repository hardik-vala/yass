# yass

Yet Another Sudoku Solver - In Scala.

## Installation

Open this project in the devcontainer and then run,

```
sdk install sbt
```

(Doesn't work as a `postCreateCommand` in `.devcontainer/devcontainer.json` for some reason.)

## Run

Update the puzzle in the main function in `Main.scala`,

```scala
// Update this object.
val board = SudokuBoard(2, Array(
  Array(0, 0, 4, 0),
  Array(3, 0, 2, 0),
  Array(0, 3, 0, 2),
  Array(0, 0, 3, 0),
))
val solver = SudokuSolver()
...
```

* You can compile code with `sbt compile`
* Run it with `sbt run`

## Test

```
sbt test
```

## Resources

For generating Sudoku puzzles, see [sudokuweb.org](https://www.sudokuweb.org/) or
[sudokutodo.com/generator](https://sudokutodo.com/generator).