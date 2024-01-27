import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class Board
{
    private JButton[][] boardButtons;
    private int[][] directions;

    public Board()
    {
        initializeBoard();
    }

    public void initializeBoard() 
    {
        boardButtons = new JButton[6][7];
        directions = new int[6][7];

        for (int row = 0; row < 6; row++) 
        {
            for (int col = 0; col < 7; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(60, 60));
                boardButtons[row][col] = button;
                directions[row][col] = 0;

            }
        }

        // Fill second row with Player 1 point pieces
        for (int col = 0; col < 7; col++) {
            setPieceIcon("P1", 1, col);
            directions[1][col] = 1; // Player 1 pieces move down
        }

        // Fill fifth row with Player 2 point pieces
        for (int col = 0; col < 7; col++) {
            setPieceIcon("P2", 4, col);
            directions[4][col] = -1; // Player 2 pieces move up
        }

        // Add plus pieces in the four corners
        setPieceIcon("Pl1", 0, 0); // Player 1 plus pieces
        setPieceIcon("Pl1", 0, 6); // Player 1 plus pieces
        setPieceIcon("Pl2", 5, 0); // Player 2 plus pieces
        setPieceIcon("Pl2", 5, 6); // Player 2 plus pieces

        // Add time pieces on both sides of kings
        setPieceIcon("T1", 0, 2); // Player 1 time pieces
        setPieceIcon("T1", 0, 4); // Player 1 time pieces
        setPieceIcon("T2", 5, 2); // Player 2 time pieces
        setPieceIcon("T2", 5, 4); // Player 2 time pieces

        // Add hourglass pieces between rooks and bishops
        setPieceIcon("H1", 0, 1); // Player 1 hourglass pieces
        setPieceIcon("H1", 0, 5); // Player 1 hourglass pieces
        setPieceIcon("H2", 5, 1); // Player 2 hourglass pieces
        setPieceIcon("H2", 5, 5); // Player 2 hourglass pieces

        // Add sun pieces at the specified positions
        setPieceIcon("S1", 0, 3); // Player 1 sun piece
        setPieceIcon("S2", 5, 3); // Player 2 sun piece
    }
    
    public JButton[][] getBoardButtons() 
    {
        return boardButtons;
    }

    // Update the button icon
    public void setPieceIcon(String piece, int row, int col) 
    {
        ImageIcon icon;
        
        try {
            switch (piece) {
                case "P1":
                    icon = new ImageIcon(getClass().getResource("p1.png"));
                    break;
                case "P2":
                    icon = new ImageIcon(getClass().getResource("p2.png"));
                    break;
                case "H1":
                    icon = new ImageIcon(getClass().getResource("h1.png"));
                    break;
                case "H2":
                    icon = new ImageIcon(getClass().getResource("h2.png"));
                    break;
                case "T1":
                    icon = new ImageIcon(getClass().getResource("t1.png"));
                    break;
                case "T2":
                    icon = new ImageIcon(getClass().getResource("t2.png"));
                    break;
                case "Pl1":
                    icon = new ImageIcon(getClass().getResource("plus1.png"));
                    break;
                case "Pl2":
                    icon = new ImageIcon(getClass().getResource("plus2.png"));
                    break;
                case "S1":
                    icon = new ImageIcon(getClass().getResource("s1.png"));
                    break;
                case "S2":
                    icon = new ImageIcon(getClass().getResource("s2.png"));
                    break;
                default:
                    icon = null;
                    break;

            }
            boardButtons[row][col].setIcon(icon);
            boardButtons[row][col].setActionCommand(piece); // Set piece information as action command

        } catch (Exception e) 
        {
            System.err.println("Error loading image for piece " + piece);
            e.printStackTrace();
        
            // Print additional information about the resource path
            System.out.println("Resource Path: " + getClass().getResource("icon/" + piece.toLowerCase() + ".png"));
        }
    }
    
    public int getDirection(int row, int col) {
        return directions[row][col];
    }

    public void reverseDirection(int row, int col) {
        directions[row][col] *= -1;
    }

    public String getPieceAt(int row, int col) {
        return boardButtons[row][col].getActionCommand();
    }

    public void movePiece(int oldRow, int oldCol, int newRow, int newCol) {
        // Check if the game is over
        if (boardButtons[newRow][newCol].getActionCommand().equals("S1")
        || boardButtons[newRow][newCol].getActionCommand().equals("S2")) {
            JOptionPane.showMessageDialog(null, "Game Over! The sun has been captured.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        // Check if the destination square contains an opposing player's piece
        if (!boardButtons[newRow][newCol].getActionCommand().isEmpty() &&
        boardButtons[newRow][newCol].getActionCommand().charAt(1) != boardButtons[oldRow][oldCol]
        .getActionCommand().charAt(1)) {

            // Capture the piece
            boardButtons[newRow][newCol].setIcon(boardButtons[oldRow][oldCol].getIcon());
            boardButtons[oldRow][oldCol].setIcon(null);
            boardButtons[newRow][newCol].setActionCommand(boardButtons[oldRow][oldCol].getActionCommand());
            boardButtons[oldRow][oldCol].setActionCommand("");
            JOptionPane.showMessageDialog(null, "Piece captured!", "Piece is captured!", JOptionPane.INFORMATION_MESSAGE);

        } else {
            // Move the piece from the old position to the new position
            boardButtons[newRow][newCol].setIcon(boardButtons[oldRow][oldCol].getIcon());
            boardButtons[oldRow][oldCol].setIcon(null);
            boardButtons[newRow][newCol].setActionCommand(boardButtons[oldRow][oldCol].getActionCommand());
            boardButtons[oldRow][oldCol].setActionCommand("");

            // To update the direction after a successful move
            directions[newRow][newCol] = directions[newRow][newCol] * -1;
        }
    }
}
