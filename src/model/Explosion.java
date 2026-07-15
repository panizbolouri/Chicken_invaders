package model;

import javax.swing.*;
import java.awt.*;

public class Explosion {
    private int x;
    private int y;
    private int radius;
    private int maxRadius;
    private int alpha;
    private Image image;

    public Explosion(int x, int y, int maxRadius) {
        this.x = x;
        this.y = y;
        this.radius = 10;
        this.maxRadius = maxRadius;
        this.alpha = 255;
        this.image = new ImageIcon("res/Explosion.png").getImage();
    }

    public void update() {
        radius += 4;
        alpha -= 10;
        if (alpha < 0) {
            alpha = 0;
        }
    }

    public boolean isFinished() {
        return alpha <= 0 || radius >= maxRadius;
    }

    public void draw(Graphics g) {
        if (alpha > 0 && image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
            g2d.drawImage(image, x - radius, y - radius, radius * 2, radius * 2, null);
            g2d.dispose();
        }
    }
}