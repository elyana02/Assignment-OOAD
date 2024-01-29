/**
 * The HomePage class represents the initial screen of the Talabia Chess Game.
 * It provides options for starting a new game or loading a previous game,
 * acting as a gateway to the main game interface (GameView).
 *
 * Key Features:
 * - Displays a welcome message and buttons for starting a new game or loading a previous game.
 * - Serves as the entry point to the Talabia Chess Game, connecting the user to the main game view.
 * - Utilizes the GameFileManager class for loading saved games.
 *
 * Relationships:
 * - Collaborates with the GameView and GameFileManager classes.
 * - Acts as a bridge between the user's initial choices and the main game interface.
 *
 * Usage Guidelines:
 * - Instantiate this class to present the Talabia Chess Game's home screen.
 * - Allows users to choose between starting a new game or loading a previously saved game.
 * - Directs users to the main game interface (GameView) based on their selections.
 */

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class HomePage extends JFrame {

    private GameView gameView;
    private Board board;

    public HomePage(GameView gameView) {
        this.gameView = gameView;
        initialize();
    }

    public HomePage() {
        initialize();
        this.board = new Board();  // Initialize the board object
    }

    public void setTalabiaGame(GameView gameView) {
        this.gameView = gameView;
    }

    public void setMainGame(GameView gameView) {
        this.gameView = gameView;
    }

    private void initialize() {
        JPanel homePanel = new JPanel(new BorderLayout());

        // Welcome Message
        JLabel welcomeLabel = new JLabel("Welcome to Talabia Chess!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        homePanel.add(welcomeLabel, BorderLayout.NORTH);

        // Options Panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 1, 0, 20));

        // Start New Game Button
        JButton newGameButton = new JButton("Start New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeHomePage();
                startNewGame();
            }
        });

        // Load Previous Game Button
        JButton loadGameButton = new JButton("Load Previous Game");
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (board != null) {
                    GameState loadedGameState = GameFileManager.loadGame();
                    if (loadedGameState != null) {
                        closeHomePage();
                        GameView newGame = new GameView();
                        // Make sure board.updateGameFromState handles null checks
                        board.updateGameFromState(loadedGameState);
                        JOptionPane.showMessageDialog(HomePage.this, "Game loaded successfully!", "Load Game", JOptionPane.INFORMATION_MESSAGE);
                        // Refresh the board after loading the game
                        board.refreshBoard();
                    } else {
                        // Handle case where loading failed
                        JOptionPane.showMessageDialog(HomePage.this, "Error: Unable to load the game.", "Load Game", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Handle the case where board is not initialized in the HomePage class
                    JOptionPane.showMessageDialog(HomePage.this, "Error: Board not initialized.", "Load Game", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        optionsPanel.add(newGameButton);
        optionsPanel.add(loadGameButton);

        homePanel.add(optionsPanel, BorderLayout.CENTER);

        // Display the homepage
        add(homePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void closeHomePage() {
        dispose();  // Close the homepage
    }

    private void startNewGame() {
        SwingUtilities.invokeLater(() -> {
            GameView newGame = new GameView();
            setTalabiaGame(newGame);
            newGame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomePage();
        });
    }
}
