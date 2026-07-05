package tetris;

// Class managing Tetris piece movements
public class Movements {

    Board board = new Board();
    Piece piece = new Piece();
    Collisions collisions = new Collisions();

    // Movement directions expressed as coordinate changes
    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private static final int DOWN = 1;
    private static final int NO_CHANGE = 0;

    public void dropPiece(String[][] currentBoard, int[][] currentPiece) {
        movePiece(currentBoard, currentPiece, NO_CHANGE, DOWN);
    }

    public void moveRight(String[][] currentBoard, int[][] currentPiece) {
        movePiece(currentBoard, currentPiece, RIGHT, NO_CHANGE);
    }

    public void moveLeft(String[][] currentBoard, int[][] currentPiece) {
        movePiece(currentBoard, currentPiece, LEFT, NO_CHANGE);
    }

    // Apply the same movement process regardless of direction
    private boolean movePiece(String[][] currentBoard, int[][] currentPiece, int deltaX, int deltaY) {
        if (!collisions.canMove(currentBoard, currentPiece, deltaX, deltaY)) {
            return false;
        }

        String pieceValue = currentBoard[currentPiece[0][0]][currentPiece[0][1]];
        clearPieceFromBoard(currentBoard, currentPiece);
        translateCoordinates(currentPiece, deltaX, deltaY);
        updatePieceOnBoard(currentBoard, currentPiece, pieceValue);
        return true;
    }

    // Add a horizontal and vertical offset to every cell in a piece
    private void translateCoordinates(int[][] coordinates, int deltaX, int deltaY) {
        for (int[] cell : coordinates) {
            cell[0] += deltaX;
            cell[1] += deltaY;
        }
    }

    // Remove the piece from its previous board positions
    private void clearPieceFromBoard(String[][] currentBoard, int[][] coordinates) {
        String emptyValue = board.getBoardEmptyValue();
        for (int[] cell : coordinates) {
            currentBoard[cell[0]][cell[1]] = emptyValue;
        }
    }

    // Keep the Piece object and board synchronized after a movement
    private void updatePieceOnBoard(String[][] currentBoard, int[][] currentPiece, String pieceValue) {
        piece.setCurrentPiece(currentPiece);
        for (int[] cell : currentPiece) {
            currentBoard[cell[0]][cell[1]] = pieceValue;
        }
    }

    public boolean rotatePieceToPosition(
            String[][] currentBoard,
            int[][] currentPiece,
            int nextPieceRotation,
            int pieceId) {

        int[][] rotatedPiece = piece.getPieceCoordinates(pieceId, nextPieceRotation);
        int offsetX = currentPiece[0][0] - rotatedPiece[0][0];
        int offsetY = currentPiece[0][1] - rotatedPiece[0][1];

        if (!collisions.canRotate(currentBoard, rotatedPiece, currentPiece, offsetX, offsetY)) {
            return false;
        }

        String pieceValue = currentBoard[currentPiece[0][0]][currentPiece[0][1]];
        clearPieceFromBoard(currentBoard, currentPiece);
        translateCoordinates(rotatedPiece, offsetX, offsetY);
        copyCoordinates(rotatedPiece, currentPiece);
        updatePieceOnBoard(currentBoard, currentPiece, pieceValue);
        return true;
    }

    // Copy coordinates without replacing the original array reference
    private void copyCoordinates(int[][] source, int[][] destination) {
        for (int cell = 0; cell < source.length; cell++) {
            destination[cell][0] = source[cell][0];
            destination[cell][1] = source[cell][1];
        }
    }

    public void rotatePiece(String[][] currentBoard, int[][] currentPiece, int pieceId) {
        int rotationCount = piece.getPieceRotationCount(pieceId);
        int nextPieceRotation = piece.getCurrentPieceRotation();

        if (rotationCount > nextPieceRotation) {
            nextPieceRotation++;
        } else {
            nextPieceRotation = 0;
        }

        if (rotatePieceToPosition(currentBoard, currentPiece, nextPieceRotation, pieceId)) {
            piece.setCurrentPieceRotation(nextPieceRotation);
        }
    }
}
