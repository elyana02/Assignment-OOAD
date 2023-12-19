import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGame extends JFrame {
    private JButton[][] boardButtons;
    private int[][] directions;

    public ChessGame() {
        initializeBoard();
        setupGUI();
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
            boardButtons[1][col].setText("P1");
            directions[1][col] = 1; // Player 1 pieces move down
        }

        // Fill fifth row with Player 2 point pieces
        for (int col = 0; col < 7; col++) {
            boardButtons[4][col].setText("P2");
            directions[4][col] = -1; // Player 2 pieces move up
        }

        // Add plus pieces in the four corners
        boardButtons[0][0].setText("Pl1"); // Player 1 plus pieces
        boardButtons[0][6].setText("Pl1"); // Player 1 plus pieces
        boardButtons[5][0].setText("Pl1"); // Player 2 plus pieces
        boardButtons[5][6].setText("Pl1"); // Player 2 plus pieces

        // Add time pieces on both sides of kings
        boardButtons[0][2].setText("T1"); // Player 1 time pieces
        boardButtons[0][4].setText("T1"); // Player 1 time pieces
        boardButtons[5][2].setText("T2"); // Player 2 time pieces
        boardButtons[5][4].setText("T2"); // Player 2 time pieces

        // Add hourglass pieces between rooks and bishops
        boardButtons[0][1].setText("H1"); // Player 1 hourglass pieces
        boardButtons[0][5].setText("H1"); // Player 1 hourglass pieces
        boardButtons[5][1].setText("H2"); // Player 2 hourglass pieces
        boardButtons[5][5].setText("H2"); // Player 2 hourglass pieces

        // Add sun pieces at the specified positions
        boardButtons[0][3].setText("S1"); // Player 1 sun piece
        boardButtons[5][3].setText("S2"); // Player 2 sun piece
    }

    private void setupGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chess Game");

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
    
        private boolean isValidMove(int newRow, int newCol) {
            // Check if the new position is within the board and is empty
            return newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 7 && boardButtons[newRow][newCol].getText().equals("");
        }
    
        private void movePiece(int oldRow, int oldCol, int newRow, int newCol) {
            // Check if the game is over
            if (boardButtons[newRow][newCol].getText().equals("S1") || boardButtons[newRow][newCol].getText().equals("S2")) {
                System.out.println("Game Over! The sun has been captured.");
                System.exit(0);
            }
    
            // Check if the destination square contains an opposing player's piece
            if (!boardButtons[newRow][newCol].getText().isEmpty() &&
                boardButtons[newRow][newCol].getText().charAt(1) != boardButtons[oldRow][oldCol].getText().charAt(1)) {
                
                // Capture the piece
                boardButtons[newRow][newCol].setText(boardButtons[oldRow][oldCol].getText());
                boardButtons[oldRow][oldCol].setText("");
                System.out.println("Piece captured!");
    
            } else {
                // Move the piece from the old position to the new position
                boardButtons[newRow][newCol].setText(boardButtons[oldRow][oldCol].getText());
                boardButtons[oldRow][oldCol].setText("");
            }
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGame());
    }
}