package projekt_java;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private Timer spawnTimer;
    private Timer moveTimer;
    private List<EnemyFish> enemies = new ArrayList<>();
    private String[] fishPaths = {
        "src/projekt_java/Mała_ryba.png",
        "src/projekt_java/Średnia_ryba.png",
        "src/projekt_java/Duża_ryba.png"
        
    };

    public BackgroundPanel(String bgPath) {
        this.backgroundImage = new ImageIcon(bgPath).getImage();
        setLayout(null);
        startEnemySpawning();
    }

    private void startEnemySpawning() {
        spawnTimer = new Timer(1000, e -> spawnEnemy());
        spawnTimer.start();

        moveTimer = new Timer(30, e -> moveEnemies());
        moveTimer.start();
    }

    private void spawnEnemy() {
        int panelHeight = getHeight();
        int panelWidth = getWidth();
        if (panelHeight == 0 || panelWidth == 0) return; // zabezpieczenie

        Random rand = new Random();
        int index = rand.nextInt(fishPaths.length);
        String path = fishPaths[index];

        int size;
        switch (index) {
            case 0: size = 40; break; // mała
            case 1: size = 70; break; // średnia
            case 2: size = 100; break; // duża
            default: size = 60;
        }

        int y = rand.nextInt(Math.max(1, panelHeight - size));
        EnemyFish fish = new EnemyFish(path, panelWidth, y, size);
        enemies.add(fish);
        this.add(fish);
        this.repaint();
    }

    private void moveEnemies() {
        Iterator<EnemyFish> iter = enemies.iterator();
        while (iter.hasNext()) {
            EnemyFish fish = iter.next();
            fish.move();
            if (fish.isOutOfScreen()) {
                this.remove(fish);
                iter.remove();
            }
        }
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
