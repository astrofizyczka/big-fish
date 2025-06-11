package projekt_java;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private Timer leftSpawnTimer;
    private Timer rightSpawnTimer;
    private Timer moveTimer;
    public List<EnemyFish> enemies = new ArrayList<>();
    private String[] fishPaths = {
        "src/projekt_java/Mała_ryba.png",
        "src/projekt_java/Średnia_ryba.png",
        "src/projekt_java/Duża_ryba.png"
        
    };

    public BackgroundPanel(String bgPath) {
        this.backgroundImage = new ImageIcon(bgPath).getImage();
        setLayout(null);
        startEnemySpawning(); // uruchamia generowanie wrogów po utworzeniu panelu.
    }
    
    public void stopAllTimers() {
        if (leftSpawnTimer != null) leftSpawnTimer.stop();
        if (rightSpawnTimer != null) rightSpawnTimer.stop();
        if (moveTimer != null) moveTimer.stop();
    }

    public void startEnemySpawning() {
    	//spawnowanie nowych ryb co 2 sekundy po prawej i lewej stronie ekaranu
    	rightSpawnTimer = new Timer(2000, e -> spawnEnemy("right"));
    	rightSpawnTimer.start();
        
    	leftSpawnTimer = new Timer(2000, e -> spawnEnemy("left"));
    	leftSpawnTimer.start();

        moveTimer = new Timer(30, e -> moveEnemies());
        moveTimer.start();
    }

    private void spawnEnemy(String side) {
        int panelHeight = getHeight();
        //int panelWidth = 0;
       

        //generowanie randomowej ryby
        Random rand = new Random();
        int index = rand.nextInt(fishPaths.length);
        String path = fishPaths[index];

        int size;
        switch (index) {
            case 0: size = 40; break; // mała
            case 1: size = 100; break; // średnia
            case 2: size = 160; break; // duża
            default: size = 40;
        }
        
        int y = rand.nextInt(panelHeight - size); //losowanie wysokości spawnowania ryby
        String direction = "";
        if (side == "left") {
        	direction = "right";
        }
        else {
        	direction = "left";
        }
        
        int x;
        if (side.equals("left")) {
            x = -size; // start poza lewą krawędzią
        } else {
            x = getWidth(); // start poza prawą krawędzią
        }

        EnemyFish fish = new EnemyFish(path, x, y, size, direction, getWidth());

        enemies.add(fish);
        this.add(fish);
        this.repaint();
    }

    private void moveEnemies() {
    	//iterator dla listy enemies
    	Iterator<EnemyFish> iter = enemies.iterator();
    	while (iter.hasNext()) {
    	    EnemyFish enemy = iter.next();
    	    if ("left".equals(enemy.direction)) {
    	    	enemy.move("left");
    	    }
    	    else {
    	    	enemy.move("right");
    	    }
    	    
    	    //sprawdzanie czy ryba opuściła ekran
            if (enemy.isOutOfScreen(enemy.direction)) {
                this.remove(enemy);
                iter.remove();
            }
    	}
    	
        this.repaint();
        Toolkit.getDefaultToolkit().sync(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
