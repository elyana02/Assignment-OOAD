import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {

    public HomePage() {
        initialize();
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
                new TalabiaGame();  // Create an instance of the game class
            }
        });

        // Load Previous Game Button
        JButton loadGameButton = new JButton("Load Previous Game");
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // letak logic kat sini
                closeHomePage();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage());
    }
}

