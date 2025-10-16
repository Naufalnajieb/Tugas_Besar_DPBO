/* Saya Naufal Fakhri Al-Najieb mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin. */


package Workspace.views;

import Workspace.config.DBConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainMenuView extends JFrame {

    private JPanel mainPanel;
    private JTextField usernameField;
    private JTable scoreTable;
    private JButton playButton;
    private JButton quitButton;

    private DBConfig database;
    private DefaultTableModel tableModel;

    public MainMenuView() {
        // Koneksi ke database
        database = new DBConfig();

        // Inisialisasi UI
        setTitle("Collect the Skill Balls - Main Menu");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Komponen GUI
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(255, 250, 240)); // Beige

        JLabel titleLabel = new JLabel("Collect The Skill Balls");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBounds(230, 20, 400, 40);
        mainPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(80, 90, 100, 25);
        mainPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 90, 200, 25);
        mainPanel.add(usernameField);

        playButton = new JButton("Play");
        playButton.setBounds(400, 90, 100, 25);
        mainPanel.add(playButton);

        quitButton = new JButton("Quit");
        quitButton.setBounds(520, 90, 100, 25);
        mainPanel.add(quitButton);

        // Tabel Score
        JLabel scoreLabel = new JLabel("Leaderboard:");
        scoreLabel.setBounds(80, 140, 200, 25);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(scoreLabel);

        scoreTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setBounds(80, 170, 600, 300);
        mainPanel.add(scrollPane);

        // Isi tabel
        updateScoreTable();

        // Action Tombol Quit
        quitButton.addActionListener(e -> System.exit(0));

        // Tombol Play: validasi dan navigasi ke Game
        playButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan username terlebih dahulu!");
                return;
            }

            try {
                ResultSet rs = database.selectQuery("SELECT * FROM thasil WHERE username = '" + username + "'");
                if (!rs.next()) {
                    String sqlInsert = "INSERT INTO thasil VALUES (null, '" + username + "', 0, 0)";
                    database.InsertUpdateDeleteQuery(sqlInsert);
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Gagal insert user: " + ex.getMessage());
            }

            // Tampilkan GameView
            GameView game = new GameView(username);
            game.setVisible(true);

            // Sembunyikan menu
            this.setVisible(false);
        });


        // Set panel sebagai konten utama
        setContentPane(mainPanel);
    }

    public void updateScoreTable() {
        String[] columns = {"No", "Username", "Score", "Count"};
        tableModel = new DefaultTableModel(null, columns);

        try {
            ResultSet rs = database.selectQuery("SELECT * FROM thasil");
            int i = 1;
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = i++;
                row[1] = rs.getString("username");
                row[2] = rs.getInt("score");
                row[3] = rs.getInt("count");
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memuat data leaderboard: " + e.getMessage());
        }

        scoreTable.setModel(tableModel);
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public void resetForm() {
        usernameField.setText("");
    }
}
