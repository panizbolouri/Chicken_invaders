package model;

import javax.swing.*;
import java.awt.*;

public class FastPlane extends Plane {
    private Image image;

    public FastPlane(int x, int y) {
        super(x, y, 7, 250, 3, "Fast");
        this.image = new ImageIcon("res/fastplane.png").getImage();
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
}