import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                // Assuming board is a class member in the HomePage class
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
                        // Handle the case where loading failed
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
            new HomePage(); // Creating an instance without assigning to a variable
        });
    }    
}
