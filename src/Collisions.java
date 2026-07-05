package tetris;

import java.util.ArrayList;

// Class handling collisions in Tetris
public class Collisions {

    // Instances that manage the board and pieces
    Board board = new Board();
    Piece piece = new Piece();

    // Board attributes cached for convenient access
    int BoardWidth = board.getBoardWidth();
    int BoardHeight = board.getBoardHeight();

    // Check for a collision during the next downward movement
    public boolean canMoveDown(String[][] Board, int[][] CurrentPiece) {
        return canMove(Board, CurrentPiece, 0, 1);
    }

    // Check for a collision in the negative x direction
    public boolean canMoveLeft(String[][] Board, int[][] CurrentPiece) {
        return canMove(Board, CurrentPiece, -1, 0);
    }

    // Check for a collision in the positive x direction
    public boolean canMoveRight(String[][] Board, int[][] CurrentPiece) {
        return canMove(Board, CurrentPiece, 1, 0);
    }

    // Validate any movement represented by a coordinate offset
    public boolean canMove(String[][] currentBoard, int[][] currentPiece, int deltaX, int deltaY) {
        for (int[] cell : currentPiece) {
            int targetX = cell[0] + deltaX;
            int targetY = cell[1] + deltaY;

            if (targetX < 0 || targetX >= BoardWidth || targetY < 0 || targetY >= BoardHeight) {
                return false;
            }
            if (board.isStaticPieceValue(currentBoard[targetX][targetY])) {
                return false;
            }
        }
        return true;
    }

    // Check for a collision while rotating the piece
    public boolean canRotate(String[][] Board, int[][] NewPiece, int[][] CurrentPiece, int OffsetX, int OffsetY) {
        int StaticPieceCollisionCount = 0;
        int WallCollisionCount = 0;
        for (int Cell = 0; Cell < CurrentPiece.length; Cell++) {
            int targetX = NewPiece[Cell][0] + OffsetX;
            int targetY = NewPiece[Cell][1] + OffsetY;
            if (targetX >= 0 && targetX < this.BoardWidth && targetY >= 0 && targetY < this.BoardHeight) {
                WallCollisionCount++;
                if (!board.isStaticPieceValue(Board[targetX][targetY])) {
                    StaticPieceCollisionCount++;
                }
            }
        }
        return StaticPieceCollisionCount == NewPiece.length && WallCollisionCount == NewPiece.length;
    }

    // Make the piece static after a downward collision
    public int makeStatic(String[][] Board, int[][] CurrentPiece) {
        ArrayList<Integer> CellYPositions = new ArrayList<>();
        for (int Cell = 0; Cell < CurrentPiece.length; Cell++) {
            int CellX = CurrentPiece[Cell][0];
            int CellY = CurrentPiece[Cell][1];
            int pieceId = board.getPieceIdFromValue(Board[CellX][CellY]);
            Board[CellX][CellY] = board.createStaticPieceValue(pieceId);
            CellYPositions.add(CellY);
        }
        return board.clearLines(Board, CellYPositions);
    }

    public boolean canPlacePiece(String[][] Board, int[][] CurrentPiece) {
        for (int[] cell : CurrentPiece) {
            int cellX = cell[0];
            int cellY = cell[1];
            if (cellX < 0 || cellX >= BoardWidth || cellY < 0 || cellY >= BoardHeight
                    || board.isStaticPieceValue(Board[cellX][cellY])) {
                return false;
            }
        }
        return true;
    }
}
