package com.example.vesprada.appdependencia.Background;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.vesprada.appdependencia.Activities.SplashActivity;
import com.example.vesprada.appdependencia.R;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        private Context context;
        private String user;
        private String pass;
        private boolean correctLogin;
        private ProgressBar progressBar;

        public LoginTask(String user, String pass, Context context) {
            this.user = user;
            this.pass = pass;
            this.context = context;
            this.correctLogin = false;

            //View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
            View rootView = View.inflate(context, R.layout.activity_login, null);
            progressBar = rootView.findViewById(R.id.loginPb);
        }

        //Runs in background Thread
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Class.forName("org.postgresql.Driver");
                // "jdbc:postgresql://IP:PUERTO/DB", "USER", "PASSWORD");
                // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
                Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://149.202.8.235:5432/BDgrup2", "grup2", "Grupo-312");
                        //"jdbc:postgresql://10.0.2.2:9999/BDgrup2", "grup2", "Grupo-312");
                //En el stsql se puede agregar cualquier consulta SQL deseada.
                String stsql = "SELECT * FROM x_dependiente_model where persona_id = (SELECT id FROM x_persona_model where dni = '" + user + "') AND password ='" + pass + "'";
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery(stsql);
                if (rs.isBeforeFirst()){
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean result){
            progressBar.setVisibility(View.INVISIBLE);
            if (result){
                Toast.makeText(context,"AUTENTICACIÓN CORRECTA",Toast.LENGTH_LONG).show();
                lanzarSplashActivity();
            }else{
                Toast.makeText(context,"ERROR DE AUTENTICACIÖN",Toast.LENGTH_LONG).show();
            }
        }


        @Override
        protected void onCancelled(){
            super.onCancelled();
            Log.e("onCancelled", "ASYNCTASK " + this.getClass().getSimpleName() + ": I've been canceled and ready to GC clean");
        }

        private void lanzarSplashActivity(){
            Intent intent = new Intent(context, SplashActivity.class);
            context.startActivity(intent);
        }

    }



}
