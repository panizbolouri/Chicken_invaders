package model;
import javax.swing.*;
import java.awt.*;

public class EnemyBullet extends Egg {
    private Image bulletImage;

    public EnemyBullet(int x, int y, double dx, double dy) {
        super(x, y, dx, dy);

        this.bulletImage = new ImageIcon("res/laser_red.png").getImage();
    }

    @Override
    public void draw(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, getX(), getY(), 8, 20, null);
        }
    }
}