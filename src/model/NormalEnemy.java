package model;

import javax.swing.*;
import java.awt.*;

public class NormalEnemy extends Enemy {
    private Image image;

    public NormalEnemy(int x, int y) {
        super(x, y, 2, 10);
        this.image = new ImageIcon("res/normal_chicken.png").getImage();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}