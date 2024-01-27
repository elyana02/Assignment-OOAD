import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainGame extends JFrame 
{
    private TalabiaGame talabiaGame;
    private JButton[][] boardButtons;

    public MainGame() 
    {
        this.boardButtons = new JButton[6][7]; // Assuming the size of the boardButtons array is known
        initializeGUI();
    }

    private void initializeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Talabia Game");
        
        Board board = new Board();
        Player currentPlayer = new Player(1);
        this.talabiaGame = new TalabiaGame(board, currentPlayer);
        this.boardButtons = board.getBoardButtons();

        JPanel boardPanel = new JPanel(new GridLayout(6, 7));

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) 
            {
                boardButtons[row][col].setIcon(board.getBoardButtons()[row][col].getIcon());
                boardButtons[row][col].setActionCommand(board.getBoardButtons()[row][col].getActionCommand());
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