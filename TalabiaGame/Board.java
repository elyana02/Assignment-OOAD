import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Board {
    private static JButton[][] boardButtons;
    private static int[][] directions;

    public Board() {
        initializeBoard();
    }

    public void initializeBoard() {
        boardButtons = new JButton[6][7];
        directions = new int[6][7];

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(60, 60));
                boardButtons[row][col] = button;
                directions[row][col] = 0;
            }
        }

        for (int col = 0; col < 7; col++) {
            setPieceIcon("P1", 1, col);
            directions[1][col] = 1; // Player 1 pieces move down
        }

        for (int col = 0; col < 7; col++) {
            setPieceIcon("P2", 4, col);
            directions[4][col] = -1; // Player 2 pieces move up
        }

        setPieceIcon("Pl1", 0, 0);
        setPieceIcon("Pl1", 0, 6);
        setPieceIcon("Pl2", 5, 0);
        setPieceIcon("Pl2", 5, 6);

        setPieceIcon("T1", 0, 2);
        setPieceIcon("T1", 0, 4);
        setPieceIcon("T2", 5, 2);
        setPieceIcon("T2", 5, 4);

        setPieceIcon("H1", 0, 1);
        setPieceIcon("H1", 0, 5);
        setPieceIcon("H2", 5, 1);
        setPieceIcon("H2", 5, 5);

        setPieceIcon("S1", 0, 3);
        setPieceIcon("S2", 5, 3);
    }

    public static JButton[][] getBoardButtons() {
        return boardButtons;
    }

    public void setPieceIcon(String piece, int row, int col) {
        ImageIcon icon;

        try {
            switch (piece) {
                case "P1":
                    icon = new ImageIcon(getClass().getResource("p1.png"));
                    boardButtons[row][col].setActionCommand("P1");
                    break;
                case "P2":
                    icon = new ImageIcon(getClass().getResource("p2.png"));
                    boardButtons[row][col].setActionCommand("P2");
                    break;
                case "H1":
                    icon = new ImageIcon(getClass().getResource("h1.png"));
                    boardButtons[row][col].setActionCommand("H1");
                    break;
                case "H2":
                    icon = new ImageIcon(getClass().getResource("h2.png"));
                    boardButtons[row][col].setActionCommand("H2");
                    break;
                case "T1":
                    icon = new ImageIcon(getClass().getResource("t1.png"));
                    boardButtons[row][col].setActionCommand("T1");
                    break;
                case "T2":
                    icon = new ImageIcon(getClass().getResource("t2.png"));
                    boardButtons[row][col].setActionCommand("T2");
                    break;
                case "Pl1":
                    icon = new ImageIcon(getClass().getResource("plus1.png"));
                    boardButtons[row][col].setActionCommand("Pl1");
                    break;
                case "Pl2":
                    icon = new ImageIcon(getClass().getResource("plus2.png"));
                    boardButtons[row][col].setActionCommand("Pl2");
                    break;
                case "S1":
                    icon = new ImageIcon(getClass().getResource("s1.png"));
                    boardButtons[row][col].setActionCommand("S1");
                    break;
                case "S2":
                    icon = new ImageIcon(getClass().getResource("s2.png"));
                    boardButtons[row][col].setActionCommand("S2");
                    break;
                default:
                    icon = null;
                    break;
            }
            boardButtons[row][col].setIcon(icon);

        } catch (Exception e) {
            System.err.println("Error loading image for piece " + piece);
            e.printStackTrace();
            System.out.println("Resource Path: " + getClass().getResource("icon/" + piece.toLowerCase() + ".png"));
        }
    }

    public int getDirection(int row, int col) {
        return directions[row][col];
    }

    public void reverseDirection(int row, int col) {
        directions[row][col] *= -1;
    }

    public String getPieceAt(int row, int col) {
        return boardButtons[row][col].getActionCommand();
    }

    public void movePiece(int oldRow, int oldCol, int newRow, int newCol) {
        if (boardButtons[newRow][newCol].getActionCommand().equals("S1")
                || boardButtons[newRow][newCol].getActionCommand().equals("S2")) {
            JOptionPane.showMessageDialog(null, "Game Over! The sun has been captured.", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        if (!boardButtons[newRow][newCol].getActionCommand().isEmpty()
                && !boardButtons[newRow][newCol].getActionCommand()
                        .equals(boardButtons[oldRow][oldCol].getActionCommand())) {
            boardButtons[newRow][newCol].setIcon(boardButtons[oldRow][oldCol].getIcon());
            boardButtons[oldRow][oldCol].setIcon(null);
            boardButtons[newRow][newCol].setActionCommand(boardButtons[oldRow][oldCol].getActionCommand());
            boardButtons[oldRow][oldCol].setActionCommand("");
            JOptionPane.showMessageDialog(null, "Piece captured!", "Piece is captured!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            boardButtons[newRow][newCol].setIcon(boardButtons[oldRow][oldCol].getIcon());
            boardButtons[oldRow][oldCol].setIcon(null);
            boardButtons[newRow][newCol].setActionCommand(boardButtons[oldRow][oldCol].getActionCommand());
            boardButtons[oldRow][oldCol].setActionCommand("");
            directions[newRow][newCol] = directions[newRow][newCol] * -1;
        }
    }

    public static int[][] getDirections() {
        return directions;
    }

    public void updateBoardFromState(GameState gameState) {
        JButton[][] newBoardButtons = gameState.getBoardButtons();
        int[][] newDirections = gameState.getDirections();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                boardButtons[row][col].setIcon(newBoardButtons[row][col].getIcon());
                boardButtons[row][col].setActionCommand(newBoardButtons[row][col].getActionCommand());
                directions[row][col] = newDirections[row][col];
            }
        }
    }

    public void refreshBoard(GameState gameState) {
        for (int row = 0; row < boardButtons.length; row++) {
            for (int col = 0; col < boardButtons[row].length; col++) {
                String piece = gameState.getPieceAt(row, col);
                setPieceIcon(piece, row, col);
            }
        }
    }

    public void updateGameFromState(GameState loadedGameState) {
        JButton[][] newBoardButtons = loadedGameState.getBoardButtons();
        int[][] newDirections = loadedGameState.getDirections();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                boardButtons[row][col].setIcon(newBoardButtons[row][col].getIcon());
                boardButtons[row][col].setActionCommand(newBoardButtons[row][col].getActionCommand());
                directions[row][col] = newDirections[row][col];
            }
        }
    }

    public void refreshBoard() {
    }
}
