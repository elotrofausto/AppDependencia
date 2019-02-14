package com.example.vesprada.appdependencia.Background;

import android.os.AsyncTask;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AsyncTasks {

    //static class for AsyncTask
    public static class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private final int CONNECTION_TIMEOUT = 15000;
        private final int READ_TIMEOUT = 10000;
        private String user;
        private String pass;
        private Boolean correctLogin;

        public LoginTask(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        //Runs in background Thread
        @Override
        protected Boolean doInBackground(Void... params) {
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
                }else {
                    correctLogin = false;
                }
                conn.close();
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }

            return correctLogin;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean searchResult){
            //pb.setVisibility(View.INVISIBLE);
        }


        @Override
        protected void onCancelled(){
            super.onCancelled();
            Log.e("onCancelled", "ASYNCTASK " + this.getClass().getSimpleName() + ": I've been canceled and ready to GC clean");
        }



    }

}
