package model;

import java.awt.*;
import java.util.ArrayList;

public abstract class Plane {
    protected int x, y;
    protected int speed;
    protected int lives;
    protected int fireLevel = 1;
    protected int fireDelay;
    protected long lastShotTime = 0;
    protected int shieldTimer = 0;
    protected int rapidFireTimer = 0;
    protected String type;

    public Plane(int x, int y, int speed, int fireDelay, int lives, String type) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.fireDelay = fireDelay;
        this.lives = lives;
        this.type = type;
    }

    public void move(int dx, int dy) {
        x += dx * speed;
        y += dy * speed;

        if (x < 0) x = 0;
        if (x > 800 - 100) x = 800 - 100;
        if (y < 0) y = 0;
        if (y > 600 - 100) y = 600 - 100;
    }

    public ArrayList<Bullet> shoot() {
        ArrayList<Bullet> createdBullets = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        int currentDelay = isRapidFireActive() ? fireDelay / 2 : fireDelay;

        if (currentTime - lastShotTime >= currentDelay) {
            lastShotTime = currentTime;
            if (fireLevel == 1) {
                createdBullets.add(new Bullet(x + 40, y));
            } else if (fireLevel == 2) {
                createdBullets.add(new Bullet(x + 20, y));
                createdBullets.add(new Bullet(x + 60, y));
            } else {
                createdBullets.add(new Bullet(x + 10, y));
                createdBullets.add(new Bullet(x + 40, y - 10));
                createdBullets.add(new Bullet(x + 70, y));
            }
        }
        return createdBullets;
    }

    public abstract void draw(Graphics g);

    public int getDamageAgainstBoss() {
        return 1;
    }

    public void updateTimers() {
        if (shieldTimer > 0) shieldTimer--;
        if (rapidFireTimer > 0) rapidFireTimer--;
    }

    public void loseLife() {
        if (shieldTimer > 0) return;
        lives--;
    }

    public void addLife() { lives++; }
    public void upgradeFireLevel() { if (fireLevel < 3) fireLevel++; }
    public void activateRapidFire() { rapidFireTimer = 300; }
    public void activateShield() { shieldTimer = 300; }
    public boolean isShieldActive() { return shieldTimer > 0; }
    public boolean isRapidFireActive() { return rapidFireTimer > 0; }
    public boolean isDead() { return lives <= 0; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getLives() { return lives; }
    public int getFireLevel() { return fireLevel; }
    public String getType() { return type; }
}