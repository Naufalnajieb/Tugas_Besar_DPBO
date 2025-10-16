/* Saya Naufal Fakhri Al-Najieb mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin. */

package Workspace.model;

import java.awt.*;

public class SkillBall {
    private int x, y, score, direction;
    private Image image;

    public SkillBall(int x, int y, int score, Image image, int direction) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.image = image;
        this.direction = direction;
    }

    public void move() {
        x += direction * 3;
    }

    public void draw(Graphics g, int w, int h) {
        g.drawImage(image, x, y, w, h, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 40);
    }

    public void setPosition(int px, int py) {
        this.x = px;
        this.y = py;
    }

    public int getScore() {
        return score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }
}
