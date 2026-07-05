package tetris;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Class representing the Tetris board
public class Board {

    // Board properties
    int BoardWidth = 10;
    int BoardHeight = 22;
    int BoardPositionX = 50;
    int BoardPositionY = 50;
    int BoardCellSizeX = 30;
    int BoardCellSizeY = 30;
    String BoardEmptyValue = "0";
    String BoardPieceValue = "1";
    String BoardPieceStaticValue = "2";
    Color BoardEmptyColor = Color.GRAY;
    String[][] Board = new String[BoardWidth][BoardHeight];

    // Create the board and fill it with default values
    public void createBoard() {
        for (int height = 0; height < BoardHeight; height++) {
            for (int width = 0; width < BoardWidth; width++) {
                this.Board[width][height] = BoardEmptyValue;
            }
        }
    }

    // Check whether a board line is completely full
    public boolean isLineFull(String[][] Board, int CellY) {
        int StaticCellCount = 0;

        // Count the static cells in a specific line
        for (int width = 0; width < BoardWidth; width++) {
            if (isStaticPieceValue(Board[width][CellY])) {
                StaticCellCount++;
            }
        }
        // Return true when every cell in the line is static
        return StaticCellCount == this.BoardWidth;
    }

    // Clear a full line and shift the pieces downward
    public int clearLines(String[][] Board, ArrayList<Integer> CellYPositions) {
        Set<Integer> rowsToClear = new HashSet<>();
        for (Integer cellY : CellYPositions) {
            if (isLineFull(Board, cellY)) {
                rowsToClear.add(cellY);
            }
        }

        for (int width = 0; width < BoardWidth; width++) {
            int writeRow = BoardHeight - 1;
            for (int readRow = BoardHeight - 1; readRow >= 0; readRow--) {
                if (!rowsToClear.contains(readRow)) {
                    Board[width][writeRow] = Board[width][readRow];
                    writeRow--;
                }
            }
            while (writeRow >= 0) {
                Board[width][writeRow] = BoardEmptyValue;
                writeRow--;
            }
        }
        setBoard(Board);
        return rowsToClear.size();
    }

    // Shift pieces downward after clearing a line
    public void shiftPiecesAfterClearing(String[][] Board, int CellY) {
        for (int width = 0; width < this.BoardWidth; width++) {
            for (int height = CellY; height > 0; height--) {
                // Move each piece one position downward
                Board[width][height] = Board[width][height - 1];
            }
            Board[width][0] = this.BoardEmptyValue;
        }
        // Update the board state
        setBoard(Board);
    }

    // Getters and setters for board properties
    public int getBoardWidth() {
        return BoardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        BoardWidth = boardWidth;
    }

    public int getBoardHeight() {
        return BoardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        BoardHeight = boardHeight;
    }

    public int getBoardPositionX() {
        return BoardPositionX;
    }

    public void setBoardPositionX(int boardPositionX) {
        BoardPositionX = boardPositionX;
    }

    public int getBoardPositionY() {
        return BoardPositionY;
    }

    public void setBoardPositionY(int boardPositionY) {
        BoardPositionY = boardPositionY;
    }

    public int getBoardCellSizeX() {
        return BoardCellSizeX;
    }

    public void setBoardCellSizeX(int boardCellSizeX) {
        BoardCellSizeX = boardCellSizeX;
    }

    public int getBoardCellSizeY() {
        return BoardCellSizeY;
    }

    public void setBoardCellSizeY(int boardCellSizeY) {
        BoardCellSizeY = boardCellSizeY;
    }

    public String getBoardEmptyValue() {
        return BoardEmptyValue;
    }

    public void setBoardEmptyValue(String boardVoidValue) {
        BoardEmptyValue = boardVoidValue;
    }

    public String getBoardPieceValue() {
        return BoardPieceValue;
    }

    public void setBoardPieceValue(String boardPieceValue) {
        BoardPieceValue = boardPieceValue;
    }

    public String getBoardPieceStaticValue() {
        return BoardPieceStaticValue;
    }

    public void setBoardPieceStaticValue(String boardPieceStaticValue) {
        BoardPieceStaticValue = boardPieceStaticValue;
    }

    public Color getBoardEmptyColor() {
        return BoardEmptyColor;
    }

    public void setBoardEmptyColor(Color boardVoidColor) {
        BoardEmptyColor = boardVoidColor;
    }

    public String[][] getBoard() {
        return Board;
    }

    public void setBoard(String[][] board) {
        Board = board;
    }

    public String createMovingPieceValue(int pieceId) {
        return BoardPieceValue + ":" + pieceId;
    }

    public String createStaticPieceValue(int pieceId) {
        return BoardPieceStaticValue + ":" + pieceId;
    }

    public boolean isMovingPieceValue(String value) {
        return value != null && (value.equals(BoardPieceValue) || value.startsWith(BoardPieceValue + ":"));
    }

    public boolean isStaticPieceValue(String value) {
        return value != null && (value.equals(BoardPieceStaticValue) || value.startsWith(BoardPieceStaticValue + ":"));
    }

    public int getPieceIdFromValue(String value) {
        if (value == null || !value.contains(":")) {
            return 0;
        }
        return Integer.parseInt(value.substring(value.indexOf(':') + 1));
    }
}
