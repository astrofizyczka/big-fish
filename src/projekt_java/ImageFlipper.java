package projekt_java;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class ImageFlipper {

	public static ImageIcon flipImageIconHorizontally(ImageIcon icon) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        //stworzenie buforowanego obrazu
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(icon.getImage(), 0, 0, null);
        g2.dispose();

        //Odbicie w poziomie
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); // obróć w osi x
        tx.translate(-width, 0); // przesunięcie obrazu, bo po odbiciu lewa krawędz znajduje się po prawej stronie
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage flippedImage = op.filter(bufferedImage, null);

        return new ImageIcon(flippedImage);
    }

}
