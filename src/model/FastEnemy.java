package model;

import javax.swing.*;
import java.awt.*;

public class FastEnemy extends Enemy {
    private Image image;
    private int xOffset = 0;
    private boolean movingRight = true;

    public FastEnemy(int x, int y) {
        super(x, y, 1, 15);
        this.image = new ImageIcon("res/fast_chicken.png").getImage();
    }

    @Override
    public void setX(int base_x) {
        if (movingRight) {
            xOffset += 3;
            if (xOffset >= 25) movingRight = false;
        } else {
            xOffset -= 3;
            if (xOffset <= -25) movingRight = true;
        }
        super.setX(base_x + xOffset);
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
    }
}