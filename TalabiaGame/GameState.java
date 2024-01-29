import java.io.Serializable;

import javax.swing.JButton;

public class GameState implements Serializable {
    private JButton[][] boardButtons;
    private int[][] directions;
    private String[][] pieceInfo;  // Stores information about pieces at each position
    private int playerTurn;

    public GameState(JButton[][] boardButtons, int[][] directions, String[][] pieceInfo, int playerTurn) {
        this.boardButtons = boardButtons;
        this.directions = directions;
        this.pieceInfo = pieceInfo;
        this.playerTurn = playerTurn;
    }

    public JButton[][] getBoardButtons() {
        return boardButtons;
    }

    public int[][] getDirections() {
        return directions;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public GameState(String[][] pieceInfo) {
        this.pieceInfo = pieceInfo;
    }

    public String getPieceAt(int row, int col) {
        // Check if the provided row and column are within the bounds of the array
        if (row >= 0 && row < pieceInfo.length && col >= 0 && col < pieceInfo[row].length) {
            return pieceInfo[row][col];
        } else {
            // Return an empty string or handle the out of bounds case as needed
            return "";
        }
    }

    public String[][] getPieceInfo() {
        return pieceInfo;
    }
}
