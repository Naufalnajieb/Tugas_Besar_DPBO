/* Saya Naufal Fakhri Al-Najieb mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin. */

package Workspace.viewmodel;

import Workspace.config.DBConfig;
import Workspace.model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class GameLogic {
    private GameState state;
    private Image[] ballImages;
    private int[] scores = {90, 70, 50, 30, 20, 10};
    private Random rand = new Random();
    private DBConfig db;
    private Rectangle basketHitbox;

    public GameLogic(String username) {
        try {
            Image player = ImageIO.read(new File("Workspace/assets/Player.png"));
            Image bg = ImageIO.read(new File("Workspace/assets/Background2.png"));
            Image basket = ImageIO.read(new File("Workspace/assets/Garbage_bascket.png"));
            state = new GameState(username, player, bg, basket);

            ballImages = new Image[6];
            for (int i = 0; i < 6; i++) {
                ballImages[i] = ImageIO.read(new File("Workspace/assets/Sampah" + (i + 1) + ".png"));
            }

            basketHitbox = new Rectangle(1050, 300, 200, 200);

            state.setPlayerX((1280 - 320) / 2);
            state.setPlayerY((720 - 320) / 2);
        } catch (Exception e) {
            throw new RuntimeException("Gagal load image: " + e.getMessage());
        }

        db = new DBConfig();
    }

    public void handleKeyPress(KeyEvent e) {
        int step = 10;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> state.setPlayerX(state.getPlayerX() - step);
            case KeyEvent.VK_RIGHT -> state.setPlayerX(state.getPlayerX() + step);
            case KeyEvent.VK_UP -> state.setPlayerY(state.getPlayerY() - step);
            case KeyEvent.VK_DOWN -> state.setPlayerY(state.getPlayerY() + step);
        }
    }

    public void handleMouseClick(int x, int y) {
        if (state.getLassoState() != GameState.LassoState.NONE) return;

        if (state.getCarriedBall() == null) {
            for (SkillBall ball : state.getBalls()) {
                if (ball.getBounds().contains(x, y)) {
                    state.setTargetBall(ball);
                    state.setLasso(state.getPlayerX(), state.getPlayerY());
                    state.setLassoState(GameState.LassoState.EXTENDING_TO_BALL);
                    return;
                }
            }
        } else {
            if (basketHitbox.contains(x, y)) {
                state.setLasso(state.getPlayerX(), state.getPlayerY());
                state.setLassoState(GameState.LassoState.EXTENDING_TO_BASKET);
            }
        }
    }

    public void update() {
        switch (state.getLassoState()) {
            case EXTENDING_TO_BALL -> {
                SkillBall target = state.getTargetBall();
                if (target != null) {
                    approachLassoTo(target.getX(), target.getY());
                    if (lassoReached(target.getX(), target.getY())) {
                        state.setCarriedBall(target);
                        state.getBalls().remove(target);
                        state.setLassoState(GameState.LassoState.RETRACTING_TO_PLAYER);
                    }
                }
            }

            case RETRACTING_TO_PLAYER -> {
                approachLassoTo(state.getPlayerX(), state.getPlayerY());
                if (lassoReached(state.getPlayerX(), state.getPlayerY())) {
                    state.resetLasso();
                    state.setLassoState(GameState.LassoState.NONE);
                }
            }

            case EXTENDING_TO_BASKET -> {
                int bx = basketHitbox.x + basketHitbox.width / 2;
                int by = basketHitbox.y + basketHitbox.height / 2;
                approachLassoTo(bx, by);
                if (lassoReached(bx, by)) {
                    if (state.getCarriedBall() != null) {
                        state.addScore(state.getCarriedBall().getScore());
                        state.addCount();
                        state.removeCarriedBall();
                    }
                    state.setLassoState(GameState.LassoState.RETRACTING_TO_BASKET);
                }
            }

            case RETRACTING_TO_BASKET -> {
                approachLassoTo(state.getPlayerX(), state.getPlayerY());
                if (lassoReached(state.getPlayerX(), state.getPlayerY())) {
                    state.resetLasso();
                    state.setLassoState(GameState.LassoState.NONE);
                }
            }


            case NONE -> {
                // Normal ball spawn and movement
                if (rand.nextInt(60) == 0) {
                    int i = rand.nextInt(6);
                    int riverIndex = rand.nextInt(2);
                    int y = (riverIndex == 0) ? 200 : 600;
                    int x = (riverIndex == 0) ? 0 : 1280;
                    int dir = (riverIndex == 0) ? 1 : -1;
                    SkillBall ball = new SkillBall(x, y, scores[i], ballImages[i], dir);
                    state.getBalls().add(ball);
                }

                for (SkillBall ball : state.getBalls()) {
                    ball.move();
                }
            }
        }

        if (state.getCarriedBall() != null) {
            state.getCarriedBall().setPosition(state.getPlayerX(), state.getPlayerY());
        }
    }

    private void approachLassoTo(int targetX, int targetY) {
        int lx = state.getLassoX();
        int ly = state.getLassoY();
        int dx = targetX - lx;
        int dy = targetY - ly;
        int step = 30;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < step) {
            state.setLasso(targetX, targetY);
        } else {
            state.setLasso(lx + (int) (dx / distance * step), ly + (int) (dy / distance * step));
        }
    }

    private boolean lassoReached(int tx, int ty) {
        return Math.abs(state.getLassoX() - tx) < 10 && Math.abs(state.getLassoY() - ty) < 10;
    }

    public void render(Graphics g) {
        g.drawImage(state.getBackgroundImage(), 0, 0, 1280, 720, null);
        g.drawImage(state.getBasketImage(), 1050, 300, 200, 200, null);

        for (SkillBall ball : state.getBalls()) {
            ball.draw(g, 48, 48);
        }

        if (state.getCarriedBall() != null) {
            state.getCarriedBall().draw(g, 48, 48);
        }

        g.drawImage(state.getPlayerImage(), state.getPlayerX(), state.getPlayerY(), 320, 320, null);

        if (state.getLassoState() != GameState.LassoState.NONE) {
            g.setColor(Color.RED);
            g.drawLine(state.getPlayerX() + 160, state.getPlayerY() + 160, state.getLassoX(), state.getLassoY());
        }

        g.setColor(Color.BLACK);
        g.drawString("Username: " + state.getUsername(), 20, 20);
        g.drawString("Score: " + state.getScore(), 20, 40);
        g.drawString("Count: " + state.getCount(), 20, 60);
    }

    public void saveToDatabase() {
        try {
            ResultSet rs = db.selectQuery("SELECT * FROM thasil WHERE username = '" + state.getUsername() + "'");
            if (rs.next()) {
                int newScore = state.getScore() + rs.getInt("score");
                int newCount = state.getCount() + rs.getInt("count");
                db.InsertUpdateDeleteQuery("UPDATE thasil SET score = " + newScore + ", count = " + newCount +
                        " WHERE username = '" + state.getUsername() + "'");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan skor: " + e.getMessage());
        }
    }
}
