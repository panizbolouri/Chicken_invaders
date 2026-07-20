package ui;

import database.DatabaseManager;
import model.*;
import sound.SoundManager;
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
    private ArrayList<PowerUp> powerUps;
    private ArrayList<Explosion> explosions;
    private Enemy currentBoss;
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
    private int bossEggTimer = 0;
    private int freezeTimer = 0;
    private int score = 0;
    private boolean isPaused = false;
    private int levelTransitionTimer = 0;

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
                String equippedPlane = DatabaseManager.getEquippedPlane(LoginPanel.loggedInUser);
                if (equippedPlane.equals("Fast")) {
                    plane = new FastPlane(350, 480);
                } else if (equippedPlane.equals("Heavy")) {
                    plane = new HeavyPlane(350, 480);
                } else if (equippedPlane.equals("Sniper")) {
                    plane = new SniperPlane(350, 480);
                } else {
                    plane = new DefaultPlane(350, 480);
                }

                bullets = new ArrayList<>();
                cells = new ArrayList<>();
                eggs = new ArrayList<>();
                powerUps = new ArrayList<>();
                explosions = new ArrayList<>();
                currentBoss = null;
                currentLevel = 1;
                freezeTimer = 0;
                levelTransitionTimer = 20;
                score = 0;
                isPaused = false;
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
        powerUps.clear();
        if (explosions != null) explosions.clear();
        gridX = 20;
        gridY = -150;
        enemyDirection = 1;
        frameCount = 0;
        bossEggTimer = 0;

        if (currentLevel == 4) {
            currentBoss = new BossLevel4(300, 50, 50);
            return;
        } else if (currentLevel == 8) {
            currentBoss = new BossLevel8(275, 50, 100);
            return;
        }

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
        } else if (currentLevel == 5) {
            gridSpeed = 2.0; enemyStepDown = 25; eggDelayFrames = 60; cellCounter = 3;
        } else if (currentLevel == 6) {
            gridSpeed = 3.0; enemyStepDown = 30; eggDelayFrames = 48; cellCounter = 4;
        } else if (currentLevel == 7) {
            gridSpeed = 3.5; enemyStepDown = 30; eggDelayFrames = 42; cellCounter = 4;
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
        if (currentLevel == 5) return (row % 2 == 0) ? "Shooter" : "Fast";
        if (currentLevel == 6) return (row % 2 == 0) ? "Zigzag" : "Shooter";
        if (currentLevel == 7) {
            int rand = (int)(Math.random() * 4);
            if (rand == 0) return "Normal";
            if (rand == 1) return "Fast";
            if (rand == 2) return "Zigzag";
            return "Shooter";
        }
        return "Normal";
    }

    private Enemy createEnemy(String type, int x, int y) {
        if (type.equals("Fast")) return new FastEnemy(x, y);
        if (type.equals("Zigzag")) return new ZigzagEnemy(x, y);
        if (type.equals("Shooter")) return new ShooterEnemy(x, y);
        return new NormalEnemy(x, y);
    }

    private void handleGameEnd(String message) {
        timer.stop();
        SoundManager.stopMusic();

        if (message.contains("Victory")) {
            SoundManager.playMusic("res/victory.wav", 6.0f);
        } else {
            SoundManager.playMusic("res/gameover.wav", 6.0f);
        }

        JOptionPane.showMessageDialog(this, message + "\nFinal Score: " + score);

        if (!LoginPanel.loggedInUser.isEmpty()) {
            DatabaseManager.saveGameScore(LoginPanel.loggedInUser, score, currentLevel);
        }

        SoundManager.playMusic("res/music.wav");
        cardLayout.show(mainPanel, "MainMenu");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPaused) {
            repaint();
            return;
        }

        if (freezeTimer > 0) freezeTimer--;
        plane.updateTimers();

        int dx = 0, dy = 0;
        if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) dx = -1;
        if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) dx = 1;
        if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) dy = -1;
        if (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]) dy = 1;
        plane.move(dx, dy);
        if (levelTransitionTimer > 0) {
            levelTransitionTimer--;
            repaint();
            return;
        }

        if (keys[KeyEvent.VK_SPACE]) {
            ArrayList<Bullet> newShot = plane.shoot();
            if (!newShot.isEmpty()) {
                SoundManager.playSFX("res/shoot.wav");
                bullets.addAll(newShot);
            }
        }

        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion ex = explosions.get(i);
            ex.update();
            if (ex.isFinished()) explosions.remove(i);
        }

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update();
            boolean hit = false;
            Rectangle bulletBounds = new Rectangle(b.getX(), b.getY(), 20, 60);

            if (currentBoss != null) {
                Rectangle bossBounds = new Rectangle(currentBoss.getX(), currentBoss.getY(), currentBoss.getWidth(), currentBoss.getHeight());
                if (bulletBounds.intersects(bossBounds)) {
                    currentBoss.takeDamage(plane.getDamageAgainstBoss());
                    if (currentBoss.isDead()) {
                        SoundManager.playSFX("res/hit.wav");
                        explosions.add(new Explosion(currentBoss.getX() + currentBoss.getWidth() / 2, currentBoss.getY() + currentBoss.getHeight() / 2, 80));
                        score += currentBoss.getScoreValue();
                        currentBoss = null;
                        if (currentLevel == 8) {
                            handleGameEnd("Victory! You saved the galaxy!");
                            return;
                        } else {
                            currentLevel++;
                            initLevel();
                            levelTransitionTimer = 60;
                            return;
                        }
                    }
                    hit = true;
                }
            } else {
                for (Cell cell : cells) {
                    Enemy enemy = cell.getEnemy();
                    if (enemy != null) {
                        Rectangle enemyBounds = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
                        if (bulletBounds.intersects(enemyBounds)) {
                            enemy.takeDamage(1);
                            if (enemy.isDead()) {
                                SoundManager.playSFX("res/hit.wav");
                                explosions.add(new Explosion(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2, 40));
                                score += enemy.getScoreValue();
                                if (Math.random() < 0.15) {
                                    int pType = (int)(Math.random() * 5);
                                    powerUps.add(new PowerUp(enemy.getX() + 20, enemy.getY() + 20, pType));
                                }

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
            }
            if (hit || b.isOutOfBounds()) {
                bullets.remove(i);
            }
        }

        Rectangle planeBounds = new Rectangle(plane.getX(), plane.getY(), 100, 100);
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            p.update();
            Rectangle pBounds = new Rectangle(p.getX(), p.getY(), 40, 40);

            if (pBounds.intersects(planeBounds)) {
                int type = p.getType();
                if (type == 0) plane.upgradeFireLevel();
                else if (type == 1) plane.activateRapidFire();
                else if (type == 2) plane.addLife();
                else if (type == 3) plane.activateShield();
                else if (type == 4) freezeTimer = 180;
                powerUps.remove(i);
            } else if (p.isOutOfBounds()) {
                powerUps.remove(i);
            }
        }

        if (freezeTimer == 0) {
            if (currentBoss != null) {
                currentBoss.move(0, 0);
                bossEggTimer++;

                if (currentLevel == 4 && bossEggTimer >= 90) {
                    bossEggTimer = 0;
                    int bx = currentBoss.getX() + currentBoss.getWidth() / 2 - 17;
                    int by = currentBoss.getY() + currentBoss.getHeight() / 2;
                    eggs.add(new Egg(bx, by, 0, -4));
                    eggs.add(new Egg(bx, by, 0, 4));
                    eggs.add(new Egg(bx, by, -4, 0));
                    eggs.add(new Egg(bx, by, 4, 0));
                } else if (currentLevel == 8 && bossEggTimer >= 60) {
                    bossEggTimer = 0;
                    int bx = currentBoss.getX() + currentBoss.getWidth() / 2 - 17;
                    int by = currentBoss.getY() + currentBoss.getHeight() / 2;
                    for (int i = 0; i < 8; i++) {
                        double angle = Math.toRadians(i * 45);
                        eggs.add(new Egg(bx, by, Math.cos(angle) * 5, Math.sin(angle) * 5));
                    }
                }
            } else {
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

                            int intendedX = enemy.getX();
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
                    if (gridX < -50) gridX = -50;
                    if (gridX > 400) gridX = 400;
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

                        if (targetEnemy instanceof ShooterEnemy && Math.random() < 0.5) {
                            double angle = Math.atan2(plane.getY() - targetEnemy.getY(), plane.getX() - targetEnemy.getX());
                            eggs.add(new EnemyBullet(targetEnemy.getX() + targetEnemy.getWidth() / 2, targetEnemy.getY() + targetEnemy.getHeight() / 2, Math.cos(angle) * 5, Math.sin(angle) * 5));
                        } else {
                            eggs.add(new Egg(targetEnemy.getX() + targetEnemy.getWidth() / 2 - 17, targetEnemy.getY() + targetEnemy.getHeight(), 0, 4));
                        }
                    }
                }
            }

            for (int i = eggs.size() - 1; i >= 0; i--) {
                Egg egg = eggs.get(i);
                egg.update();
                Rectangle eggBounds = new Rectangle(egg.getX(), egg.getY(), 35, 50);

                if (eggBounds.intersects(planeBounds)) {
                    SoundManager.playSFX("res/plane_hit.wav");
                    explosions.add(new Explosion(plane.getX() + 50, plane.getY() + 50, 60));
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
        }

        if (currentBoss == null) {
            boolean levelClear = true;
            for (Cell c : cells) {
                if (c.getCounter() > 0 || c.getEnemy() != null) {
                    levelClear = false;
                    break;
                }
            }
            if (levelClear) {
                if (currentLevel < 8) {
                    currentLevel++;
                    initLevel();
                    levelTransitionTimer = 35;
                } else {
                    handleGameEnd("Victory! You saved the galaxy!");
                    return;
                }
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

        if (currentBoss != null) {
            currentBoss.draw(g);
        } else {
            for (Cell cell : cells) {
                if (cell.getEnemy() != null) {
                    cell.getEnemy().draw(g);
                }
            }
        }

        for (Egg egg : eggs) egg.draw(g);
        for (PowerUp p : powerUps) p.draw(g);
        for (Explosion ex : explosions) ex.draw(g);

        if (freezeTimer > 0) {
            g.setColor(new Color(0, 200, 255, 100));
            g.fillRect(0, 0, 800, 600);
        }

        if (levelTransitionTimer > 0) {
            g.setColor(new Color(150, 0, 200, 100));
            g.fillRect(0, 0, 800, 600);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Monospaced", Font.BOLD, 60));
            String text = "LEVEL " + currentLevel;
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g.drawString(text, (800 - textWidth) / 2, 300);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString("PILOT: " + LoginPanel.loggedInUser, 10, 25);
        g.drawString("SCORE: " + score, 10, 50);
        g.drawString("LEVEL: " + currentLevel, 10, 75);
        g.drawString("LIVES: " + plane.getLives(), 10, 100);
        g.drawString("FIRE POWER: " + plane.getFireLevel(), 10, 125);

        int indicatorY = 25;
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        if (plane.isShieldActive()) {
            g.setColor(Color.GREEN);
            g.drawString("[SHIELD ACTIVE]", 620, indicatorY);
            indicatorY += 20;
        }
        if (plane.isRapidFireActive()) {
            g.setColor(Color.ORANGE);
            g.drawString("[RAPID FIRE]", 620, indicatorY);
            indicatorY += 20;
        }
        if (freezeTimer > 0) {
            g.setColor(Color.CYAN);
            g.drawString("[ENEMIES FROZEN]", 620, indicatorY);
        }
        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 800, 600);
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Monospaced", Font.BOLD, 36));
            g.drawString("GAME PAUSED", 265, 270);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 18));
            g.drawString("Press 'P' to Resume", 285, 310);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_P) {
            isPaused = !isPaused;
            return;
        }

        if (code == KeyEvent.VK_M) {
            isPaused = true;
            cardLayout.show(mainPanel, "SettingsPage");
            return;
        }

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