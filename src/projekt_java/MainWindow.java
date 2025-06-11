package projekt_java;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class MainWindow {
    private int zjedzoneRybyCount = 0;
    private JLabel zjedzoneRybyValueLabel;
    
    private JFrame mainFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private String playerImagePath;
    private PlayerFish movableFish;

    public void initMainFrame() {
        mainFrame = new JFrame("Gra Rybki");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setUndecorated(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        mainFrame.setContentPane(cardPanel);

        playerImagePath = "src/projekt_java/Sylwia1.png";
        // Dodajemy "ekrany"
        cardPanel.add(chooseYourFishMode(), "okno3");
        cardPanel.add(createMainMenuPanel(), "menu");
        cardPanel.add(bigFishMode(), "okno1");

        mainFrame.setVisible(true);
        cardLayout.show(cardPanel, "menu");
        movableFish.triggerMainMenu();
    }

    private JPanel createMainMenuPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(screenWidth, screenHeight));

        // Tło
        ImageIcon backgroundImage = new ImageIcon("src/projekt_java/tło_menu.png");
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH)));
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        // Przycisk exit
        ImageIcon exitIcon = new ImageIcon("src/projekt_java/exit.png");
        JButton exitButton = new JButton(scaleIcon(exitIcon, 50, 40));
        exitButton.setBounds(screenWidth - 70, 20, 60, 40);
        makeButtonTransparent(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        layeredPane.add(exitButton, Integer.valueOf(1));

        // Przycisk info
        ImageIcon infoIcon = new ImageIcon("src/projekt_java/info.png");
        JButton infoButton = new JButton(scaleIcon(infoIcon, 60, 40));
        infoButton.setBounds(20, 20, 60, 40);
        makeButtonTransparent(infoButton);
        infoButton.addActionListener(e -> showInfoWindow());
        layeredPane.add(infoButton, Integer.valueOf(1));

        // Przycisk 1 - Gruba Ryba
        JButton button1 = new JButton(scaleIcon(new ImageIcon("src/projekt_java/gruba_ryba.png"), 400, 200));
        button1.setBounds(screenWidth / 2 - 430, screenHeight * 3 / 5, 400, 200);
        makeButtonTransparent(button1);
        button1.addActionListener(e -> {
            if (movableFish != null) {
            	movableFish.triggerMainMenu();
            	movableFish.restartGame();
            	movableFish.updateFishImage(playerImagePath); // update to selected image
            }
            cardLayout.show(cardPanel, "okno1");
        });
        layeredPane.add(button1, Integer.valueOf(1));

        // Przycisk 2 - Wybierz Rybkę
        JButton button2 = new JButton(scaleIcon(new ImageIcon("src/projekt_java/wybierz_swoją_rybkę.png"), 400, 200));
        button2.setBounds(screenWidth / 2 + 30, screenHeight * 3 / 5, 400, 200);
        makeButtonTransparent(button2);
        button2.addActionListener(e -> cardLayout.show(cardPanel, "okno3"));
        layeredPane.add(button2, Integer.valueOf(1));

        JPanel mainMenuPanel = new JPanel(new BorderLayout());
        mainMenuPanel.add(layeredPane, BorderLayout.CENTER);

        return mainMenuPanel;
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


    //tryb gry Gruba ryba
    private JPanel bigFishMode() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int topPanelHeight = screenHeight / 8 + 30; //oblicza wysokość górnego panelu (1/8 wysokości ekranu + 30 pikseli)

        // Główny panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBounds(0, 0, screenWidth, screenHeight); //użycie null oznacza ręczne ustawianie pozycji komponentów

        //panel górny
        JPanel topPanel = new JPanel(null);
        topPanel.setBackground(new Color(203, 108, 230));
        topPanel.setBounds(0, 0, screenWidth, topPanelHeight);
        
        //górny panel z grafikami rybek
        JPanel fishPanel = new JPanel(null);
        fishPanel.setBackground(new Color(203, 108, 230));
        fishPanel.setBounds(0, 0, screenWidth, 80);

        String[] fishIcons = {
            "src/projekt_java/Mała_ryba.png",
            "src/projekt_java/Średnia_ryba.png",
            "src/projekt_java/Duża_ryba.png"
        };

        int iconSize = 60;
        int spacing = 200;
        int totalIconsWidth = fishIcons.length * iconSize + (fishIcons.length - 1) * spacing; //oblicza rozmiary i rozmieszczenie ikon ryb na środku ekranu
        int iconStartX = (screenWidth - totalIconsWidth) / 2;
        int iconY = 20;

        for (int i = 0; i < fishIcons.length; i++) { //
            ImageIcon fishIcon = new ImageIcon(fishIcons[i]);
            JButton fishButton = new JButton(scaleIcon(fishIcon, iconSize, iconSize));
            makeButtonTransparent(fishButton);
            fishButton.setBounds(iconStartX + i * (iconSize + spacing), iconY, iconSize, iconSize);
            fishPanel.add(fishButton);
        }
        topPanel.add(fishPanel); //do górnego panelu zostaje dodany panel z grafikami ryb

        // Wzrost rybki
        JLabel growthLabel = new JLabel(scaleIcon(new ImageIcon("src/projekt_java/wzrost_rybki.png"), 150, 50));
        growthLabel.setBounds(screenWidth / 3 - 200, topPanelHeight - 60, 150, 50);
        topPanel.add(growthLabel);

        // Suwak wzrostu
        JSlider growthSlider = new JSlider(0, 100, 0);
        growthSlider.setEnabled(false); //suwak, ale nie ma funkcjonalności suwaka - nie można nim przesuwać
        growthSlider.setBounds(screenWidth / 3, topPanelHeight - 60, screenWidth / 3, 40);
        growthSlider.setBackground(Color.BLACK);
        growthSlider.setUI(new BasicSliderUI(growthSlider) {
            @Override
            public void paintThumb(Graphics g) { //nadpisanie wskaznika suwaka - rysujemy czerwony trójk ąt
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.RED);
                int[] xPoints = {thumbRect.x + thumbRect.width / 2, thumbRect.x, thumbRect.x + thumbRect.width};
                int[] yPoints = {thumbRect.y + thumbRect.height, thumbRect.y, thumbRect.y};
                g2.fillPolygon(xPoints, yPoints, 3);
                g2.dispose();
            }

            @Override
            public void paintTrack(Graphics g) { //nadpisany tor suwaka - żółte wypełnienie samego paska, po którym porusza się wskaznik
                g.setColor(Color.YELLOW);
                Rectangle trackBounds = trackRect;
                g.fillRect(trackBounds.x, trackBounds.y + trackBounds.height / 3,
                        trackBounds.width, trackBounds.height / 3);
            }
        });
        topPanel.add(growthSlider); //umieszczenie suawaka wzrostu na topPanelu

        // Punkty
        int pointsX = screenWidth - 300;
        
        JLabel pointsLabel = new JLabel(scaleIcon(new ImageIcon("src/projekt_java/punkty.png"), 120, 50));
        pointsLabel.setBounds(pointsX, topPanelHeight - 60, 120, 50);
        topPanel.add(pointsLabel); //umieszczenie grafiki z napisem ounkty na topPanelu

        JPanel pointsPanel = new JPanel(new BorderLayout());
        pointsPanel.setBackground(Color.BLACK);
        pointsPanel.setBounds(pointsX + 130, topPanelHeight - 60, 80, 50);
        zjedzoneRybyValueLabel = new JLabel(String.valueOf(zjedzoneRybyCount), SwingConstants.CENTER);
        zjedzoneRybyValueLabel.setForeground(Color.YELLOW);
        zjedzoneRybyValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pointsPanel.add(zjedzoneRybyValueLabel, BorderLayout.CENTER);
        topPanel.add(pointsPanel); //umieszczenie punktów na topPanelu

        JButton exitButton = new JButton(scaleIcon(new ImageIcon("src/projekt_java/exit.png"), 50, 40));
        makeButtonTransparent(exitButton);
        exitButton.setBounds(pointsPanel.getX() + pointsPanel.getWidth() + 10, pointsPanel.getY(), 80, 50);
        exitButton.addActionListener(e -> System.exit(0));
        topPanel.add(exitButton); //umieszczenie przycisku wyjścia na topPanelu

        //gamePanel
        int gamePanelHeight = screenHeight - topPanelHeight; //stworzenie panelu gry, czyli całego okna, pomniejszonego o wysokość topPanelu
        BackgroundPanel gamePanel = new BackgroundPanel("src/projekt_java/tło_okienek.png");
        gamePanel.setBounds(0, topPanelHeight, screenWidth, gamePanelHeight);

        // Dodanie rybki gracza
        movableFish = new PlayerFish(playerImagePath, gamePanel, growthSlider, zjedzoneRybyValueLabel);
        //movableFish.setBounds(100, 100, 120, 80);
        movableFish.setFocusable(true);
        gamePanel.add(movableFish);
        SwingUtilities.invokeLater(movableFish::requestFocusInWindow); //focus na rybkę gracza
        
        // Przycisk "Menu"
        JButton menuButton = new JButton(scaleIcon(new ImageIcon("src/projekt_java/menu.png"), 120, 50));
        makeButtonTransparent(menuButton);
        menuButton.setBounds(20, topPanelHeight - 60, 120, 50);
        menuButton.addActionListener(e -> { //po kliknęciu zamyka okno i pokazuje menu główne
        	movableFish.triggerMainMenu();
        	movableFish.restartGame();
        	cardLayout.show(cardPanel, "menu");
        });
        topPanel.add(menuButton);

        // dodanie topPanelu i gamePanelu do mainPanelu
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

    
    //Zakładka Wybierz swoją rybkę
    private JPanel chooseYourFishMode() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        ChooseYourFishPanel mainPanel = new ChooseYourFishPanel("src/projekt_java/tło_okienek.png");
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, screenWidth, screenHeight);
        
        // Panel górny
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(new Color(203, 108, 230));
        topPanel.setBounds(0, 0, screenWidth, 80);

        // Przycisk menu
        ImageIcon menuIcon = new ImageIcon("src/projekt_java/menu.png");
        JButton menuButton = new JButton(scaleIcon(menuIcon, 100, 40));
        makeButtonTransparent(menuButton);
        menuButton.setBounds(20, 20, 100, 40);
        menuButton.addActionListener(e -> {
        	cardLayout.show(cardPanel, "menu");
        });
        topPanel.add(menuButton);

        // Przycisk exit
        ImageIcon exitIcon = new ImageIcon("src/projekt_java/exit.png");
        JButton exitButton = new JButton(scaleIcon(exitIcon, 50, 40));
        makeButtonTransparent(exitButton);
        exitButton.setBounds(screenWidth - 100, 20, 80, 40);
        exitButton.addActionListener(e -> System.exit(0)); // Wyjście z gry
        topPanel.add(exitButton);

        mainPanel.add(topPanel);

        // Panel z przyciskami rybek, wywołujemy metodę z parametrami
        JPanel fishSelectionPanel = createFishSelectionPanel(screenWidth, screenHeight);
        mainPanel.add(fishSelectionPanel);

        return mainPanel;
    }


    
    private JPanel createFishSelectionPanel(int screenWidth, int screenHeight) {
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

            // Grafika z podpisem każdej rybki
            ImageIcon nameIcon = new ImageIcon(nameImagePaths[i]);
            JLabel nameLabel = new JLabel(new ImageIcon(nameIcon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH)), SwingConstants.CENTER);

            int finalI = i;
            rybkaButton.addActionListener(e ->
	            {
	            	JOptionPane.showMessageDialog(cardPanel, messages[finalI]);
	            	playerImagePath = imagePaths[finalI];
	            }   
            );

            // Dodanie przycisków i podpisów do panelu
            JPanel buttonWithLabelPanel = new JPanel(new BorderLayout());
            buttonWithLabelPanel.setOpaque(false);
            buttonWithLabelPanel.add(rybkaButton, BorderLayout.CENTER);
            buttonWithLabelPanel.add(nameLabel, BorderLayout.SOUTH);

            fishButtonPanel.add(buttonWithLabelPanel);
        }

        centerPanel.add(fishButtonPanel, BorderLayout.CENTER);
        centerPanel.setBounds(screenWidth / 2 - 450, screenHeight / 2 - 300, 900, 600);

        return centerPanel;
    }


    private void showInfoWindow() {
        JFrame infoFrame = new JFrame("Informacja o grze");
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setSize(400, 220);
        infoFrame.setResizable(false);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());

        JLabel infoLabel = new JLabel("<html><h2>Gra w ryby!</h2><p>Zasady są proste. Zjadaj mniejszych od siebie i unikaj większych. W oceanie rządzą brutalne prawa natury! Gdy zje cię większa ryba gra się kończy! Zdobądź 1000 punktów i zostań królem tego zbiornika wodnego!</h2><p>PS: Możesz też wybrać swoją rybkę!</p></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Powodzenia ;)");
        closeButton.addActionListener(e -> infoFrame.dispose());
        infoPanel.add(closeButton, BorderLayout.SOUTH);

        infoFrame.setContentPane(infoPanel);
        infoFrame.setLocationRelativeTo(null);
        infoFrame.setVisible(true);
    }
    

    public static void main(String[] args) {
    	System.setProperty("sun.java2d.uiScale", "1.0");

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.initMainFrame();
        });
    }
    
}

