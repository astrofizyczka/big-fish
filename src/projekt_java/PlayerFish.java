package projekt_java;

import javax.swing.*;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayerFish extends JLabel {
    private int dx = 0, dy = 0; //prędkość ryby w poziomie i w pionie
    private Timer moveTimer;
    private ImageIcon originalIcon; 
    private ImageIcon flippedIcon;
    private boolean isFacingRight = false;
	private JSlider growthSlider;
    private int playerLevel = 1;
    private int growthCount = 0;
    private int score = 0;
    private JLabel scoreLabel;
    private JPanel parentPanel;

    public PlayerFish(String imagePath, JPanel parentPanel, JSlider growthSlider, JLabel scoreLabel) { //stworzenie ryby gracza
        this.parentPanel = parentPanel;
        int width = 120;
        int height = 80;

        //załadowanie obrazu ryby oraz stworzenie obrazu ryby przerzuconej w poziomie
        originalIcon = scaleImageIcon(new ImageIcon(imagePath), width, height); //skalowanie wielkości
        flippedIcon = ImageFlipper.flipImageIconHorizontally(originalIcon);

        setIcon(originalIcon);
        setBounds(100, 100, width, height);
        setupKeyBindings(parentPanel);

        //stworzenie timera do ruchu rybką, który co 16ms wywołuje ruch ryby
        moveTimer = new Timer(16, e -> moveFish()); 
        moveTimer.start();
        
        this.growthSlider = growthSlider;
        this.scoreLabel = scoreLabel;
    }

    private void moveFish() {
    	//obliczanie nowej pozycji ryby
        if (dx != 0 || dy != 0) {
            int newX = getX() + dx;
            int newY = getY() + dy;
            
            // ustalenie granic panelu gry
            int panelWidth = parentPanel.getWidth();
            int panelHeight = parentPanel.getHeight();

            // szerokość i wysokość rybki
            int fishWidth = getWidth();
            int fishHeight = getHeight();

            // ograniczenie ruchu poziomy
            if (newX < 0) newX = 0;
            if (newX + fishWidth > panelWidth) newX = panelWidth - fishWidth;

            // ograniczenie ruchu pionowy
            if (newY < 0) newY = 0;
            if (newY + fishHeight > panelHeight) newY = panelHeight - fishHeight;
            
            setLocation(newX, newY);
        }

        //odbijanie grafiki ryby w poziomie jeśli zmieniła kierunek płynięcia
        if (dx > 0 && !isFacingRight) {
            setIcon(flippedIcon);
            isFacingRight = true;
        } else if (dx < 0 && isFacingRight) {
            setIcon(originalIcon);
            isFacingRight = false;
        }
        
        checkCollision(); //sprawdzanie kolizji
    }
    
    private void checkCollision() {
        Component[] components = getParent().getComponents(); //Pobiera wszystkie komponenty  znajdujące się w tym samym panelu co PlayerFish 
        //(czyli jego "rodzicu", np. BackgroundPanel).

        for (Component comp : components) {
            if (comp instanceof EnemyFish && comp != this) { //sprawdzenie w pętli czy dany komponent to wroga ryba i czy nie jest rybą gracza
                EnemyFish enemy = (EnemyFish) comp; //rzutowanie komponentu do typu EnemyFish, żeby mieć dostęp do jego pól

                Rectangle enemyBounds = enemy.getBounds(); //pobieranie prostokątu opisującego położenie i rozmiar wrofiej ryby

                //obliczenie środka wrogiej ryby
                int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
                int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;

                //ustawienie marginesu obszaru kolizji, by obszar ten był większy niż sam środek
                int horizontalMargin = (int) (enemyBounds.width * 0.4);
                int verticalMargin = (int) (enemyBounds.height * 0.4);

                // stworzenie prostokątu reprezentującego obszar kolizji
                Rectangle collisionZone = new Rectangle(
                    enemyCenterX - horizontalMargin,
                    enemyCenterY - verticalMargin,
                    horizontalMargin * 2,
                    verticalMargin * 2
                );
                
                //obliczenie środka ryby gracza
                int playerCenterX = this.getX() + this.getWidth() / 2;
                int playerCenterY = this.getY() + this.getHeight() / 2;

                //sprawdzenie czy środek gracza znajduje się w obszarze kolizji
                if (collisionZone.contains(playerCenterX, playerCenterY)) {
                    //spawdzenie czy gracz może zjeść przeciwnika w zależności od swojegp poziomu
                    boolean canEat = switch (playerLevel) {
                        case 1 -> enemy.level == 1;
                        case 2 -> enemy.level <= 2;
                        case 3 -> enemy.level <= 3;
                        default -> false;
                    };

                    if (canEat) {
                        //System.out.println("Zjedzono rybę poziomu l " + enemy.level);
                    	//jeśli może zjeść to wroga ryba jest usuwana z panelu i listy wrogów 
                        getParent().remove(enemy);
                        ((BackgroundPanel) getParent()).enemies.remove(enemy);
                        getParent().repaint();

                        handleScoring(enemy); //aktualizacja punktów
                    } else {
                        //System.out.println("Koniec gry. Kolizja z rybą " + enemy.level);
                        triggerGameOver();
                    }

                    break; //jedna kolizja na kratkę
                }
            }
        }
    }
    
    private void triggerGameOver() {
    	//zatrzymanie ruchu
        dx = 0;
        dy = 0;
        moveTimer.stop();

        //pobiera panel gry i zatrzynuje ruch innych ryb
        if (getParent() instanceof BackgroundPanel panel) {
            panel.stopAllTimers();
        }

        Object[] options = {"TAK", "NIE"};
        int result = JOptionPane.showOptionDialog(
            SwingUtilities.getWindowAncestor(this),
            "Przegrałeś! Czy chcesz zagrać ponownie?",
            "Przegrałeś!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (result == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }
    
    private void restartGame() {
        if (!(getParent() instanceof BackgroundPanel panel)) return;

        // usuwanie wrogich ryb (wizualne i logiczne)
        for (EnemyFish enemy : new ArrayList<>(panel.enemies)) {
            panel.remove(enemy);
        }
        panel.enemies.clear();

        // restartowanie statystyk ryby gracza
        playerLevel = 1;
        growthCount = 0;
        setBounds(panel.getWidth() / 2, panel.getHeight() / 2, 120, 80); // ustawienie początkowej pozycji
        
        score = 0;
        scoreLabel.setText(String.valueOf(score)); //restartowanie liczby punktów


        // reskalowanie ikony do wielkości początkowej
        originalIcon = scaleImageIcon(new ImageIcon("src/projekt_java/Sylwia1.png"), 120, 80);
        flippedIcon = ImageFlipper.flipImageIconHorizontally(originalIcon);
        setIcon(originalIcon);
        isFacingRight = false;

        // reset slidera
        if (growthSlider != null) {
            growthSlider.setValue(0);
        }

        // restart timerów ruchu
        moveTimer.start();
        panel.startEnemySpawning();
        panel.repaint();

        //System.out.println("restart gry");
    }
   
    //punktacja
    private void handleScoring(EnemyFish enemy) {
        int pointsEarned = switch (enemy.level) {
            case 1 -> 10;
            case 2 -> 15;
            case 3 -> 20;
            default -> 0;
        };

        score += pointsEarned;
        scoreLabel.setText(String.valueOf(score));

        //obliczenie procrocentu procesu zwycięsta i aktualizacja slidera
        int progressPercent = (int) (score / 1000.0 * 100);
        growthSlider.setValue(Math.min(progressPercent, 100));

        //warunki wzrostu
        if (score >= 500 && playerLevel < 3) {
            grow();
        } else if (score >= 100 && playerLevel < 2) {
            grow();
        }

        //sprawdzenie wygranej
        if (score >= 1000) {
            triggerVictory();
        }
    }
    
    
    private void triggerVictory() {
        dx = 0;
        dy = 0;
        moveTimer.stop();

        if (getParent() instanceof BackgroundPanel panel) {
            panel.stopAllTimers();
        }

        Object[] options = {"TAK", "NIE"};
        int result = JOptionPane.showOptionDialog(
            SwingUtilities.getWindowAncestor(this),
            "Wygrałeś!!! Czy chcesz zagrać ponownie?",
            "Wygrałeś!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (result == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    
    private void grow() {
    	//zwiększa poziom ryby gracza, co wpływa na to że może teraz zjadać większe ryby
        playerLevel++;
        //System.out.println("rośnięcie do poziomu " + playerLevel);

        //obliczanie nowego rozmiaru ryby
        int newWidth = (int) (getWidth() * 1.3);
        int newHeight = (int) (getHeight() * 1.3);
        //aktualizacja rozmiaru ryby na ekranie
        setBounds(getX(), getY(), newWidth, newHeight);

        // skalowanie
        originalIcon = scaleImageIcon(originalIcon, newWidth, newHeight);
        flippedIcon = ImageFlipper.flipImageIconHorizontally(originalIcon);
        setIcon(isFacingRight ? flippedIcon : originalIcon);
    }
    
    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    
    
    //obsługa klawiatury
    private void setupKeyBindings(JComponent comp) {
    	//mapowanie klawiszy na ciągi znaków a następnie na akcje
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = comp.getActionMap();

        // powiązanie klawiszy ze nazwami akcji
        im.put(KeyStroke.getKeyStroke("pressed LEFT"), "leftPressed");
        im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "rightPressed");
        im.put(KeyStroke.getKeyStroke("pressed UP"), "upPressed");
        im.put(KeyStroke.getKeyStroke("pressed DOWN"), "downPressed");

        im.put(KeyStroke.getKeyStroke("released LEFT"), "leftReleased");
        im.put(KeyStroke.getKeyStroke("released RIGHT"), "rightReleased");
        im.put(KeyStroke.getKeyStroke("released UP"), "upReleased");
        im.put(KeyStroke.getKeyStroke("released DOWN"), "downReleased");

        // akcja
        am.put("leftPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dx = -4;
            }
        });

        am.put("rightPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dx = 4;
            }
        });

        am.put("upPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dy = -4;
            }
        });

        am.put("downPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dy = 4;
            }
        });

        // zatrzymanie ruch, gdy klawisze są puszczone
        am.put("leftReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dx < 0) dx = 0; //dzięki warunkowi if (dx < 0) unikamy nadpisania ruchu w przeciwną stronę 
                					//jeśli użytkownik trzyma dwa klawisze naraz
            }
        });

        am.put("rightReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dx > 0) dx = 0;
            }
        });

        am.put("upReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dy < 0) dy = 0;
            }
        });

        am.put("downReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dy > 0) dy = 0;
            }
        });
    }
}


