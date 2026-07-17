package model;

import javax.swing.*;
import java.awt.*;

public class HeavyPlane extends Plane {
    private Image image;

    public HeavyPlane(int x, int y) {
        super(x, y, 4, 200, 5, "Heavy");
        this.image = new ImageIcon("res/heavyplane.png").getImage();
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 100, 100, null);
        }
    }
}