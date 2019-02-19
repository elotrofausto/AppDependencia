package com.example.vesprada.appdependencia.Background;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class PanicButton {




//    static class for AsyncTask
//--------------------------------------------------------------------------------------------------
    public static class PanicButtonTask extends AsyncTask<Void, Void, Boolean> {

        private final static int PORT = 4444;
        private final static int TIMEOUT = 35000;
        private final static String DNI = "dni";
        private final static String NONE = "none";
        private final static String CORRECT_STATUS = "1";


        //Weak reference
        private final WeakReference<Activity> activityWeakReference;

        private Socket clientSocket;
        private SharedPreferences sharedPreferences;

        public PanicButtonTask(Activity caller, SharedPreferences sharedPreferences) {
            activityWeakReference = new WeakReference<>(caller);
            this.sharedPreferences = sharedPreferences;
        }

        //Runs in background Thread
        @Override
        protected Boolean doInBackground(Void... params) {
            //TODO conectar mediante socket a Asistente asignado o a todos progresivamente
            try {
                InetSocketAddress inet = new InetSocketAddress("172.16.226.14",PORT);
                clientSocket = new Socket();
                clientSocket.connect(inet);
                clientSocket.setSoTimeout(TIMEOUT);

                JSONObject jsonAlarm = createJson();

                String reply = null;
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                do {

                    bw.write(jsonAlarm.toString());
                    bw.newLine();
                    bw.flush();

                    reply = br.readLine();
                    Log.i("SOCKET", "Respuesta recibida");

                }while (!reply.equals(CORRECT_STATUS));
                Log.i("SOCKET", "Alarma aceptada");
                bw.close();
                clientSocket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result){
            //TODO darle confirmación de la ayuda al dependiente
        }


        @Override
        protected void onCancelled(){
            super.onCancelled();
           //TODO desactivar alarma
        }

        private JSONObject createJson() throws JSONException {
            JSONObject jsonAlarm = new JSONObject();
            jsonAlarm.accumulate("idDependiente",sharedPreferences.getString(DNI,NONE));
            jsonAlarm.accumulate("fecha",new Date().getTime());
            return  jsonAlarm;
        }
    }
}
