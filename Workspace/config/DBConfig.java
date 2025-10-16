/* Saya Naufal Fakhri Al-Najieb mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin. */

package Workspace.config;
import java.sql.*;

public class DBConfig {
    private Connection connection;
    private Statement statement;

    public DBConfig(){
        try {
            // Disesuaikan dengan nama database untuk game ini
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_collect_skill", "root", "");
            statement = connection.createStatement();
        }
        catch (SQLException e){
            throw new RuntimeException("Koneksi ke database gagal: " + e.getMessage());
        }
    }

    public ResultSet selectQuery(String sql){
        try {
            statement.executeQuery(sql);
            return statement.getResultSet();
        } catch (SQLException e) {
            throw new RuntimeException("Kesalahan SELECT query: " + e.getMessage());
        }
    }

    public int InsertUpdateDeleteQuery(String sql){
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Kesalahan INSERT/UPDATE/DELETE query: " + e.getMessage());
        }
    }

    public Statement getStatement(){
        return statement;
    }
}