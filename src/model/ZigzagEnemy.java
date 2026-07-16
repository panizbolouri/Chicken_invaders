package model;

import javax.swing.*;
import java.awt.*;

public class ZigzagEnemy extends Enemy {
    private Image image;
    private int yOffset = 0;
    private boolean movingUp = true;

    public ZigzagEnemy(int x, int y) {
        super(x, y, 2, 20);
        this.image = new ImageIcon("res/zigzag_chicken.png").getImage();
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);

        if (dy == 0) {
            if (movingUp) {
                this.y -= 2; yOffset -= 2;
                if (yOffset <= -15) movingUp = false;
            } else {
                this.y += 2; yOffset += 2;
                if (yOffset >= 15) movingUp = true;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}