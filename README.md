# yass

Yet Another Sudoku Solver - In Scala.

## Development

### Installation

Open this project in the devcontainer and then run,

```
sdk install sbt
```

(Doesn't work as a `postCreateCommand` in `.devcontainer/devcontainer.json` for some reason.)

### Run

You can compile code with `sbt compile`, run it with `sbt run`, test it with `sbt test`, and
`sbt console` will start a Scala 3 REPL.

## Resources

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).

For generating Sudoku puzzles, see [sudokuweb.org](https://www.sudokuweb.org/) or
[sudokutodo.com/generator](https://sudokutodo.com/generator).