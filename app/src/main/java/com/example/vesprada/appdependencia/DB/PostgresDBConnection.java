package com.example.vesprada.appdependencia.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDBConnection {

    //Se gestiona la conexión a Postgres con esta clase singleton.
    private Connection connection;
    private static PostgresDBConnection instance = new PostgresDBConnection();

    public Connection getConnection() {
        return connection;
    }

    private PostgresDBConnection(){
        connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://149.202.8.235:5432/BDgrup2", "grup2", "Grupo-312");
            //"jdbc:postgresql://10.0.2.2:9999/BDgrup2", "grup2", "Grupo-312");
        } catch (SQLException e) {
            System.out.println("Database Connection Creation Failed : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static PostgresDBConnection getInstance() throws SQLException {
            if (instance == null) {
                instance = new PostgresDBConnection();
            } else if (instance.getConnection() == null || instance.getConnection().isClosed()) {
                instance = new PostgresDBConnection();
            }
        return instance;
    }
}
