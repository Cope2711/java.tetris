package tetris;

import java.awt.Color;
import java.util.Random;

// Class representing a Tetris piece
public class Piece {

    // Starting index used when generating pieces
    int PieceStartIndex = 4;
    // Current rotation position of the piece
    int CurrentPieceRotation = 0;
    // Piece color
    private final Color[] pieceColors = {
        Color.BLACK,
        Color.GRAY,
        Color.BLUE,
        new Color(120, 72, 32),
        Color.BLACK,
        Color.GRAY,
        Color.BLUE
    };
    // Current piece coordinates on the board
    int[][] CurrentPiece;
    // Piece identifier
    public int PieceId;

    // Board instance used to access board properties
    Board board = new Board();

    // Update the piece position on the board
    public void updatePiece(String[][] Board) {
        // Get the value representing a cell occupied by a piece
        String BoardPieceValue = board.createMovingPieceValue(PieceId);
        // Iterate over every cell in the piece
        for (int Cell = 0; Cell < CurrentPiece.length; Cell++) {
            int CellX = CurrentPiece[Cell][0];
            int CellY = CurrentPiece[Cell][1];
            // Mark the corresponding board cell as occupied by the piece
            Board[CellX][CellY] = BoardPieceValue;
        }
        // Update the board state
        board.setBoard(Board);
    }

    // Generate a new piece with its initial coordinates
    public void insertNewPiece() {
        // Generate a random identifier for the new piece
        Random random = new Random();
        PieceId = random.nextInt(7);
        // Get the initial coordinates of the new piece
        this.CurrentPiece = getPieceCoordinates(PieceId, CurrentPieceRotation);
        // Adjust the coordinates so the piece appears at the top of the board
        for (int Cell = 0; Cell < this.CurrentPiece.length; Cell++) {
            this.CurrentPiece[Cell][0] = this.CurrentPiece[Cell][0] + this.PieceStartIndex;
        }
    }

    // Return the coordinates for a specific piece at its current rotation
    public int[][] getPieceCoordinates(int PieceId, int PiecePosition) {
        // Define the coordinates of each piece type in every rotation
        int[][][] Hero =          new int[][][]{{{0, 1}, {0, 0}, {0, 2}, {0, 3}}, {{1, 0}, {0, 0}, {2, 0}, {3, 0}}};
        int[][][] LNormal =       new int[][][]{{{0, 1}, {0, 0}, {0, 2}, {1, 2}}, {{1, 0}, {2, 0}, {0, 0}, {0, 1}}, {{1, 1}, {1, 2}, {1, 0}, {0, 0}}, {{1, 1}, {0, 1}, {2, 1}, {2, 0}}};
        int[][][] ReversedL =    new int[][][]{{{1, 1}, {1, 0}, {1, 2}, {0, 2}}, {{1, 1}, {2, 1}, {0, 1}, {0, 0}}, {{0, 1}, {0, 2}, {0, 0}, {1, 0}}, {{1, 0}, {0, 0}, {2, 0}, {2, 1}}};
        int[][][] NormalZ =    new int[][][]{{{1, 0}, {0, 0}, {1, 1}, {2, 1}}, {{1, 1}, {1, 0}, {0, 1}, {0, 2}}};
        int[][][] ReversedZ = new int[][][]{{{1, 0}, {2, 0}, {1, 1}, {0, 1}}, {{0, 1}, {0, 0}, {1, 1}, {1, 2}}};
        int[][][] Square =      new int[][][]{{{0, 0}, {1, 0}, {0, 1}, {1, 1}}};
        int[][][] Podium =         new int[][][]{{{1, 1}, {0, 1}, {1, 0}, {2, 1}}, {{0, 1}, {0, 0}, {1, 1}, {0, 2}}, {{1, 0}, {2, 0}, {1, 1}, {0, 0}}, {{1, 1}, {1, 2}, {0, 1}, {1, 0}}};

        // Select coordinates based on piece type and rotation
        switch (PieceId) {
            case 0 : {return Hero[PiecePosition];}
            case 1 : {return LNormal[PiecePosition];}
            case 2 : {return ReversedL[PiecePosition];}
            case 3 : {return NormalZ[PiecePosition];}
            case 4 : {return ReversedZ[PiecePosition];}
            case 5 : {return Square[PiecePosition];}
            case 6 : {return Podium[PiecePosition];}
            default: return new int[0][0];
        }
    }

    // Return the total number of rotations for a piece
    public int getPieceRotationCount(int PieceId) {
        // Counter for valid rotation positions
        int RotationCount = 0;
        // Iterate until an invalid rotation is found
        while (true) {
            try {
                // Try to get the coordinates for the current rotation
                getPieceCoordinates(PieceId, RotationCount);
                // Increment the counter
                RotationCount++;
            } catch (Exception e) {
                // Return the previous count after reaching an invalid rotation
                return RotationCount - 1;
            }
        }
    }

    // Getters and setters for piece properties
    public int getPieceStartIndex() {
        return PieceStartIndex;
    }

    public void setPieceStartIndex(int pieceStartIndex) {
        PieceStartIndex = pieceStartIndex;
    }

    public Color getPieceColor() {
        return getPieceColor(PieceId);
    }

    public Color getPieceColor(int pieceId) {
        return pieceColors[pieceId % pieceColors.length];
    }

    public void setPieceColor(Color pieceColor) {
        pieceColors[PieceId] = pieceColor;
    }

    public int[][] getCurrentPiece() {
        return CurrentPiece;
    }

    public void setCurrentPiece(int[][] currentPiece) {
        CurrentPiece = currentPiece;
    }

    public int getCurrentPieceRotation() {
        return CurrentPieceRotation;
    }

    public void setCurrentPieceRotation(int currentPieceRotation) {
        CurrentPieceRotation = currentPieceRotation;
    }

    public int getPieceId() {
        return PieceId;
    }

    public void setPieceId(int idPiece) {
        PieceId = idPiece;
    }
}
