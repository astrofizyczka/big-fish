package projekt_java;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class MainWindow {
    private JFrame mainMenuFrame;
    private int zjedzoneRybyCount = 0;
    private JLabel zjedzoneRybyValueLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }

    public MainWindow() {
    	mainMenuFrame = new JFrame("Moje Okno");
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainMenuFrame.setUndecorated(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(screenWidth, screenHeight));

        ImageIcon backgroundImage = new ImageIcon("src/projekt_java/tło_menu.png");
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH)));
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);
        layeredPane.add(backgroundLabel, Integer.valueOf(0));


     // Przyciski w prawym górnym rogu
        ImageIcon soundIcon = new ImageIcon("src/projekt_java/głośnik.png");
        JButton soundButton = new JButton(scaleIcon(soundIcon, 60, 40));
        soundButton.setBounds(screenWidth - 140, 20, 60, 40);
        makeButtonTransparent(soundButton);
        layeredPane.add(soundButton, Integer.valueOf(1));

        ImageIcon exitIcon = new ImageIcon("src/projekt_java/exit.png");
        JButton exitButton = new JButton(scaleIcon(exitIcon, 50, 40));
        exitButton.setBounds(screenWidth - 70, 20, 60, 40);
        makeButtonTransparent(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        layeredPane.add(exitButton, Integer.valueOf(1));

        ImageIcon infoIcon = new ImageIcon("src/projekt_java/info.png");
        JButton infoButton = new JButton(scaleIcon(infoIcon, 60, 40));
        infoButton.setBounds(20, 20, 60, 40);
        makeButtonTransparent(infoButton);
        infoButton.addActionListener(e -> showInfoWindow());
        layeredPane.add(infoButton, Integer.valueOf(1));

        String[] imagePaths = {
                "src/projekt_java/gruba_ryba.png",
                "src/projekt_java/szybka_rybka.png",
                "src/projekt_java/wybierz_swoją_rybkę.png",
                "src/projekt_java/najszybsze_ryby_w_ławicy.png"
        };

        int buttonWidth = 400;
        int buttonHeight = 200;
        int spacing = 30;
        int totalWidth = (2 * buttonWidth) + spacing;

        int startX = (screenWidth - totalWidth) / 2;
        int startY = screenHeight / 2; // Przyciski w dolnej części ekranu

        for (int i = 0; i < 4; i++) {
            ImageIcon icon = new ImageIcon(imagePaths[i]);
            JButton button = new JButton(scaleIcon(icon, buttonWidth, buttonHeight));

            int x = startX + (i % 2) * (buttonWidth + spacing);
            int y = startY + (i / 2) * (buttonHeight + spacing);
            button.setBounds(x, y, buttonWidth, buttonHeight);

            makeButtonTransparent(button);
            int finalI = i + 1;
            button.addActionListener(e -> openNewWindow(finalI));
            layeredPane.add(button, Integer.valueOf(1));
        }
        mainMenuFrame.setContentPane(layeredPane);
        mainMenuFrame.pack();
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    private void makeButtonTransparent(JButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }

    private Icon scaleIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void openNewWindow(int windowNumber) {
        JFrame newFrame = new JFrame("Okno " + windowNumber);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        newFrame.setUndecorated(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(screenWidth, screenHeight));

        ImageIcon backgroundImage = new ImageIcon("src/projekt_java/tło_okienek.png");
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH)));
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        if (windowNumber == 1) {
            JPanel topPanel = createTopPanelOkno1(newFrame);
            layeredPane.add(topPanel, Integer.valueOf(1));
        } else if (windowNumber == 2) {
            JPanel topPanel = createTopPanelOkno2(newFrame);
            layeredPane.add(topPanel, Integer.valueOf(1));
        } else if (windowNumber == 3) {
            JPanel topPanel = createTopPanelOkno3(newFrame);
            layeredPane.add(topPanel, Integer.valueOf(1));

            // Panel dla przycisków rybek
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setOpaque(false);

            JPanel fishButtonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
            fishButtonPanel.setOpaque(false);

            String[] imagePaths = {
                    "src/projekt_java/Sylwia1.png",
                    "src/projekt_java/Sylwia2.png",
                    "src/projekt_java/Sylwia3.png",
                    "src/projekt_java/Sylwia4.png"
            };

            String[] messages = {
                    "wybrano żółtą rybkę Sylwię",
                    "wybrano różową rybkę Młodego",
                    "wybrano purpurową rybkę Finkę",
                    "wybrano fioletową rybkę Leona"
            };

            // Paths to the images for the names
            String[] nameImagePaths = {
                    "src/projekt_java/Sylwia.png",
                    "src/projekt_java/Młody.png",
                    "src/projekt_java/Finka.png",
                    "src/projekt_java/Leon.png"
            };

            for (int i = 0; i < imagePaths.length; i++) {
                ImageIcon buttonIcon = new ImageIcon(imagePaths[i]);
                JButton rybkaButton = new JButton(scaleIcon(buttonIcon, 200, 150));
                makeButtonTransparent(rybkaButton);

                // Create a label with the image for each fish name
                ImageIcon nameIcon = new ImageIcon(nameImagePaths[i]);
                JLabel nameLabel = new JLabel(new ImageIcon(nameIcon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH)), SwingConstants.CENTER);

                int finalI = i;
                rybkaButton.addActionListener(e ->
                        JOptionPane.showMessageDialog(newFrame, messages[finalI])
                );

                // Add button and name image label to a panel
                JPanel buttonWithLabelPanel = new JPanel();
                buttonWithLabelPanel.setLayout(new BorderLayout());
                buttonWithLabelPanel.setOpaque(false);
                buttonWithLabelPanel.add(rybkaButton, BorderLayout.CENTER);
                buttonWithLabelPanel.add(nameLabel, BorderLayout.SOUTH);

                fishButtonPanel.add(buttonWithLabelPanel);
            }

            centerPanel.add(fishButtonPanel, BorderLayout.CENTER);
            centerPanel.setBounds(screenWidth / 2 - 450, screenHeight / 2 - 300, 900, 600);
            layeredPane.add(centerPanel, Integer.valueOf(1));
        } else if (windowNumber == 4) {
            JPanel topPanel = createTopPanelOkno4(newFrame);
            layeredPane.add(topPanel, Integer.valueOf(1));
        }

        newFrame.setContentPane(layeredPane);
        newFrame.pack();
        newFrame.setLocationRelativeTo(null);
        newFrame.setVisible(true);
        mainMenuFrame.setVisible(false);
    }
    
    
    private JPanel createTopPanelOkno1(JFrame frameToClose) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int topPanelHeight = screenHeight / 8 + 30;

        // Główny panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBounds(0, 0, screenWidth, screenHeight);

        // ---------------- TOP PANEL ----------------
        JPanel topPanel = new JPanel(null);
        topPanel.setBackground(new Color(203, 108, 230));
        topPanel.setBounds(0, 0, screenWidth, topPanelHeight);

        // Panel z ikonkami rybek
        JPanel fishPanel = new JPanel(null);
        fishPanel.setBackground(new Color(203, 108, 230));
        fishPanel.setBounds(0, 0, screenWidth, 70);

        String[] fishIcons = {
            "src/projekt_java/Mała_ryba.png",
            "src/projekt_java/Średnia_ryba.png",
            "src/projekt_java/Duża_ryba.png"
        };

        int iconSize = 60;
        int spacing = 200;
        int totalIconsWidth = fishIcons.length * iconSize + (fishIcons.length - 1) * spacing;
        int iconStartX = (screenWidth - totalIconsWidth) / 2;
        int iconY = 20;

        for (int i = 0; i < fishIcons.length; i++) {
            ImageIcon fishIcon = new ImageIcon(fishIcons[i]);
            JButton fishButton = new JButton(scaleIcon(fishIcon, iconSize, iconSize));
            makeButtonTransparent(fishButton);
            fishButton.setBounds(iconStartX + i * (iconSize + spacing), iconY, iconSize, iconSize);
            fishPanel.add(fishButton);
        }
        topPanel.add(fishPanel);

        // Przycisk "Menu"
        JButton menuButton = new JButton(scaleIcon(new ImageIcon("src/projekt_java/menu.png"), 120, 50));
        makeButtonTransparent(menuButton);
        menuButton.setBounds(20, topPanelHeight - 60, 120, 50);
        menuButton.addActionListener(e -> {
            frameToClose.dispose();
            mainMenuFrame.setVisible(true);
        });
        topPanel.add(menuButton);

        // Wzrost rybki
        JLabel growthLabel = new JLabel(scaleIcon(new ImageIcon("src/projekt_java/wzrost_rybki.png"), 150, 50));
        growthLabel.setBounds(screenWidth / 3 - 200, topPanelHeight - 60, 150, 50);
        topPanel.add(growthLabel);

        // Suwak wzrostu
        JSlider growthSlider = new JSlider(0, 100, 50);
        growthSlider.setBounds(screenWidth / 3, topPanelHeight - 60, screenWidth / 3, 40);
        growthSlider.setBackground(Color.BLACK);
        growthSlider.setUI(new BasicSliderUI(growthSlider) {
            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.RED);
                int[] xPoints = {thumbRect.x + thumbRect.width / 2, thumbRect.x, thumbRect.x + thumbRect.width};
                int[] yPoints = {thumbRect.y + thumbRect.height, thumbRect.y, thumbRect.y};
                g2.fillPolygon(xPoints, yPoints, 3);
                g2.dispose();
            }

            @Override
            public void paintTrack(Graphics g) {
                g.setColor(Color.YELLOW);
                Rectangle trackBounds = trackRect;
                g.fillRect(trackBounds.x, trackBounds.y + trackBounds.height / 3,
                        trackBounds.width, trackBounds.height / 3);
            }
        });
        topPanel.add(growthSlider);

        // Punkty
        int pointsX = screenWidth - 300;
        JLabel pointsLabel = new JLabel(scaleIcon(new ImageIcon("src/projekt_java/punkty.png"), 120, 50));
        pointsLabel.setBounds(pointsX, topPanelHeight - 60, 120, 50);
        topPanel.add(pointsLabel);

        JPanel pointsPanel = new JPanel(new BorderLayout());
        pointsPanel.setBackground(Color.BLACK);
        pointsPanel.setBounds(pointsX + 130, topPanelHeight - 60, 80, 50);
        zjedzoneRybyValueLabel = new JLabel(String.valueOf(zjedzoneRybyCount), SwingConstants.CENTER);
        zjedzoneRybyValueLabel.setForeground(Color.YELLOW);
        zjedzoneRybyValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pointsPanel.add(zjedzoneRybyValueLabel, BorderLayout.CENTER);
        topPanel.add(pointsPanel);

        JButton exitButton = new JButton(scaleIcon(new ImageIcon("src/projekt_java/exit.png"), 50, 40));
        makeButtonTransparent(exitButton);
        exitButton.setBounds(pointsPanel.getX() + pointsPanel.getWidth() + 10, pointsPanel.getY(), 80, 50);
        exitButton.addActionListener(e -> System.exit(0));
        topPanel.add(exitButton);

        // ---------------- GAME PANEL ----------------
        int gamePanelHeight = screenHeight - topPanelHeight;
        BackgroundPanel gamePanel = new BackgroundPanel("src/projekt_java/tło_okienek.png");
        gamePanel.setBounds(0, topPanelHeight, screenWidth, gamePanelHeight);

     // Dodanie gracza
        MovableImage movableFish = new MovableImage("src/projekt_java/Sylwia1.png", gamePanel);
        movableFish.setBounds(100, 100, 120, 80);
        movableFish.setFocusable(true);
        gamePanel.add(movableFish);

        // Fokus (upewnij się, że to wywoływane po dodaniu do panelu)
        SwingUtilities.invokeLater(movableFish::requestFocusInWindow);

        // Dodanie do mainPanel
        mainPanel.add(topPanel);
        mainPanel.add(gamePanel);
        
     // Ustawienie rybki gracza na środku po renderze
        SwingUtilities.invokeLater(() -> {
            int panelWidth = gamePanel.getWidth();
            int panelHeight = gamePanel.getHeight();

            int fishWidth = movableFish.getWidth();
            int fishHeight = movableFish.getHeight();

            int startX = (panelWidth - fishWidth) / 2;
            int startY = (panelHeight - fishHeight) / 2;

            movableFish.setLocation(startX, startY);
            movableFish.requestFocusInWindow();
        });

        return mainPanel;
    }






    private JPanel createTopPanelOkno2(JFrame frameToClose) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;

        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(new Color(203, 108, 230));
        topPanel.setBounds(0, 0, screenWidth, 80); // Rozciągnięcie na całą szerokość ekranu

        // Przycisk "Menu"
        ImageIcon menuIcon = new ImageIcon("src/projekt_java/menu.png");
        JButton menuButton = new JButton(scaleIcon(menuIcon, 100, 40));
        makeButtonTransparent(menuButton);
        menuButton.setBounds(20, 20, 100, 40);
        menuButton.addActionListener(e -> {
            frameToClose.dispose();
            mainMenuFrame.setVisible(true);
        });
        topPanel.add(menuButton);

        // Przycisk "Exit" (po prawej stronie ekranu)
        ImageIcon exitIcon = new ImageIcon("src/projekt_java/exit.png");
        JButton exitButton = new JButton(scaleIcon(exitIcon, 50, 40));
        makeButtonTransparent(exitButton);
        exitButton.setBounds(screenWidth - 100, 20, 80, 40);
        exitButton.addActionListener(e -> System.exit(0)); // Wyjście z gry
        topPanel.add(exitButton);

        // Licznik "Zjedzone ryby" (po lewej stronie przycisku Exit)
        JPanel licznikPanel = new JPanel();
        licznikPanel.setLayout(new BorderLayout());
        licznikPanel.setBackground(Color.BLACK);
        licznikPanel.setBounds(screenWidth - 180, 20, 60, 40);

        zjedzoneRybyValueLabel = new JLabel(String.valueOf(zjedzoneRybyCount), SwingConstants.CENTER);
        zjedzoneRybyValueLabel.setForeground(Color.YELLOW);
        zjedzoneRybyValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        licznikPanel.add(zjedzoneRybyValueLabel, BorderLayout.CENTER);
        topPanel.add(licznikPanel);

        // Grafika "Zjedzone ryby" (po lewej stronie licznika)
        ImageIcon zjedzoneRybyIcon = new ImageIcon("src/projekt_java/zjedzone_ryby.png");
        JLabel zjedzoneRybyLabel = new JLabel(scaleIcon(zjedzoneRybyIcon, 200, 40));
        zjedzoneRybyLabel.setBounds(screenWidth - 400, 20, 200, 40);
        topPanel.add(zjedzoneRybyLabel);

        return topPanel;
    }
    
    private JPanel createTopPanelOkno3(JFrame frameToClose) {
        // Uzyskanie rozmiaru ekranu
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;

        // Panel górny
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null); // Użycie układu null, aby ręcznie ustawić pozycje
        topPanel.setBackground(new Color(203, 108, 230));
        topPanel.setBounds(0, 0, screenWidth, 80); // Rozciągnięcie na całą szerokość ekranu

        // Przycisk "Menu" po lewej stronie
        ImageIcon menuIcon = new ImageIcon("src/projekt_java/menu.png");
        JButton menuButton = new JButton(scaleIcon(menuIcon, 100, 40));
        makeButtonTransparent(menuButton);
        menuButton.setBounds(20, 20, 100, 40); // Ustawienie przycisku w lewym górnym rogu
        menuButton.addActionListener(e -> {
            frameToClose.dispose();
            mainMenuFrame.setVisible(true);
        });
        topPanel.add(menuButton);

        // Przycisk "Exit" po prawej stronie
        ImageIcon exitIcon = new ImageIcon("src/projekt_java/exit.png");
        JButton exitButton = new JButton(scaleIcon(exitIcon, 50, 40));
        makeButtonTransparent(exitButton);
        exitButton.setBounds(screenWidth - 100, 20, 80, 40); // Ustawienie przycisku w prawym górnym rogu
        exitButton.addActionListener(e -> System.exit(0)); // Wyjście z gry
        topPanel.add(exitButton);

        return topPanel;
    }

    private JPanel createTopPanelOkno4(JFrame frameToClose) {
        // Uzyskanie rozmiaru ekranu
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;

        // Panel górny
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null); // Użycie układu null, aby ręcznie ustawić pozycje
        topPanel.setBackground(new Color(203, 108, 230));
        topPanel.setBounds(0, 0, screenWidth, 80); // Rozciągnięcie na całą szerokość ekranu

        // Przycisk "Menu" po lewej stronie
        ImageIcon menuIcon = new ImageIcon("src/projekt_java/menu.png");
        JButton menuButton = new JButton(scaleIcon(menuIcon, 100, 40));
        makeButtonTransparent(menuButton);
        menuButton.setBounds(20, 20, 100, 40); // Ustawienie przycisku w lewym górnym rogu
        menuButton.addActionListener(e -> {
            frameToClose.dispose();
            mainMenuFrame.setVisible(true);
        });
        topPanel.add(menuButton);

        // Przycisk "Exit" po prawej stronie
        ImageIcon exitIcon = new ImageIcon("src/projekt_java/exit.png");
        JButton exitButton = new JButton(scaleIcon(exitIcon, 50, 40));
        makeButtonTransparent(exitButton);
        exitButton.setBounds(screenWidth - 100, 20, 80, 40); // Ustawienie przycisku w prawym górnym rogu
        exitButton.addActionListener(e -> System.exit(0)); // Wyjście z gry
        topPanel.add(exitButton);

        return topPanel;
    }

    private void showInfoWindow() {
        JFrame infoFrame = new JFrame("Informacja o grze");
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setSize(400, 300);
        infoFrame.setResizable(false);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());

        JLabel infoLabel = new JLabel("<html><h2>Gra w ryby!</h2><p>Zasady są proste. Na dobry początek wybierz tryb w któym chcesz grać! Możesz zostać grubą rybą albo szybką rybką!</h2><p>W trybie gruba ryba musisz zjadać ryby mniejsze od siebie i uciekać przed rybami większymi od ciebie. Uważaj, żeby cię nie zjadły! Wtedy gra się kończy:(</h2><p>W trybie szybka rybka, liczy się twój refleks, szybkość, czas i ilość zjedzonych w tym czasie małych rybek!</h2><p>Możesz też wybrać swoją rybkę, a twoje wyniki regularnie zapisują sie w tabeli zwycięzców!</p></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Powodzenia;)");
        closeButton.addActionListener(e -> infoFrame.dispose());
        infoPanel.add(closeButton, BorderLayout.SOUTH);

        infoFrame.setContentPane(infoPanel);
        infoFrame.setLocationRelativeTo(null);
        infoFrame.setVisible(true);
    }
}

