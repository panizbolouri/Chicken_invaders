package model;

import javax.swing.*;
import java.awt.*;

public class DefaultPlane extends Plane {
    private Image image;

    public DefaultPlane(int x, int y) {
        super(x, y, 5, 300, 3, "Default");
        this.image = new ImageIcon("res/plane.png").getImage();
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 100, 100, null);
        }
    }
}