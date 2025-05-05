package projekt_java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovableImage extends JLabel {
    private int dx = 0;
    private int dy = 0;
    private final int MOVE_STEP = 5;

    public MovableImage(String imagePath, JPanel gamePanel) {
        // Stały rozmiar rybki
        int fishWidth = 100;
        int fishHeight = 60;

        // Skaluj obrazek do rozmiaru rybki
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(fishWidth, fishHeight, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));

        // Ustaw rozmiar JLabela
        setSize(fishWidth, fishHeight);
        setLocation(100, 100);

        setFocusable(true);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> dx = -MOVE_STEP;
                    case KeyEvent.VK_RIGHT -> dx = MOVE_STEP;
                    case KeyEvent.VK_UP -> dy = -MOVE_STEP;
                    case KeyEvent.VK_DOWN -> dy = MOVE_STEP;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                dx = 0;
                dy = 0;
            }
        });

        // Timer do poruszania
        Timer timer = new Timer(20, e -> moveFish(gamePanel.getWidth(), gamePanel.getHeight()));
        timer.start();
    }

    public void moveFish(int panelWidth, int panelHeight) {
        int newX = Math.max(0, Math.min(getX() + dx, panelWidth - getWidth()));
        int newY = Math.max(0, Math.min(getY() + dy, panelHeight - getHeight()));
        setLocation(newX, newY);
        setFocusable(true);
    }
}

