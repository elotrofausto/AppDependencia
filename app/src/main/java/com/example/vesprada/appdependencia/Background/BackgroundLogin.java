package com.example.vesprada.appdependencia.Background;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BackgroundLogin extends Thread{

    private static Boolean correctLogin = false;
    private String user;
    private String pass;


    public BackgroundLogin(String user, String pass){
        this.user = user;
        this.pass = pass;
    }

        public void run() {
            try {
                Class.forName("org.postgresql.Driver");
                // "jdbc:postgresql://IP:PUERTO/DB", "USER", "PASSWORD");
                // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                Connection conn = DriverManager.getConnection(
                        //"jdbc:postgresql://149.202.8.235:5432/BDgrup2", "grup2", "Grupo-312");
                        "jdbc:postgresql://10.0.2.2:9999/BDgrup2", "grup2", "Grupo-312");
                //En el stsql se puede agregar cualquier consulta SQL deseada.
                String stsql = "SELECT * FROM x_dependiente_model where persona_id = (SELECT id FROM x_persona_model where dni = '" + user + "') AND password ='" + pass + "'";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(stsql);
                if (rs != null){
                    correctLogin = true;
                }
                conn.close();
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }

        }


    public Boolean getCorrectLogin() {
        return correctLogin;
    }
}

