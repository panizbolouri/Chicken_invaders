package model;

import java.awt.*;

public abstract class Enemy {
    protected int x, y;
    protected int health;
    protected int scoreValue;
    protected int width = 80;
    protected int height = 80;

    public Enemy(int x, int y, int health, int scoreValue) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.scoreValue = scoreValue;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public abstract void draw(Graphics g);

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isDead() {
        return this.health <= 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getScoreValue() { return scoreValue; }
}