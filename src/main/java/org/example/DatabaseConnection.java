
package com.scc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:voting_system.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String usersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                roles TEXT NOT NULL DEFAULT 'VOTER',
                status TEXT NOT NULL DEFAULT 'PENDING'
            )
            """;

        String candidatesTable = """
            CREATE TABLE IF NOT EXISTS candidates (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                position TEXT NOT NULL,
                party TEXT
            )
            """;

        String votesTable = """
            CREATE TABLE IF NOT EXISTS votes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL UNIQUE,
                candidate_id INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (candidate_id) REFERENCES candidates(id)
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute(usersTable);
            stmt.execute(candidatesTable);
            stmt.execute(votesTable);

            System.out.println("Voting System database initialized successfully!");

        } catch (SQLException e) {
            System.out.println("Database setup failed: " + e.getMessage());
        }
    }
}
