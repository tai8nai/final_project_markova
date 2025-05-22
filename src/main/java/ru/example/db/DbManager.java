package ru.example.db;

import java.sql.*;

public class DbManager {
    private final String url = "jdbc:sqlite:sports_objects.db";
    private Connection conn;

    public DbManager() throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    public void createTables() throws SQLException {
        String sql1 = "CREATE TABLE IF NOT EXISTS region (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE NOT NULL" +
                ")";
        String sql2 = "CREATE TABLE IF NOT EXISTS sports_object (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "type TEXT," +
                "address TEXT," +
                "date TEXT," +
                "region_id INTEGER," +
                "FOREIGN KEY(region_id) REFERENCES region(id)" +
                ")";
        try (Statement st = conn.createStatement()) {
            st.execute(sql1);
            st.execute(sql2);
        }
    }

    public int getOrInsertRegion(String region) throws SQLException {
        String sel = "SELECT id FROM region WHERE name=?";
        String ins = "INSERT OR IGNORE INTO region(name) VALUES(?)";
        try (PreparedStatement psSel = conn.prepareStatement(sel);
             PreparedStatement psIns = conn.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            // Try select
            psSel.setString(1, region);
            ResultSet rs = psSel.executeQuery();
            if (rs.next()) return rs.getInt("id");
            // Else insert
            psIns.setString(1, region);
            psIns.executeUpdate();
            ResultSet gen = psIns.getGeneratedKeys();
            if (gen.next()) return gen.getInt(1);
            // If already exists (because of IGNORE), select again
            rs = psSel.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        throw new SQLException("Cannot insert/select region: " + region);
    }

    public void insertSportsObject(int id, String name, String type, String address, String date, int regionId) throws SQLException {
        String sql = "INSERT INTO sports_object(id, name, type, address, date, region_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, type);
            ps.setString(4, address);
            ps.setString(5, date);
            ps.setInt(6, regionId);
            ps.executeUpdate();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}