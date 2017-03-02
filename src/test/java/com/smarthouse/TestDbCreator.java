package com.smarthouse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDbCreator {

    private static void createDB() {
        Connection c = null;


        try {
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "rjcvjc6");
            Statement statement = c.createStatement();
            statement.executeUpdate("CREATE DATABASE testdb");
        } catch (SQLException e) {
            try {
                dropDB();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            createDB();
        }
        finally {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void dropDB() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "rjcvjc6");
        Statement statement = c.createStatement();
        try {
            statement.executeUpdate("DROP DATABASE testdb");
        } catch (SQLException e) {
            createDB();
        }
    }

    public static void main(String[] args) throws SQLException {
        //createDB();
        //dropDB();
    }
}
