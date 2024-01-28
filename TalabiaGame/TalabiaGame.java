import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class TalabiaGame {
    public Board board;
    private Player currentPlayer;
    private int yellowTurns;
    private int blueTurns;

    public TalabiaGame(Board board, Player currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.yellowTurns = 0;
        this.blueTurns = 0;
        this.board.initializeBoard();
    }

    private boolean isValidPlayerMove(String piece) {
        // Check if the piece belongs to the current player
        if ((currentPlayer.getPlayerNumber() == 1 && (piece.equals("P1") || piece.equals("H1") || piece.equals("T1")
                || piece.equals("Pl1") || piece.equals("S1")))
                || (currentPlayer.getPlayerNumber() == 2 && (piece.equals("P2") || piece.equals("H2") || piece.equals("T2")
                        || piece.equals("Pl2") || piece.equals("S2")))) {
            return true;
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }

    public class ChessButtonListener implements ActionListener {
        private int row;
        private int col;
        private int direction;

        public ChessButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
            this.direction = (board != null) ? board.getDirection(row, col) : 0;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if the clicked button is empty
            if (board.getPieceAt(row, col).isEmpty()) {
                return;
            }

            // Check if it's the correct player's turn
            if (isValidPlayerMove(board.getPieceAt(row, col))) {
                System.out.println("player:" + currentPlayer.getPlayerNumber());
                System.out.println("Old Position: [" + row + ", " + col + "]");
                System.out.println("Player's Piece:" + board.getPieceAt(row, col));

                // Check if the clicked button has a point piece
                if (board.getPieceAt(row, col).equals("P1") || board.getPieceAt(row, col).equals("P2")) {

                    // Instead of automatically moving the piece, display a dialog box with the possible moves
                    Object[] options = { "Move 1 box", "Move 2 boxes" };
                    int n = JOptionPane.showOptionDialog(null, "Choose a move", "Multiple valid moves",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    // Check the player's choice
                    if (n == JOptionPane.CLOSED_OPTION) {
                        // Player closed the dialog without choosing, you can handle this case here
                        JOptionPane.showMessageDialog(null, "Please choose a move.", "Move Canceled",
                                JOptionPane.INFORMATION_MESSAGE);
                        return; // Return without switching the turn
                    }

                    System.out.println("Move choice: " + n);

                    // Move the piece based on the player's choice
                    if (n == 0) { // 1 box
                        System.out.println("New box piece:" + board.getPieceAt(row + direction, col));

                        if (isValidPointMove(row + direction, col, 0)) {
                            board.movePiece(row, col, row + direction, col);
                            updatePieceLabel(row, col, row + direction, col);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Move: Pieces cannot be skipped.",
                                    "Invalid Move", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                    } else if (n == 1) { // 2 boxes
                        System.out.println("New box piece:" + board.getPieceAt(row + 2 * direction, col));

                        // Check if the move is valid using isValidPointMove
                        if (isValidPointMove(row + 2 * direction, col, 1)) {
                            board.movePiece(row, col, row + 2 * direction, col);
                            updatePieceLabel(row, col, row + direction, col);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Move: Pieces cannot be skipped.",
                                    "Invalid Move", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // If the piece reached the end of the board, change its direction
                    if (row + direction == 0 || row + direction == 5) {
                        direction *= -1;
                    }
                }

                // Check if the clicked button has an hourglass piece
                else if (board.getPieceAt(row, col).equals("H1") || board.getPieceAt(row, col).equals("H2")) {
                    // Store valid moves in a list
                    List<int[]> validMoves = new ArrayList<>();

                    // Check all possible L-shaped moves
                    for (int[] move : new int[][] { { -2, -1 }, { -2, 1 }, { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 },
                            { 2, -1 }, { 2, 1 } }) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];
                        if (isValidMove(newRow, newCol)) {
                            validMoves.add(new int[] { newRow, newCol });
                        }
                    }

                    // If there are valid moves, allow the player to choose
                    if (!validMoves.isEmpty()) {
                        Object[] options = new Object[validMoves.size()];
                        for (int i = 0; i < validMoves.size(); i++) {
                            options[i] = "Move to " + Arrays.toString(validMoves.get(i));
                        }

                        int n = JOptionPane.showOptionDialog(null, "Choose a move", "Multiple valid moves",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                        // Check the player's choice
                        if (n == JOptionPane.CLOSED_OPTION) {
                            // Player closed the dialog without choosing, you can handle this case here
                            JOptionPane.showMessageDialog(null, "Please choose a move.", "Move Canceled",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return; // Return without switching the turn
                        }

                        // Get the chosen move and move the piece
                        int[] chosenMove = validMoves.get(n);
                        board.movePiece(row, col, chosenMove[0], chosenMove[1]);
                    } else {
                        // No valid moves
                        JOptionPane.showMessageDialog(null, "No valid moves available.", "Invalid Move",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Check if the clicked button has a time piece
                else if (board.getPieceAt(row, col).equals("T1") || board.getPieceAt(row, col).equals("T2")) {
                    // Check all possible diagonal moves
                    for (int dRow = -1; dRow <= 1; dRow += 2) {
                        for (int dCol = -1; dCol <= 1; dCol += 2) {
                            int dist;
                            for (dist = 1; dist < 6; dist++) {
                                int newRow = row + dRow * dist;
                                int newCol = col + dCol * dist;

                                // Check if the move is within the board boundaries
                                if (newRow < 0 || newRow >= 6 || newCol < 0 || newCol >= 7) {
                                    break;
                                }

                                // Check if the destination square is empty
                                if (!board.getPieceAt(newRow, newCol).isEmpty()) {
                                    // If the piece belongs to the opponent, capture it
                                    if (board.getPieceAt(newRow, newCol).charAt(1) != board.getPieceAt(row, col)
                                            .charAt(1)) {
                                        board.movePiece(row, col, newRow, newCol);
                                        break;
                                    } else {
                                        // If the square is occupied by the player's own piece, the move is not valid
                                        JOptionPane.showMessageDialog(null, "No valid moves available.", "Invalid Move",
                                                JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                // Check if the clicked button has a plus piece
                else if (board.getPieceAt(row, col).equals("Pl1") || board.getPieceAt(row, col).equals("Pl2")) {
                    // Check all possible horizontal and vertical moves
                    for (int[] move : new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];

                        // Continue moving in the same direction until reaching the board boundary or an obstacle
                        while (isValidMove(newRow, newCol) && board.getPieceAt(newRow, newCol).isEmpty()) {
                            newRow += move[0];
                            newCol += move[1];
                        }

                        // Check if the final position is within the board boundaries
                        if (isValidMove(newRow, newCol)) {
                            // Check if the destination square is either empty or occupied by the opponent's piece
                            if (board.getPieceAt(newRow, newCol).isEmpty() || isOpponentPiece(newRow, newCol)) {
                                board.movePiece(row, col, newRow, newCol);
                                return; // Exit the method after a valid move
                            }
                        }
                    }

                    // If no valid moves are found, display an error message
                    JOptionPane.showMessageDialog(null, "Invalid Move: No valid moves available.", "Invalid Move",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Check if the clicked button has a sun piece
                else if (board.getPieceAt(row, col).equals("S1") || board.getPieceAt(row, col).equals("S2")) {
                    // Check all possible moves in any direction
                    for (int[] move : new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, -1 }, { -1, 1 },
                            { 1, -1 }, { 1, 1 } }) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];
                        if (isValidMove(newRow, newCol)) {
                            // Move the piece
                            board.movePiece(row, col, newRow, newCol);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "No valid moves available.", "Invalid Move",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Switch turn after a valid move
                if (currentPlayer.getPlayerNumber() == 1) {
                    currentPlayer = new Player(2);
                } else {
                    currentPlayer = new Player(1);
                }

                // Increment turns for the current player
                if (currentPlayer.getPlayerNumber() == 1) {
                    yellowTurns++;
                } else {
                    blueTurns++;
                }

                // Check if both players have taken two turns each
                if (yellowTurns == 2 && blueTurns == 2) {
                    // Reset turn counters
                    yellowTurns = 0;
                    blueTurns = 0;

                    // Change time pieces to plus pieces and vice versa
                    changeTimeAndPlusPieces();
                }
            } else {
                JOptionPane.showMessageDialog(null, "It's not your turn!", "Invalid Move", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean isValidMove(int newRow, int newCol) {
            // Check if the new position is within the board
            if (newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 7) {
                // Check if the new position has an opponent piece
                return !isValidPlayerMove(board.getPieceAt(newRow, newCol));
            }
            return false;
        }

        private boolean isValidPointMove(int newRow, int newCol, int n) {
            int direction = board.getDirection(row, col);

            // check if the new position has an opponent piece
            if (!isValidPlayerMove(board.getPieceAt(newRow, newCol))) {
                if (n == 0) {
                    // Check if the new position is within the board
                    return newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 7;
                } else if (n == 1) {
                    // Check if the new position is within the board and is empty
                    if (newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 7
                            && board.getPieceAt(newRow - direction, newCol).isEmpty()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }

        private boolean isOpponentPiece(int newRow, int newCol) {
            // Check if the piece at the destination position belongs to the opponent
            int opponentPlayerNumber = (currentPlayer.getPlayerNumber() == 1) ? 2 : 1;
            return board.getPieceAt(newRow, newCol).startsWith("P" + opponentPlayerNumber)
                    || board.getPieceAt(newRow, newCol).startsWith("H" + opponentPlayerNumber)
                    || board.getPieceAt(newRow, newCol).startsWith("T" + opponentPlayerNumber)
                    || board.getPieceAt(newRow, newCol).startsWith("Pl" + opponentPlayerNumber)
                    || board.getPieceAt(newRow, newCol).startsWith("S" + opponentPlayerNumber);
        }

        private void changeTimeAndPlusPieces() {
            for (int r = 0; r < 6; r++) {
                for (int c = 0; c < 7; c++) {
                    String piece = board.getPieceAt(r, c);
                    if (piece.equals("T1")) {
                        board.setPieceIcon("Pl1", r, c);
                        board.reverseDirection(r, c); // Reverse direction for plus piece
                    } else if (piece.equals("T2")) {
                        board.setPieceIcon("Pl2", r, c);
                        board.reverseDirection(r, c); // Reverse direction for plus piece
                    } else if (piece.equals("Pl1")) {
                        board.setPieceIcon("T1", r, c);
                        // Update direction for time piece based on the current direction
                        if (board.getDirection(r, c) == 1) {
                            board.reverseDirection(r, c);
                        }
                    } else if (piece.equals("Pl2")) {
                        board.setPieceIcon("T2", r, c);
                        // Update direction for time piece based on the current direction
                        if (board.getDirection(r, c) == -1) {
                            board.reverseDirection(r, c);
                        }
                    }
                }
            }
        }
        
        // Helper method to update piece labels after a move
        private void updatePieceLabel(int oldRow, int oldCol, int newRow, int newCol) {
            String piece = board.getPieceAt(newRow, newCol);
            String pieceLabel = "";
        
            // Determine the piece label based on the piece type
            switch (piece) {
                case "P1":
                    pieceLabel = "P1";
                    break;
                case "P2":
                    pieceLabel = "P2";
                    break;
                case "H1":
                    pieceLabel = "H1";
                    break;
                case "H2":
                    pieceLabel = "H2";
                    break;
                case "T1":
                    pieceLabel = "T1";
                    break;
                case "T2":
                    pieceLabel = "T2";
                    break;
                case "Pl1":
                    pieceLabel = "Pl1";
                    break;
                case "Pl2":
                    pieceLabel = "Pl2";
                    break;
                case "S1":
                    pieceLabel = "S1";
                    break;
                case "S2":
                    pieceLabel = "S2";
                    break;
            }
        
            // Set the piece label at the new position
            board.setPieceIcon(pieceLabel, newRow, newCol);
        
            // Clear the piece label at the old position
            board.setPieceIcon("", oldRow, oldCol);
        }
    }
}