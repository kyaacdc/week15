package com.smarthouse.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbCreator {

    private Connection c;
    private String urlPostgres;
    private String urlDb;
    private String user;
    private String psw;
    private String pathSqlFile;

    public DbCreator(String urlPostgres, String urlDb, String user, String psw, String pathSqlFile) {
        this.urlPostgres = urlPostgres;
        this.urlDb = urlDb;
        this.user = user;
        this.psw = psw;
        this.pathSqlFile = pathSqlFile;
    }

    public void dropCreateDbAndTables() throws SQLException, InterruptedException {
        dropDB();
        createDB();
        createTables();
    }

    public void createDB() throws SQLException {

        c = DriverManager.getConnection(urlPostgres, user, psw);

        try (Statement statement = c.createStatement();){
            statement.executeUpdate("CREATE DATABASE testdb");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
    }

    public void createTables() throws SQLException {

        c = DriverManager.getConnection(urlDb, user, psw);

        String query = "";

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(pathSqlFile))){
            String readline;
            while(( readline = bufferedReader.readLine()) !=null){
                query  = query + readline;
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        Connection conn = DriverManager.getConnection(urlDb, user, psw);
        try (Statement statement = conn.createStatement();){
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
    }

    public void dropDB() throws SQLException {

        c = DriverManager.getConnection(urlPostgres, user, psw);

        try (Statement statement = c.createStatement();) {
            statement.executeUpdate("DROP DATABASE testdb");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("app-config.xml");
        DbCreator dbCreator = (DbCreator) ac.getBean("testDbCreator");
        dbCreator.dropCreateDbAndTables();
    }

}
