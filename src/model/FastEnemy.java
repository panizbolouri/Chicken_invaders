package model;
import javax.swing.*;
import java.awt.*;

public class FastEnemy extends Enemy {
    private Image image;

    public FastEnemy(int x, int y) {
        super(x, y, 1, 15);
        this.image = new ImageIcon("res/fast_chicken.png").getImage();
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx * 2, dy);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}