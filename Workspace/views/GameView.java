/* Saya Naufal Fakhri Al-Najieb mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin. */


package Workspace.views;

import Workspace.viewmodel.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameView extends JFrame {
    private GamePanel gamePanel;
    private GameLogic gameLogic;

    public GameView(String username) {
        setTitle("Collect the Skill Balls");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        gameLogic = new GameLogic(username);
        gamePanel = new GamePanel(gameLogic);
        add(gamePanel);

        Timer timer = new Timer(16, e -> {
            gameLogic.update();
            gamePanel.repaint();
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameLogic.handleKeyPress(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    gameLogic.saveToDatabase();
                }
            }
        });

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gameLogic.handleMouseClick(e.getX(), e.getY());
            }
        });

        setFocusable(true);
        setVisible(true);
    }

    static class GamePanel extends JPanel {
        private GameLogic gameLogic;

        public GamePanel(GameLogic gameLogic) {
            this.gameLogic = gameLogic;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            gameLogic.render(g);
        }
    }
}
