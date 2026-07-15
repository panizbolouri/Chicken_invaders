package model;

public class Cell {
    private int offsetX;
    private int offsetY;
    private int counter;
    private Enemy enemy;
    private boolean isSpawning;
    private String enemyType;

    public Cell(int offsetX, int offsetY, int counter, String enemyType) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.counter = counter;
        this.enemyType = enemyType;
        this.isSpawning = false;
    }

    public int getOffsetX() { return offsetX; }
    public int getOffsetY() { return offsetY; }
    public int getCounter() { return counter; }
    public void decrementCounter() { counter--; }
    public Enemy getEnemy() { return enemy; }
    public void setEnemy(Enemy enemy) { this.enemy = enemy; }
    public boolean isSpawning() { return isSpawning; }
    public void setSpawning(boolean spawning) { this.isSpawning = spawning; }
    public String getEnemyType() { return enemyType; }
}