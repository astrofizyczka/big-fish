package projekt_java;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class EnemyFish extends JLabel {
    private int speed;
    public int x, y;
    public String direction;
    public int panelWidth;
    public int level;

    public EnemyFish(String imagePath, int x, int y, int size, String direction, int panelWidth) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        
        if (direction == "left") {
        	setIcon(new ImageIcon(scaledImage));
        }
        else {
        	setIcon(ImageFlipper.flipImageIconHorizontally(new ImageIcon(scaledImage)));
 
        }
        
        setBounds(x, y, size, size);

        this.speed = 2 + (int)(Math.random() * 3); // losowa prędkość od 2 do 4
        this.direction = direction;
        this.panelWidth = panelWidth;
        this.x = x;
        this.y = y;
        
        if (imagePath.contains("Mała_ryba")) { //określenie poziomu wrogiej ryby
            this.level = 1;
        } else if (imagePath.contains("Średnia_ryba")) {
            this.level = 2;
        } else if (imagePath.contains("Duża_ryba")) {
            this.level = 3;
        } else {
            this.level = 1; // default
        }
    }
    
    //metoda na ruch rybek
    public void move(String direction) {
    	if (direction == "left") {
    		setLocation(getX() - speed, getY());
    	}
    	else {
    		setLocation(getX() + speed, getY());
    	}
    }

    //metoda na sprawdzenie czy ryba jest poza ekranem
    public boolean isOutOfScreen(String direction) {
    	if (direction == "left") {
            return getX() + getWidth() < 0 ;
    	}
    	else {
    		return getX() > panelWidth;
    	}

    }
}

