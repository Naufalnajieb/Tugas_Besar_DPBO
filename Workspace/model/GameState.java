/* Saya Naufal Fakhri Al-Najieb mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin. */

package Workspace.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    private String username;
    private int score, count;
    private int playerX, playerY;

    private Image playerImage, backgroundImage, basketImage;
    private List<SkillBall> balls = new ArrayList<>();
    private SkillBall carriedBall;

    private int lassoX, lassoY;
    private boolean lassoActive;
    private LassoState lassoState = LassoState.NONE;
    private SkillBall targetBall;

    public enum LassoState {
        NONE,
        EXTENDING_TO_BALL,
        RETRACTING_TO_PLAYER,
        EXTENDING_TO_BASKET,
        RETRACTING_TO_BASKET
    }

    public GameState(String username, Image playerImage, Image backgroundImage, Image basketImage) {
        this.username = username;
        this.playerImage = playerImage;
        this.backgroundImage = backgroundImage;
        this.basketImage = basketImage;
        this.lassoX = 0;
        this.lassoY = 0;
        this.lassoActive = false;
    }

    // Getters
    public String getUsername() { return username; }
    public int getScore() { return score; }
    public int getCount() { return count; }
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public Image getPlayerImage() { return playerImage; }
    public Image getBackgroundImage() { return backgroundImage; }
    public Image getBasketImage() { return basketImage; }
    public List<SkillBall> getBalls() { return balls; }
    public SkillBall getCarriedBall() { return carriedBall; }

    public int getLassoX() { return lassoX; }
    public int getLassoY() { return lassoY; }
    public boolean isLassoActive() { return lassoActive; }
    public LassoState getLassoState() { return lassoState; }
    public SkillBall getTargetBall() { return targetBall; }

    // Setters
    public void setPlayerX(int x) { this.playerX = x; }
    public void setPlayerY(int y) { this.playerY = y; }
    public void setCarriedBall(SkillBall ball) { this.carriedBall = ball; }
    public void removeCarriedBall() { this.carriedBall = null; }

    public void addScore(int value) { this.score += value; }
    public void addCount() { this.count += 1; }

    public void setLasso(int x, int y) {
        this.lassoX = x;
        this.lassoY = y;
        this.lassoActive = true;
    }

    public void resetLasso() {
        this.lassoX = this.playerX;
        this.lassoY = this.playerY;
        this.lassoActive = false;
        this.targetBall = null;
        this.lassoState = LassoState.NONE;
    }

    public void setLassoState(LassoState state) { this.lassoState = state; }
    public void setTargetBall(SkillBall ball) { this.targetBall = ball; }
}
