package projekt_java;

import javax.swing.*;
import java.awt.*;

public class MainWindow2 {
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }

    public MainWindow2() {
        mainFrame = new JFrame("Gra Rybki");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tworzenie ekranów
        JPanel menuPanel = createMenuPanel();
        JPanel gamePanel = createGamePanel();

        // Dodanie paneli do CardLayout
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        mainFrame.setContentPane(mainPanel);
        mainFrame.setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton startButton = new JButton("Start Gry");
        startButton.addActionListener(e -> cardLayout.show(mainPanel, "Game"));

        panel.add(startButton, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton backButton = new JButton("Powrót do Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        panel.add(backButton, BorderLayout.CENTER);
        return panel;
    }
}
