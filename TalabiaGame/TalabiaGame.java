import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

//Act as Game Controller
public class TalabiaGame
{
    private static TalabiaGame instance;  // Singleton instance
    public Board board;
    private Player currentPlayer;
    private int yellowTurns;
    private int blueTurns;

    // Private constructor to prevent external instantiation
    private TalabiaGame(Board board, Player currentPlayer) 
    {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.yellowTurns = 0;
        this.blueTurns = 0;
        this.board.initializeBoard();
    }

    // Public method to get the singleton instance
    public static TalabiaGame getInstance(Board board, Player currentPlayer) {
        if (instance == null) {
            instance = new TalabiaGame(board, currentPlayer);
        }
        return instance;
    }

    private boolean isValidPlayerMove(String piece) 
    {
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

    public class ChessButtonListener implements ActionListener
    {
        private int row;
        private int col;
        private int direction;

        public ChessButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
            this.direction = (board != null) ? board.getDirection(row,col) : 0;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            // Check if the clicked button is empty
            if (board.getPieceAt(row, col).isEmpty()) 
            {
                return;
            }

            // Check if it's the correct player's turn
            if(isValidPlayerMove(board.getPieceAt(row, col))) 
            {
                System.out.println("player:" + currentPlayer.getPlayerNumber());
                System.out.println("Old Position: [" + row + ", " + col + "]");
                System.out.println("Player's Piece:" + board.getPieceAt(row, col));

                // Check if the clicked button has a point piece
                if (board.getPieceAt(row, col).equals("P1")
                || board.getPieceAt(row, col).equals("P2")) {

                    // Store valid moves in a list
                    List<int[]> validMoves = new ArrayList<>();

                    // Check all possible moves
                    for (int i = 1; i <= 2; i++) {
                        int newRow = row + i * direction;
                        int newCol = col;

                        // Check if the move is valid using isValidPointMove
                        if (isValidPMove(row, col, newRow, newCol)) {
                            validMoves.add(new int[]{newRow, newCol});
                        }
                    }

                    // If there are valid moves, allow the player to choose
                    if (!validMoves.isEmpty()) {
                        Object[] options = new Object[validMoves.size()];
                        for (int i = 0; i < validMoves.size(); i++) {
                            options[i] = "Move to " + Arrays.toString(validMoves.get(i));
                        }

                        int n = JOptionPane.showOptionDialog(null,
                                "Choose a move",
                                "Point valid moves",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        // Check the player's choice
                        if (n == JOptionPane.CLOSED_OPTION) {
                            // Player closed the dialog without choosing, you can handle this case here
                            JOptionPane.showMessageDialog(null, "Please choose a move.", "Move Canceled", JOptionPane.INFORMATION_MESSAGE);
                            return;  // Return without switching the turn
                        }

                        // Get the chosen move and move the piece
                        int[] chosenMove = validMoves.get(n);
                        board.movePiece(row, col, chosenMove[0], chosenMove[1]);

                        this.direction = (board != null) ? board.getDirection(chosenMove[0], chosenMove[1]) : 0;
                    } else {
                        // No valid moves
                        JOptionPane.showMessageDialog(null, "Invalid Move: No valid moves available.", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                        return;
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
                    for (int[] move : new int[][]{{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}}) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];

                        if (isValidMove(newRow, newCol)) {
                            validMoves.add(new int[]{newRow, newCol});
                        }
                    }

                    // If there are valid moves, allow the player to choose
                    if (!validMoves.isEmpty()) {
                        Object[] options = new Object[validMoves.size()];
                        for (int i = 0; i < validMoves.size(); i++) {
                            options[i] = "Move to " + Arrays.toString(validMoves.get(i));
                        }

                        int n = JOptionPane.showOptionDialog(null,
                                "Choose a move",
                                "Hourglass valid moves",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        // Check the player's choice
                        if (n == JOptionPane.CLOSED_OPTION) {
                            // Player closed the dialog without choosing, you can handle this case here
                            JOptionPane.showMessageDialog(null, "Please choose a move.", "Move Canceled", JOptionPane.INFORMATION_MESSAGE);
                            return;  // Return without switching the turn
                        }

                        // Get the chosen move and move the piece
                        int[] chosenMove = validMoves.get(n);
                        board.movePiece(row, col, chosenMove[0], chosenMove[1]);
                    } else {
                        // No valid moves
                        JOptionPane.showMessageDialog(null, "No valid moves available.", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Check if the clicked button has a time piece
                else if (board.getPieceAt(row, col).equals("T1") || board.getPieceAt(row, col).equals("T2")) {
                    // Store valid moves in a list
                    List<int[]> validMoves = new ArrayList<>();

                    // Check all possible diagonal moves
                    for (int dRow = -1; dRow <= 1; dRow += 2) {
                        for (int dCol = -1; dCol <= 1; dCol += 2) {
                            int dist;
                            for (dist = 1; dist < 6; dist++) {
                                int newRow = row + dRow * dist;
                                int newCol = col + dCol * dist;
                                if(isValidTimeMove(row, col, newRow, newCol, dRow, dCol)) {
                                    // Add the current position as a valid move
                                    validMoves.add(new int[]{newRow, newCol});
                                } else
                                    break;    // Stop checking in this direction if not valid
                            }
                        }
                    }

                    // If there are valid moves, allow the player to choose
                    if (!validMoves.isEmpty()) {
                        Object[] options = new Object[validMoves.size()];
                        for (int i = 0; i < validMoves.size(); i++) {
                            options[i] = "Move to " + Arrays.toString(validMoves.get(i));
                        }

                        int n = JOptionPane.showOptionDialog(null,
                                "Choose a move",
                                "Time valid moves",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        // Check the player's choice
                        if (n == JOptionPane.CLOSED_OPTION) {
                            // Player closed the dialog without choosing, you can handle this case here
                            JOptionPane.showMessageDialog(null, "Please choose a move.", "Move Canceled", JOptionPane.INFORMATION_MESSAGE);
                            return;  // Return without switching the turn
                        }

                        // Get the chosen move and move the piece
                        int[] chosenMove = validMoves.get(n);
                        board.movePiece(row, col, chosenMove[0], chosenMove[1]);
                    } else {
                        // No valid moves
                        JOptionPane.showMessageDialog(null, "Invalid Move: No valid moves available.", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                // Check if the clicked button has a plus piece
                else if (board.getPieceAt(row, col).equals("Pl1") || board.getPieceAt(row, col).equals("Pl2")) {
                    // Store valid moves in a list
                    List<int[]> validMoves = new ArrayList<>();

                    // Check all possible horizontal and vertical moves
                    for (int[] move : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];

                        // Continue moving in the same direction until reaching the board boundary
                        while (isValidPMove(row, col, newRow, newCol)) {
                            // Add the current position as a valid move
                            validMoves.add(new int[]{newRow, newCol});

                            // Move to the next position
                            newRow += move[0];
                            newCol += move[1];
                        }
                    }

                    // If there are valid moves, allow the player to choose
                    if (!validMoves.isEmpty()) {
                        Object[] options = new Object[validMoves.size()];
                        for (int i = 0; i < validMoves.size(); i++) {
                            options[i] = "Move to " + Arrays.toString(validMoves.get(i));
                        }

                        int n = JOptionPane.showOptionDialog(null,
                                "Choose a move",
                                "Plus valid moves",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        // Check the player's choice
                        if (n == JOptionPane.CLOSED_OPTION) {
                            // Player closed the dialog without choosing, you can handle this case here
                            JOptionPane.showMessageDialog(null, "Please choose a move.", "Move Canceled", JOptionPane.INFORMATION_MESSAGE);
                            return;  // Return without switching the turn
                        }

                        // Get the chosen move and move the piece
                        int[] chosenMove = validMoves.get(n);
                        board.movePiece(row, col, chosenMove[0], chosenMove[1]);
                    } else {
                        // No valid moves
                        JOptionPane.showMessageDialog(null, "Invalid Move: No valid moves available.", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Check if the clicked button has a sun piece
                else if (board.getPieceAt(row, col).equals("S1") || board.getPieceAt(row, col).equals("S2")) {
                    // Store valid moves in a list
                    List<int[]> validMoves = new ArrayList<>();

                    // Check all possible moves in any direction
                    for (int[] move : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}}) {
                        int newRow = row + move[0];
                        int newCol = col + move[1];

                        if (isValidMove(newRow, newCol)) {
                            validMoves.add(new int[]{newRow, newCol});
                        }
                    }
                    // If there are valid moves, allow the player to choose
                    if (!validMoves.isEmpty()) {
                        Object[] options = new Object[validMoves.size()];
                        for (int i = 0; i < validMoves.size(); i++) {
                            options[i] = "Move to " + Arrays.toString(validMoves.get(i));
                        }

                        int n = JOptionPane.showOptionDialog(null,
                                "Choose a move",
                                "Sun valid moves",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        // Check the player's choice
                        if (n == JOptionPane.CLOSED_OPTION) {
                            // Player closed the dialog without choosing, you can handle this case here
                            JOptionPane.showMessageDialog(null, "Please choose a move.", "Move Canceled", JOptionPane.INFORMATION_MESSAGE);
                            return;  // Return without switching the turn
                        }

                        // Get the chosen move and move the piece
                        int[] chosenMove = validMoves.get(n);
                        board.movePiece(row, col, chosenMove[0], chosenMove[1]);
                    } else {
                        // No valid moves
                        JOptionPane.showMessageDialog(null, "Invalid Move: No valid moves available.", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Switch turn after a valid move
                if (currentPlayer.getPlayerNumber() == 1) 
                {
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
                if (yellowTurns == 2 && blueTurns == 2) 
                {
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

        private boolean isValidMove(int newRow, int newCol) 
        {
            // Check if the new position is within the board
            if (newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 7) {
                // Check if the new position has an opponent piece
                return !isValidPlayerMove(board.getPieceAt(newRow, newCol));
            }
            return false;
        }

        private boolean isValidPMove(int oldRow, int oldCol, int newRow, int newCol) 
        {
            // Check if the new position is within the board
            if (newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 7) {

                // Check if the new position has an opponent piece
                if(!isValidPlayerMove(board.getPieceAt(newRow, newCol))) {

                    // Check if all boxes between old and new locations are empty
                    int minRow = Math.min(row, newRow);
                    int maxRow = Math.max(row, newRow);
                    for (int i = minRow + 1; i < maxRow; i++) {
                        if (!board.getPieceAt(i, col).isEmpty()) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        private boolean isValidTimeMove(int oldRow, int oldCol, int newRow, int newCol, int dRow, int dCol) {
            // Check if the new position is within the board
            if (newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 7) {

                // Check if the new position has an opponent piece
                if (!isValidPlayerMove(board.getPieceAt(newRow, newCol))) {

                    // Check if all boxes between old and new locations are empty
                    int dist;
                    for (dist = 1; dist < 6; dist++) {
                        int checkRow = oldRow + dRow * dist;
                        int checkCol = oldCol + dCol * dist;
                        if (checkRow == newRow && checkCol == newCol) {
                            // Reached the destination without finding an occupied box
                            return true;
                        }
                        if (!board.getPieceAt(checkRow, checkCol).isEmpty()) {
                            // Found an occupied box between old and new locations
                            return false;
                        }
                    }

                    // If the loop completes without finding an occupied box, the move is valid
                    return true;
                }
            }
            return false;
        }

        private void changeTimeAndPlusPieces() 
        {
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
    }
}
