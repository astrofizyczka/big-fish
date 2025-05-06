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
    private int dx = 0, dy = 0;
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

    public PlayerFish(String imagePath, JPanel parentPanel, JSlider growthSlider, JLabel scoreLabel) {
        this.parentPanel = parentPanel;
        int width = 120;
        int height = 80;

        // Load original and flipped images
        originalIcon = scaleImageIcon(new ImageIcon(imagePath), width, height);
        flippedIcon = flipImageIconHorizontally(originalIcon);

        setIcon(originalIcon);
        setBounds(100, 100, width, height);
        setupKeyBindings(parentPanel);

        moveTimer = new Timer(16, e -> moveFish());
        moveTimer.start();
        
        this.growthSlider = growthSlider;
        this.scoreLabel = scoreLabel;
    }

    private void moveFish() {
        if (dx != 0 || dy != 0) {
            int newX = getX() + dx;
            int newY = getY() + dy;
            
            // Granice panelu
            int panelWidth = parentPanel.getWidth();
            int panelHeight = parentPanel.getHeight();

            // Szerokość i wysokość rybki
            int fishWidth = getWidth();
            int fishHeight = getHeight();

            // Ogranicz ruch poziomy
            if (newX < 0) newX = 0;
            if (newX + fishWidth > panelWidth) newX = panelWidth - fishWidth;

            // Ogranicz ruch pionowy
            if (newY < 0) newY = 0;
            if (newY + fishHeight > panelHeight) newY = panelHeight - fishHeight;
            
            setLocation(newX, newY);
        }

        // Flip image based on movement direction
        if (dx > 0 && !isFacingRight) {
            setIcon(flippedIcon);
            isFacingRight = true;
        } else if (dx < 0 && isFacingRight) {
            setIcon(originalIcon);
            isFacingRight = false;
        }
        
        checkCollision();
    }
    
    private void checkCollision() {
        Component[] components = getParent().getComponents();

        for (Component comp : components) {
            if (comp instanceof EnemyFish && comp != this) {
                EnemyFish enemy = (EnemyFish) comp;

                Rectangle enemyBounds = enemy.getBounds();

                int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
                int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;

                int horizontalMargin = (int) (enemyBounds.width * 0.8);
                int verticalMargin = (int) (enemyBounds.height * 0.5);

                // Obszar kolizji = środek ryby ±20%
                Rectangle collisionZone = new Rectangle(
                    enemyCenterX - horizontalMargin,
                    enemyCenterY - verticalMargin,
                    horizontalMargin * 2,
                    verticalMargin * 2
                );

                int playerCenterX = this.getX() + this.getWidth() / 2;
                int playerCenterY = this.getY() + this.getHeight() / 2;

                if (collisionZone.contains(playerCenterX, playerCenterY)) {
                    // Determine if the player can eat this fish based on level
                    boolean canEat = switch (playerLevel) {
                        case 1 -> enemy.level == 1;
                        case 2 -> enemy.level <= 2;
                        case 3 -> enemy.level <= 3;
                        default -> false;
                    };

                    if (canEat) {
                        //System.out.println("Zjedzono rybę poziomu l " + enemy.level);

                        getParent().remove(enemy);
                        ((BackgroundPanel) getParent()).enemies.remove(enemy);
                        getParent().repaint();

                        handleScoring(enemy);
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
        dx = 0;
        dy = 0;
        moveTimer.stop();

        if (getParent() instanceof BackgroundPanel panel) {
            panel.stopAllTimers();
        }

        Object[] options = {"TAK", "NIE"};
        int result = JOptionPane.showOptionDialog(
            SwingUtilities.getWindowAncestor(this),
            "Przegrałeś! Czy chcesz zagrać ponownie?",
            "Przegrałeś!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE,
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

        // usuwanie wrogich ryb
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
        flippedIcon = flipImageIconHorizontally(originalIcon);
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
   
    
    private void handleScoring(EnemyFish enemy) {
        int pointsEarned = switch (enemy.level) {
            case 1 -> 10;
            case 2 -> 15;
            case 3 -> 20;
            default -> 0;
        };

        score += pointsEarned;
        scoreLabel.setText(String.valueOf(score));

        int progressPercent = (int) (score / 1000.0 * 100);
        growthSlider.setValue(Math.min(progressPercent, 100));

        // Handle level growth
        if (score >= 500 && playerLevel < 3) {
            grow();
        } else if (score >= 100 && playerLevel < 2) {
            grow();
        }

        // Victory check
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

        JOptionPane.showMessageDialog(
            SwingUtilities.getWindowAncestor(this),
            "Gratulacje! Wygrałeś grę!",
            "Zwycięstwo!",
            JOptionPane.INFORMATION_MESSAGE
        );

        System.exit(0); // or restartGame();
    }

    
    private void grow() {
        playerLevel++;
        //System.out.println("rośnięcie do poziomu " + playerLevel);

        int newWidth = (int) (getWidth() * 1.3);
        int newHeight = (int) (getHeight() * 1.3);
        setBounds(getX(), getY(), newWidth, newHeight);

        // Rescale icons
        originalIcon = scaleImageIcon(originalIcon, newWidth, newHeight);
        flippedIcon = flipImageIconHorizontally(originalIcon);
        setIcon(isFacingRight ? flippedIcon : originalIcon);
    }
    
    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    
    public static ImageIcon flipImageIconHorizontally(ImageIcon icon) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        // Create a buffered image from the ImageIcon
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(icon.getImage(), 0, 0, null);
        g2.dispose();

        // Flip the image horizontally using AffineTransform
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); // flip X
        tx.translate(-width, 0); // move back into position
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage flippedImage = op.filter(bufferedImage, null);

        return new ImageIcon(flippedImage);
    }
    
    private void setupKeyBindings(JComponent comp) {
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = comp.getActionMap();

        // Key pressed bindings
        im.put(KeyStroke.getKeyStroke("pressed LEFT"), "leftPressed");
        im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "rightPressed");
        im.put(KeyStroke.getKeyStroke("pressed UP"), "upPressed");
        im.put(KeyStroke.getKeyStroke("pressed DOWN"), "downPressed");

        // Key released bindings
        im.put(KeyStroke.getKeyStroke("released LEFT"), "leftReleased");
        im.put(KeyStroke.getKeyStroke("released RIGHT"), "rightReleased");
        im.put(KeyStroke.getKeyStroke("released UP"), "upReleased");
        im.put(KeyStroke.getKeyStroke("released DOWN"), "downReleased");

        // Movement actions
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

        // Stop movement when keys are released
        am.put("leftReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dx < 0) dx = 0;
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


