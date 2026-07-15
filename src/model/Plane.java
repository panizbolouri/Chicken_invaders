package model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Plane {
    private int x, y;
    private int speed = 5;
    private int lives = 3;
    private int fireLevel = 1;
    private long lastShotTime = 0;
    private long shotDelay = 300;
    private Image image;

    private boolean shieldActive = false;
    private int shieldTimer = 0;
    private boolean rapidFireActive = false;
    private int rapidFireTimer = 0;

    public Plane(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon("res/plane.png").getImage();
    }

    public void move(int dx, int dy) {
        x += dx * speed;
        y += dy * speed;

        if (x < 0) x = 0;
        if (x > 800 - 100) x = 800 - 100;
        if (y < 0) y = 0;
        if (y > 600 - 100) y = 600 - 100;
    }

    public void updateTimers() {
        if (shieldActive) {
            shieldTimer--;
            if (shieldTimer <= 0) shieldActive = false;
        }
        if (rapidFireActive) {
            rapidFireTimer--;
            if (rapidFireTimer <= 0) {
                rapidFireActive = false;
                shotDelay = 300;
            }
        }
    }

    public ArrayList<Bullet> shoot() {
        ArrayList<Bullet> newBullets = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastShotTime >= shotDelay) {
            lastShotTime = currentTime;

            if (fireLevel == 1) {
                newBullets.add(new Bullet(x + 40, y));
            } else {
                int spread = 20;
                int startX = x + 40 - ((fireLevel - 1) * spread) / 2;
                for (int i = 0; i < fireLevel; i++) {
                    newBullets.add(new Bullet(startX + (i * spread), y));
                }
            }
        }
        return newBullets;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 100, 100, null);
        if (shieldActive) {
            g.setColor(new Color(0, 255, 255, 100));
            g.fillOval(x - 10, y - 10, 120, 120);
            g.setColor(Color.CYAN);
            g.drawOval(x - 10, y - 10, 120, 120);
        }
    }

    public void upgradeFireLevel() { fireLevel++; }

    public void activateRapidFire() {
        rapidFireActive = true;
        rapidFireTimer = 480;
        shotDelay = 150;
    }

    public void activateShield() {
        shieldActive = true;
        shieldTimer = 600;
    }

    public void addLife() {
        if (lives < 5) lives++;
    }

    public void loseLife() {
        if (!shieldActive) lives--;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getLives() { return lives; }
    public boolean isDead() { return lives <= 0; }
    public boolean isShieldActive() { return shieldActive; }
    public int getFireLevel() { return fireLevel; }
    public boolean isRapidFireActive() { return rapidFireActive; }
}