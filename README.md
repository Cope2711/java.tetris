# Java Tetris

A small Tetris project built with Java Swing.

## Requirements

- JDK 17 or newer

## Run

```powershell
javac -d bin src\*.java
java -cp bin tetris.TetrisApp
```

## Controls

- `A`: move left
- `D`: move right
- `W`: rotate
- `S` or down arrow: soft drop
- `Space`: hard drop
- `P`: pause or resume
- `R`: restart after game over

Clearing a line awards 100 points.
