package ui;

import database.DatabaseManager;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Plane plane;
    private ArrayList<Bullet> bullets;
    private ArrayList<Cell> cells;
    private ArrayList<Egg> eggs;
    private boolean[] keys;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Image backgroundImage;

    private double gridX = 20;
    private double gridY = -150;
    private int enemyDirection = 1;
    private double gridSpeed = 1.0;
    private int enemyStepDown = 20;

    private int currentLevel = 1;
    private int frameCount = 0;
    private int eggDelayFrames = 180;
    private int score = 0;

    public GamePanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        setFocusable(true);
        addKeyListener(this);

        keys = new boolean[256];
        timer = new Timer(16, this);
        backgroundImage = new ImageIcon("res/background.jpg").getImage();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                plane = new Plane(350, 480);
                bullets = new ArrayList<>();
                cells = new ArrayList<>();
                eggs = new ArrayList<>();
                currentLevel = 1;
                score = 0;
                initLevel();
                requestFocusInWindow();
                timer.start();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                timer.stop();
            }
        });
    }

    private void initLevel() {
        cells.clear();
        bullets.clear();
        eggs.clear();
        gridX = 20;
        gridY = -150;
        enemyDirection = 1;
        frameCount = 0;

        int rows = 5;
        int cols = 8;
        int paddingX = 90;
        int paddingY = 85;
        int cellCounter = 2;

        if (currentLevel == 1) {
            gridSpeed = 1.0; enemyStepDown = 20; eggDelayFrames = 180; cellCounter = 2;
        } else if (currentLevel == 2) {
            gridSpeed = 1.5; enemyStepDown = 20; eggDelayFrames = 120; cellCounter = 2;
        } else if (currentLevel == 3) {
            gridSpeed = 2.0; enemyStepDown = 25; eggDelayFrames = 90; cellCounter = 3;
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int offsetX = c * paddingX;
                int offsetY = r * paddingY;
                String type = determineEnemyType(r);

                Cell cell = new Cell(offsetX, offsetY, cellCounter, type);
                cell.setEnemy(createEnemy(type, (int)gridX + offsetX, (int)gridY + offsetY));
                cells.add(cell);
            }
        }
    }

    private String determineEnemyType(int row) {
        if (currentLevel == 1) return "Normal";
        if (currentLevel == 2) return (row % 2 == 0) ? "Normal" : "Fast";
        if (currentLevel == 3) return (row % 2 == 0) ? "Normal" : "Zigzag";
        return "Normal";
    }

    private Enemy createEnemy(String type, int x, int y) {
        if (type.equals("Fast")) return new FastEnemy(x, y);
        if (type.equals("Zigzag")) return new ZigzagEnemy(x, y);
        return new NormalEnemy(x, y);
    }

    private void handleGameEnd(String message) {
        timer.stop();
        JOptionPane.showMessageDialog(this, message + "\nFinal Score: " + score);

        if (!LoginPanel.loggedInUser.isEmpty()) {
            DatabaseManager.saveGameScore(LoginPanel.loggedInUser, score, currentLevel);
        }

        cardLayout.show(mainPanel, "MainMenu");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dx = 0, dy = 0;
        if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) dx = -1;
        if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) dx = 1;
        if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) dy = -1;
        if (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]) dy = 1;
        plane.move(dx, dy);

        if (keys[KeyEvent.VK_SPACE]) {
            ArrayList<Bullet> newShot = plane.shoot();
            if (!newShot.isEmpty()) {
                bullets.addAll(newShot);
            }
        }

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update();
            boolean hit = false;
            Rectangle bulletBounds = new Rectangle(b.getX(), b.getY(), 20, 60);

            for (Cell cell : cells) {
                Enemy enemy = cell.getEnemy();
                if (enemy != null) {
                    Rectangle enemyBounds = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
                    if (bulletBounds.intersects(enemyBounds)) {
                        enemy.takeDamage(1);
                        if (enemy.isDead()) {
                            score += enemy.getScoreValue();
                            cell.decrementCounter();
                            if (cell.getCounter() > 0) {
                                int spawnX = (Math.random() > 0.5) ? 0 : 800 - 80;
                                cell.setEnemy(createEnemy(cell.getEnemyType(), spawnX, 0));
                                cell.setSpawning(true);
                            } else {
                                cell.setEnemy(null);
                            }
                        }
                        hit = true;
                        break;
                    }
                }
            }
            if (hit || b.isOutOfBounds()) {
                bullets.remove(i);
            }
        }

        gridX += enemyDirection * gridSpeed;
        boolean hitEdge = false;

        for (Cell cell : cells) {
            Enemy enemy = cell.getEnemy();
            if (enemy != null) {
                if (cell.isSpawning()) {
                    double targetX = gridX + cell.getOffsetX();
                    double targetY = gridY + cell.getOffsetY();
                    int cx = enemy.getX();
                    int cy = enemy.getY();
                    int spawnSpeed = 6;

                    if (Math.abs(targetX - cx) <= spawnSpeed && Math.abs(targetY - cy) <= spawnSpeed) {
                        enemy.setX((int)targetX);
                        enemy.setY((int)targetY);
                        cell.setSpawning(false);
                    } else {
                        double angle = Math.atan2(targetY - cy, targetX - cx);
                        enemy.setX((int)(cx + Math.cos(angle) * spawnSpeed));
                        enemy.setY((int)(cy + Math.sin(angle) * spawnSpeed));
                    }
                } else {
                    enemy.setX((int)(gridX + cell.getOffsetX()));
                    enemy.setY((int)(gridY + cell.getOffsetY()));

                    int intendedX = (int)(gridX + cell.getOffsetX());
                    if (intendedX <= 0 && enemyDirection == -1) {
                        hitEdge = true;
                    } else if (intendedX >= 800 - enemy.getWidth() && enemyDirection == 1) {
                        hitEdge = true;
                    }

                    if (enemy.getY() + enemy.getHeight() >= 600) {
                        handleGameEnd("Game Over! The chickens invaded Earth.");
                        return;
                    }
                }
            }
        }

        if (hitEdge) {
            enemyDirection *= -1;
            gridY += enemyStepDown;
        }

        frameCount++;
        if (frameCount >= eggDelayFrames) {
            frameCount = 0;
            ArrayList<Cell> activeCells = new ArrayList<>();
            for (Cell c : cells) {
                if (c.getEnemy() != null && !c.isSpawning()) {
                    activeCells.add(c);
                }
            }
            if (!activeCells.isEmpty()) {
                Cell randomCell = activeCells.get((int)(Math.random() * activeCells.size()));
                Enemy targetEnemy = randomCell.getEnemy();
                eggs.add(new Egg(targetEnemy.getX() + targetEnemy.getWidth() / 2 - 17, targetEnemy.getY() + targetEnemy.getHeight(), 0, 4));
            }
        }

        Rectangle planeBounds = new Rectangle(plane.getX(), plane.getY(), 100, 100);
        for (int i = eggs.size() - 1; i >= 0; i--) {
            Egg egg = eggs.get(i);
            egg.update();
            Rectangle eggBounds = new Rectangle(egg.getX(), egg.getY(), 35, 50);

            if (eggBounds.intersects(planeBounds)) {
                plane.loseLife();
                eggs.remove(i);
                if (plane.isDead()) {
                    handleGameEnd("Game Over!");
                    return;
                }
                continue;
            }
            if (egg.isOutOfBounds()) eggs.remove(i);
        }

        boolean levelClear = true;
        for (Cell c : cells) {
            if (c.getCounter() > 0 || c.getEnemy() != null) {
                levelClear = false;
                break;
            }
        }

        if (levelClear) {
            if (currentLevel < 3) {
                currentLevel++;
                initLevel();
            } else {
                handleGameEnd("Victory! You completed the initial levels!");
                return;
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, 800, 600, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 800, 600);
        }

        plane.draw(g);
        for (Bullet b : bullets) b.draw(g);

        for (Cell cell : cells) {
            if (cell.getEnemy() != null) {
                cell.getEnemy().draw(g);
            }
        }

        for (Egg egg : eggs) egg.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString("PILOT: " + LoginPanel.loggedInUser, 10, 25);
        g.drawString("SCORE: " + score, 10, 50);
        g.drawString("LEVEL: " + currentLevel, 10, 75);
        g.drawString("LIVES: " + plane.getLives(), 10, 100);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code < 256) keys[code] = true;
        if (code == KeyEvent.VK_ESCAPE) {
            keys = new boolean[256];
            cardLayout.show(mainPanel, "MainMenu");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code < 256) keys[code] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
