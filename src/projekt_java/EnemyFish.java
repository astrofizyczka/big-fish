package projekt_java;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class EnemyFish extends JLabel {
    private int speed;
    private int width, height;
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
        	setIcon(flipImageIconHorizontally(new ImageIcon(scaledImage))); 
        }
        
        setBounds(x, y, size, size);

        this.width = size;
        this.height = size;
        this.speed = 2 + (int)(Math.random() * 3); // losowa prędkość od 2 do 4
        this.direction = direction;
        this.panelWidth = panelWidth;
        this.x = x;
        this.y = y;
        
        if (imagePath.contains("Mała_ryba")) {
            this.level = 1;
        } else if (imagePath.contains("Średnia_ryba")) {
            this.level = 2;
        } else if (imagePath.contains("Duża_ryba")) {
            this.level = 3;
        } else {
            this.level = 1; // default
        }
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
    
    public void move(String direction) {
    	if (direction == "left") {
    		setLocation(getX() - speed, getY());
    	}
    	else {
    		setLocation(getX() + speed, getY());
    	}
    }

    public boolean isOutOfScreen(String direction) {
    	if (direction == "left") {
            return getX() + getWidth() < 0 ;
    	}
    	else {
    		return getX() > panelWidth;
    	}

    }
}

