package com.example.vesprada.appdependencia.Background;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vesprada.appdependencia.Activities.SplashLoadingActivity;
import com.example.vesprada.appdependencia.DB.DependenciaDBContract;
import com.example.vesprada.appdependencia.DB.DependenciaDBManager;
import com.example.vesprada.appdependencia.DB.PostgresDBConnection;
import com.example.vesprada.appdependencia.Models.XAvisoModel;
import com.example.vesprada.appdependencia.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SyncDBTask extends AsyncTask<Void, Void, Boolean> {

    private static final String GETAVISOS = "SELECT * FROM x_aviso_model WHERE id_dependiente = (SELECT id FROM x_dependiente_model where persona_id = (SELECT id FROM x_persona_model where dni = ?) AND password =?) AND recibido != true";
    private static final String UPDATERECEIVED = "UPDATE x_aviso_model SET recibido = True WHERE id = ?";
    private static final String UPDATEFINISHED = "UPDATE x_aviso_model SET tomas = tomas - 1 WHERE id = ?";

    private static final String SYNCTAG = "SYNCBDSERVICE";
    private DependenciaDBManager db;
    private Context context;
    private String user;
    private String pass;


    public SyncDBTask(String user, String pass, Context context){
        this.user = user;
        this.pass = pass;
        this.context = context;
        this.db = new DependenciaDBManager(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            PostgresDBConnection instance = PostgresDBConnection.getInstance();
            Connection conn = instance.getConnection();
            //Primero actualizamos los avisos finalizados desde nuestra SQlite a Postgres Server
            SQLiteToServer(conn);
            //Inmediatamente traemos los nuevos avisos desde el Server a nuestro SQLite
            pgServerToSQLite(conn);
            conn.close();
        } catch (SQLException se) {
            System.out.println("oops! No se puede conectar. Error: " + se.toString());
        }
        return true;
    }

    private void pgServerToSQLite(Connection conn) throws SQLException{
        List<Integer> idList = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement(GETAVISOS);
        pst.setString(1,user);
        pst.setString(2,pass);
        ResultSet rs = pst.executeQuery();

        //Traemos los resultados obtenidos de nuestro servidor PostgreSQL
        if (rs != null){
            while (rs.next()) {
                System.out.println("SYNC:" + rs.getString(3));
                for (int i = 0; i<rs.getInt(rs.findColumn("tomas")); i++) {
                    db.insertAviso(new XAvisoModel(
                            rs.getInt(rs.findColumn("id")),
                            rs.getInt(rs.findColumn("id")),
                            user,
                            rs.getString(rs.findColumn("tipo")),
                            rs.getString(rs.findColumn("name")),
                            rs.getDate(rs.findColumn("fec_desde")),
                            rs.getDate(rs.findColumn("fec_hasta")),
                            rs.getString(rs.findColumn("periodicidad"))
                    ));
                }
                    idList.add(rs.getInt(rs.findColumn("id"))); //Guardamos los ids de los avisos que se han insertado en nuestra SQLite
            }
        }

        //Actualizamos el estado de los avisos recibidos correctamente a recibido = True en PostgreSQL
        for (Integer num: idList) {
            PreparedStatement updatePst = conn.prepareStatement(UPDATERECEIVED);
            updatePst.setInt(1,num);
            updatePst.executeUpdate();
        }
        if (!idList.isEmpty() && isAppRunning(context,context.getPackageName())){
            //Notificamos al usuario de que se han recibido nuevos avisos (solamente si la app no esta activa: isAppRunning)
            sendNotification();
        }
        Log.i(SYNCTAG,"Avisos sincronizados en la base de datos local SQLite");
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, SplashLoadingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_sync_black_24dp);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
            Notification notification = new Notification.Builder(context,
                   context.getResources().getString(R.string.notification_id_channel))
                    .setContentTitle(context.getString(R.string.NewNotifications))
                    .setContentText(context.getString(R.string.CheckApp))
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.ic_sync_black_24dp)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .build();
            try{
                if (notificationManager != null) {
                    notificationManager.notify(0, notification);
                }
            }catch (Exception e){
                Log.i("NOTIFICATION",e.getMessage());
            }
    }

    public boolean isAppRunning(final Context context, final String packageName) {
        //Método para comprobar si la app está corriendo en este momento
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void SQLiteToServer(Connection conn) throws SQLException {
        //Actualizamos los avisos finalizados en Postgres para que los asistentes tengan constancia
        PreparedStatement updatePst = conn.prepareStatement(UPDATEFINISHED);
        List<XAvisoModel> listaAcabados = db.getAvisoRows(DependenciaDBContract.Aviso.FINALIZADO + " = 1");

        conn.setAutoCommit(false);
        for (XAvisoModel aviso: listaAcabados) {
            int id = aviso.getAvisoID();
            updatePst.setInt(1,id);
            updatePst.addBatch();
            db.setAvisoSynced(aviso.getId());
        }

        try{
            updatePst.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            Log.i("POSTGRESQL","Error en batch update. Se procede a hacer un rollback()" + e.getMessage());
            conn.rollback();
            rollBackSQLite(listaAcabados);
        }finally {
            conn.setAutoCommit(true);
        }

        Log.i(SYNCTAG,"Avisos sincronizados en la base de datos remota Postgres");
    }

    private void rollBackSQLite(List<XAvisoModel> listaMarcados){
        for (XAvisoModel aviso: listaMarcados) {
            //Si hay rollback devolvemos los avisos a estado finalizado, pero no sincronizado con Postgres
            db.setAvisoFinished(aviso.getId());
        }
    }

}

