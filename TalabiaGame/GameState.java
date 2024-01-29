/**
 * The GameState class represents the current state of the Talabia Chess Game.
 * It encapsulates information about the game board, directions, piece positions,
 * and the current player's turn. This class is designed for serialization and
 * deserialization purposes, allowing the game state to be saved and loaded.
 *
 * Key Features:
 * - Holds arrays representing the game board buttons, directions, and piece positions.
 * - Stores the player number indicating whose turn it is.
 * - Implements Serializable to support Java Object Serialization.
 *
 * Relationships:
 * - Interacts closely with the GameFileManager class for saving and loading game state.
 * - Used by the GameView class to update and display the game state.
 *
 * Usage Guidelines:
 * - Use this class to represent and transport the current state of the game.
 * - Serializable for saving and loading game progress using Object Serialization.
 * - The class provides methods to retrieve specific information about the game state.
 */

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
