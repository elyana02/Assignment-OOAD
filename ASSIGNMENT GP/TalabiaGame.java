import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TalabiaGame extends JFrame {
    private JButton[][] boardButtons;
    private int[][] directions;
    private boolean isPlayer1Turn;

    public TalabiaGame() {
        initializeBoard();
        setupGUI();
        isPlayer1Turn = true; // Player 1 always starts
    }

    private void initializeBoard() {
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

        // Fill second row with Player 1 point pieces
        for (int col = 0; col < 7; col++) {
            setPieceIcon("P1", 1, col);
            directions[1][col] = 1; // Player 1 pieces move down
        }

        // Fill fifth row with Player 2 point pieces
        for (int col = 0; col < 7; col++) {
            setPieceIcon("P2", 4, col);
            directions[4][col] = -1; // Player 2 pieces move up
        }

        // Add plus pieces in the four corners
        setPieceIcon("Pl1", 0, 0); // Player 1 plus pieces
        setPieceIcon("Pl1", 0, 6); // Player 1 plus pieces
        setPieceIcon("Pl2", 5, 0); // Player 2 plus pieces
        setPieceIcon("Pl2", 5, 6); // Player 2 plus pieces

        // Add time pieces on both sides of kings
        setPieceIcon("T1", 0, 2); // Player 1 time pieces
        setPieceIcon("T1", 0, 4); // Player 1 time pieces
        setPieceIcon("T2", 5, 2); // Player 2 time pieces
        setPieceIcon("T2", 5, 4); // Player 2 time pieces

        // Add hourglass pieces between rooks and bishops
        setPieceIcon("H1", 0, 1); // Player 1 hourglass pieces
        setPieceIcon("H1", 0, 5); // Player 1 hourglass pieces
        setPieceIcon("H2", 5, 1); // Player 2 hourglass pieces
        setPieceIcon("H2", 5, 5); // Player 2 hourglass pieces

        // Add sun pieces at the specified positions
        setPieceIcon("S1", 0, 3); // Player 1 sun piece
        setPieceIcon("S2", 5, 3); // Player 2 sun piece

    }

    private void setupGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Talabia Game");

        JPanel boardPanel = new JPanel(new GridLayout(6, 7));

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                boardPanel.add(boardButtons[row][col]);
                boardButtons[row][col].addActionListener(new ChessButtonListener(row, col));
            }
        }

        add(boardPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Update the button icon
    private void setPieceIcon(String piece, int row, int col) {
        ImageIcon icon;

        try {
            switch (piece) {
                case "P1":
                    icon = new ImageIcon(getClass().getResource("p1.png"));
                    break;
                case "P2":
                    icon = new ImageIcon(getClass().getResource("p2.png"));
                    break;
                case "H1":
                    icon = new ImageIcon(getClass().getResource("h1.png"));
                    break;
                case "H2":
                    icon = new ImageIcon(getClass().getResource("h2.png"));
                    break;
                case "T1":
                    icon = new ImageIcon(getClass().getResource("t1.png"));
                    break;
                case "T2":
                    icon = new ImageIcon(getClass().getResource("t2.png"));
                    break;
                case "Pl1":
                    icon = new ImageIcon(getClass().getResource("plus1.png"));
                    break;
                case "Pl2":
                    icon = new ImageIcon(getClass().getResource("plus2.png"));
                    break;
                case "S1":
                    icon = new ImageIcon(getClass().getResource("s1.png"));
                    break;
                case "S2":
                    icon = new ImageIcon(getClass().getResource("s2.png"));
                    break;
                default:
                    icon = null;
                    break;

            }
            boardButtons[row][col].setIcon(icon);
            boardButtons[row][col].setActionCommand(piece); // Set piece information as action command

        } catch(Exception e) {
            System.out.println("Image Error");
        }
    }

    private class ChessButtonListener implements ActionListener {
        private int row;
        private int col;
        private int direction;

        public ChessButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
            this.direction = directions[row][col];
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String piece = boardButtons[row][col].getText();
            if (isPlayer1Turn && piece.endsWith("1") || !isPlayer1Turn && piece.endsWith("2")) {
                        // Check if the clicked button has a point piece
                if (boardButtons[row][col].getText().equals("P1") || boardButtons[row][col].getText().equals("P2")) {
                    // Check if the piece can move 1 or 2 steps forward
                    if (isValidMove(row + direction, col) || isValidMove(row + 2 * direction, col)) {
                        // Instead of automatically moving the piece, display a dialog box with the possible moves
                        Object[] options = {"Move 1 step", "Move 2 steps"};
                        int n = JOptionPane.showOptionDialog(null,
                            "Choose a move",
                            "Multiple valid moves",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
            
                        // Move the piece based on the player's choice
                        if (n == 0) {
                            movePiece(row, col, row + direction, col);
                        } else if (n == 1) {
                            movePiece(row, col, row + 2 * direction, col);
                        }
            
                        // If the piece reached the end of the board, change its direction
                        if (row + direction == 0 || row + direction == 5) {
                            direction *= -1;
                        }
                    }
                }
                // Check if the clicked button has an hourglass piece
                else if (boardButtons[row][col].getText().equals("H1") || boardButtons[row][col].getText().equals("H2")) {
                    // Check all possible L-shaped moves
                    for (int[] move : new int[][]{{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}}) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];
                        if (isValidMove(newRow, newCol)) {
                            // Move the piece
                            movePiece(row, col, newRow, newCol);
                            break;
                        }
                    }
                }
                // Check if the clicked button has a time piece
                else if (boardButtons[row][col].getText().equals("T1") || boardButtons[row][col].getText().equals("T2")) {
                    // Check all possible diagonal moves
                    for (int dRow = -1; dRow <= 1; dRow += 2) {
                        for (int dCol = -1; dCol <= 1; dCol += 2) {
                            int dist;
                            for (dist = 1; dist < 6; dist++) {
                                int newRow = row + dRow * dist;
                                int newCol = col + dCol * dist;
                                if (!isValidMove(newRow, newCol)) {
                                    // If the piece cannot move because another piece is in the way, stop checking in this direction
                                    break;
                                }
                            }
                            // Move the piece to the furthest valid position
                            if (dist > 1) {
                                movePiece(row, col, row + dRow * (dist - 1), col + dCol * (dist - 1));
                            }
                        }
                    }
                }
                // Check if the clicked button has a plus piece
                else if (boardButtons[row][col].getText().equals("Pl1") || boardButtons[row][col].getText().equals("Pl2")) {
                    // Check all possible horizontal and vertical moves
                    for (int[] move : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                        int dist;
                        for (dist = 1; dist < 6; dist++) {
                            int newRow = row + move[0] * dist;
                            int newCol = col + move[1] * dist;
                            if (!isValidMove(newRow, newCol)) {
                                // If the piece cannot move because another piece is in the way, stop checking in this direction
                                break;
                            }
                        }
                        // Move the piece to the furthest valid position
                        if (dist > 1) {
                            movePiece(row, col, row + move[0] * (dist - 1), col + move[1] * (dist - 1));
                        }
                    }
                }
                // Check if the clicked button has a sun piece
                else if (boardButtons[row][col].getText().equals("S1") || boardButtons[row][col].getText().equals("S2")) {
                    // Check all possible moves in any direction
                    for (int[] move : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}}) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];
                        if (isValidMove(newRow, newCol)) {
                            // Move the piece
                            movePiece(row, col, newRow, newCol);
                            break;
                        }
                    }
                }
            }
        }
    }

        private boolean isValidMove(int newRow, int newCol) {
            // Check if the new position is within the board and is empty
            return newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 7 && boardButtons[newRow][newCol].getActionCommand().equals("");
        }

        private void movePiece(int oldRow, int oldCol, int newRow, int newCol) {

            isPlayer1Turn = !isPlayer1Turn; // Switch the turn
            // Check if the game is over
            if (boardButtons[newRow][newCol].getText().equals("S1") || boardButtons[newRow][newCol].getText().equals("S2")) {
                System.out.println("Game Over! The sun has been captured.");
                System.exit(0);
            }

            // Check if the destination square contains an opposing player's piece
            if (!boardButtons[newRow][newCol].getActionCommand().isEmpty() &&
            boardButtons[newRow][newCol].getActionCommand().charAt(1) != boardButtons[oldRow][oldCol].getActionCommand().charAt(1)) {

                // Capture the piece
                boardButtons[newRow][newCol].setIcon(boardButtons[oldRow][oldCol].getIcon());
                boardButtons[oldRow][oldCol].setIcon(null);
                boardButtons[newRow][newCol].setActionCommand(boardButtons[oldRow][oldCol].getActionCommand());
                boardButtons[oldRow][oldCol].setActionCommand("");
                System.out.println("Piece captured!");

            } else {
                // Move the piece from the old position to the new position
                boardButtons[newRow][newCol].setIcon(boardButtons[oldRow][oldCol].getIcon());
                boardButtons[oldRow][oldCol].setIcon(null);
                boardButtons[newRow][newCol].setActionCommand(boardButtons[oldRow][oldCol].getActionCommand());
                boardButtons[oldRow][oldCol].setActionCommand("");

                // To update the direction after a successful move
                directions[newRow][newCol] = directions[newRow][newCol] * -1;
            }
        }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TalabiaGame());
    }
}