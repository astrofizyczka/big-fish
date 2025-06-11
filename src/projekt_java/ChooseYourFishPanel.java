package projekt_java;

import javax.swing.*;
import java.awt.*;


public class ChooseYourFishPanel extends JPanel {
    private Image backgroundImage;

    public ChooseYourFishPanel(String bgPath) {
        this.backgroundImage = new ImageIcon(bgPath).getImage();
        setLayout(null);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
