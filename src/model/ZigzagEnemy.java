package model;

import javax.swing.*;
import java.awt.*;

public class ZigzagEnemy extends Enemy {
    private Image image;
    private int yOffset = 0;
    private boolean movingDown = true;

    public ZigzagEnemy(int x, int y) {
        super(x, y, 2, 20);
        this.image = new ImageIcon("res/zigzag_chicken.png").getImage();
    }

    @Override
    public void setY(int base_y) {
        if (movingDown) {
            yOffset += 2;
            if (yOffset >= 15) movingDown = false;
        } else {
            yOffset -= 2;
            if (yOffset <= -15) movingDown = true;
        }
        super.setY(base_y + yOffset);
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
    }
}