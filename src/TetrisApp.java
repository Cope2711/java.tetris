package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// Main class representing the Tetris application
public class TetrisApp extends JPanel {

    private static final int NATURAL_DROP_DELAY = 500;

    // Window configuration
    String windowTitle = "Tetris!!!";
    int windowWidth = 400;
    int windowHeight = 800;

    // Game systems
    Board board = new Board();
    Piece piece = new Piece();
    Movements movements = new Movements();
    Collisions collisions = new Collisions();
    Timer gameTimer;

    int score = 0;
    boolean paused = false;
    boolean gameOver = false;

    public static void main(String[] args) {
        // Swing components and game updates run on the event dispatch thread
        SwingUtilities.invokeLater(() -> new TetrisApp().startGame());
    }

    public void startGame() {
        board.createBoard();
        createWindow();
        gameTimer = new Timer(NATURAL_DROP_DELAY, event -> executePieceDrop());
        generateNewPiece();
        gameTimer.start();
    }

    public void generateNewPiece() {
        piece.insertNewPiece();
        if (collisions.canPlacePiece(board.getBoard(), piece.getCurrentPiece())) {
            piece.updatePiece(board.getBoard());
        } else {
            gameOver = true;
            gameTimer.stop();
            repaint();
        }
    }

    public void executePieceDrop() {
        if (paused || gameOver) {
            return;
        }

        String[][] currentBoard = board.getBoard();
        int[][] currentPiece = piece.getCurrentPiece();
        if (collisions.canMoveDown(currentBoard, currentPiece)) {
            movements.dropPiece(currentBoard, currentPiece);
        } else {
            int clearedLines = collisions.makeStatic(currentBoard, currentPiece);
            score += clearedLines * 100;
            piece.setCurrentPieceRotation(0);
            generateNewPiece();
        }
        repaint();
    }

    public void createWindow() {
        JFrame frame = new JFrame(this.windowTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(this.windowWidth, this.windowHeight);
        frame.setResizable(false);
        frame.add(this);
        configureControls(frame);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Key bindings keep working while the game window is active
    private void configureControls(JFrame frame) {
        JComponent inputTarget = frame.getRootPane();
        bindKey(inputTarget, KeyEvent.VK_A, "moveLeft", () -> moveCurrentPiece(-1));
        bindKey(inputTarget, KeyEvent.VK_D, "moveRight", () -> moveCurrentPiece(1));
        bindKey(inputTarget, KeyEvent.VK_W, "rotate", this::rotateCurrentPiece);
        bindKey(inputTarget, KeyEvent.VK_S, "softDrop", this::softDrop);
        bindKey(inputTarget, KeyEvent.VK_DOWN, "arrowSoftDrop", this::softDrop);
        bindKey(inputTarget, KeyEvent.VK_SPACE, "hardDrop", this::hardDrop);
        bindKey(inputTarget, KeyEvent.VK_P, "pause", this::togglePause);
        bindKey(inputTarget, KeyEvent.VK_R, "restart", this::restartGame);
    }

    private void bindKey(JComponent target, int keyCode, String actionName, Runnable action) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0, false);
        target.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionName);
        target.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                action.run();
            }
        });
    }

    private void softDrop() {
        if (!paused && !gameOver) {
            executePieceDrop();
        }
    }

    private void moveCurrentPiece(int direction) {
        if (paused || gameOver) {
            return;
        }
        if (direction < 0) {
            movements.moveLeft(board.getBoard(), piece.getCurrentPiece());
        } else {
            movements.moveRight(board.getBoard(), piece.getCurrentPiece());
        }
        repaint();
    }

    private void rotateCurrentPiece() {
        if (paused || gameOver) {
            return;
        }
        movements.rotatePiece(board.getBoard(), piece.getCurrentPiece(), piece.getPieceId());
        repaint();
    }

    private void hardDrop() {
        if (paused || gameOver) {
            return;
        }

        String[][] currentBoard = board.getBoard();
        int[][] currentPiece = piece.getCurrentPiece();
        while (collisions.canMoveDown(currentBoard, currentPiece)) {
            movements.dropPiece(currentBoard, currentPiece);
        }
        // Lock the piece and create the next one immediately
        executePieceDrop();
    }

    private void togglePause() {
        if (!gameOver) {
            paused = !paused;
            repaint();
        }
    }

    private void restartGame() {
        if (!gameOver) {
            return;
        }

        score = 0;
        paused = false;
        gameOver = false;
        piece.setCurrentPieceRotation(0);
        board.createBoard();
        generateNewPiece();
        gameTimer.restart();
        repaint();
    }

    private void paintBoard(Graphics graphics) {
        int boardWidth = board.getBoardWidth();
        int boardHeight = board.getBoardHeight();
        int boardPositionX = board.getBoardPositionX();
        int boardPositionY = board.getBoardPositionY();
        int boardCellSizeX = board.getBoardCellSizeX();
        int boardCellSizeY = board.getBoardCellSizeY();
        Color boardEmptyColor = board.getBoardEmptyColor();
        String boardEmptyValue = board.getBoardEmptyValue();
        String[][] currentBoard = board.getBoard();

        for (int height = 0; height < boardHeight; height++) {
            for (int width = 0; width < boardWidth; width++) {
                int x = boardPositionX + width * boardCellSizeX;
                int y = boardPositionY + height * boardCellSizeY;
                String value = currentBoard[width][height];

                if (boardEmptyValue.equals(value)) {
                    graphics.setColor(boardEmptyColor);
                    graphics.drawRect(x, y, boardCellSizeX, boardCellSizeY);
                } else if (board.isMovingPieceValue(value) || board.isStaticPieceValue(value)) {
                    int pieceId = board.getPieceIdFromValue(value);
                    graphics.setColor(piece.getPieceColor(pieceId));
                    graphics.fillRect(x, y, boardCellSizeX, boardCellSizeY);
                }
            }
        }

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("SansSerif", Font.BOLD, 16));
        graphics.drawString("Score: " + score, 50, 35);
        if (paused) {
            graphics.drawString("PAUSED", 260, 35);
        } else if (gameOver) {
            graphics.drawString("GAME OVER", 230, 35);
            graphics.drawString("Press R to restart", 120, 735);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        paintBoard(graphics);
    }
}
