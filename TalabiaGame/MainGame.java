import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGame extends JFrame {
    private TalabiaGame talabiaGame;
    private JButton[][] boardButtons;
    private Board board;
    private Player currentPlayer;

    public MainGame() {
        this.boardButtons = new JButton[6][7]; // Assuming the size of the boardButtons array is known
        initializeGUI();
        setupMenuBar();
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Options Menu
        JMenu optionMenu = new JMenu("Option");
        optionMenu.setMnemonic(KeyEvent.VK_O);

        // Save Option
        JMenuItem saveMenuItem = new JMenuItem("Save Progress");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameFileManager.saveProgress(Board.getBoardButtons(), Board.getDirections(), currentPlayer);
            }
        });

        // Load Option
        JMenuItem loadMenuItem = new JMenuItem("Load Game");
        loadMenuItem.setMnemonic(KeyEvent.VK_L);
        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameState loadedGameState = GameFileManager.loadGame();
                if (loadedGameState != null && board != null) {
                    board.updateGameFromState(loadedGameState);
                    JOptionPane.showMessageDialog(null, "Game loaded successfully!", "Load Game", JOptionPane.INFORMATION_MESSAGE);
                    // Refresh the board after loading the game
                    board.refreshBoard();
                } else {
                    // Handle the case where loading failed
                    JOptionPane.showMessageDialog(null, "Error: Unable to load the game.", "Load Game", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameFileManager.exitGame();
            }
        });

        optionMenu.add(saveMenuItem);
        optionMenu.add(loadMenuItem);
        optionMenu.add(exitMenuItem);

        menuBar.add(optionMenu);

        setJMenuBar(menuBar);
    }

    private void initializeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Talabia Game");

        board = new Board();  // Use the instance variable instead of creating a new local variable
        currentPlayer = new Player(1);
        this.talabiaGame = new TalabiaGame(board, currentPlayer);
        this.boardButtons = Board.getBoardButtons();

        JPanel boardPanel = new JPanel(new GridLayout(6, 7));

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                boardButtons[row][col].setIcon(Board.getBoardButtons()[row][col].getIcon());
                boardButtons[row][col].setActionCommand(Board.getBoardButtons()[row][col].getActionCommand());
                boardPanel.add(boardButtons[row][col]);
                boardButtons[row][col].addActionListener(talabiaGame.new ChessButtonListener(row, col));
            }
        }

        add(boardPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGame();
        });
    }
}
