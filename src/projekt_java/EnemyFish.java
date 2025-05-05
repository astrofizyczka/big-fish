package projekt_java;

import javax.swing.*;
import java.awt.*;

public class EnemyFish extends JLabel {
    private int speed;
    private int width, height;

    public EnemyFish(String imagePath, int x, int y, int size) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));
        setBounds(x, y, size, size);

        this.width = size;
        this.height = size;
        this.speed = 2 + (int)(Math.random() * 3); // losowa prędkość od 2 do 4
    }

    public void move() {
        setLocation(getX() - speed, getY());
    }

    public boolean isOutOfScreen() {
        return getX() + width < 0;
    }
}

