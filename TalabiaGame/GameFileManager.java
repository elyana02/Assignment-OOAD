/**
 * The GameFileManager class handles the saving and loading of game progress
 * in the Talabia Chess Game. It uses Java Object Serialization to store the
 * game state in a file.
 *
 * Key Methods:
 * - saveProgress(boardButtons, directions, currentPlayer): Saves the current game progress.
 * - extractBoardInfo(boardButtons, directions, currentPlayer): Extracts game information
 *   for serialization from the current state of the board.
 * - exitGame(): Prompts the user to confirm exiting the game and terminates the application if approved.
 * - loadGame(): Opens a file dialog to allow the user to select a saved game file for loading.
 *
 * Relationships:
 * - The GameFileManager class is closely associated with the GameState class for
 *   serializing and deserializing game state information.
 * - Utilized by the GameView class to provide save, load, and exit functionality.
 *
 * Usage Guidelines:
 * - Use saveProgress to save the current game state during gameplay.
 * - Utilize loadGame to load a previously saved game state.
 * - Implement exitGame to prompt the user before exiting the game.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class GameFileManager {
    public static void saveProgress(JButton[][] boardButtons, int[][] directions, Player currentPlayer) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gamestate.txt"))) {
            GameState gameState = extractBoardInfo(boardButtons, directions, currentPlayer);
            oos.writeObject(gameState);
            JOptionPane.showMessageDialog(null, "Game progress saved!", "Save Progress", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static GameState extractBoardInfo(JButton[][] boardButtons, int[][] directions, Player currentPlayer) {
        String[][] piecePositions = new String[boardButtons.length][boardButtons[0].length];

        for (int row = 0; row < boardButtons.length; row++) {
            for (int col = 0; col < boardButtons[row].length; col++) {
                piecePositions[row][col] = boardButtons[row][col].getActionCommand();
            }
        }

        int playerNumber = currentPlayer.getPlayerNumber();
        return new GameState(boardButtons, directions, piecePositions, playerNumber);
    }

    public static void exitGame() {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static GameState loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            // The user selected a file
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                return (GameState) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
