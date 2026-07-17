package model;

import javax.swing.*;
import java.awt.*;

public class SniperPlane extends Plane {
    private Image image;

    public SniperPlane(int x, int y) {
        super(x, y, 5, 150, 3, "Sniper");
        this.image = new ImageIcon("res/sniperplane.png").getImage();
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 100, 100, null);
        }
        if (isShieldActive()) {
            g.setColor(new Color(0, 200, 255, 100));
            g.fillOval(x - 10, y - 10, 120, 120);
            g.setColor(new Color(0, 255, 255, 200));
            g.drawOval(x - 10, y - 10, 120, 120);
        }
    }

    @Override
    public int getDamageAgainstBoss() {
        return 2;
    }
}