package com.example.vesprada.appdependencia.Background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vesprada.appdependencia.DB.DependenciaDBContract;
import com.example.vesprada.appdependencia.DB.DependenciaDBManager;
import com.example.vesprada.appdependencia.DB.PostgresDBConnection;
import com.example.vesprada.appdependencia.Models.XAvisoModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SyncDBTask extends AsyncTask<Void, Void, Boolean> {

    private static final String GETAVISOS = "SELECT * FROM x_aviso_model WHERE id_dependiente = (SELECT id_dependiente FROM x_dependiente_model where persona_id = (SELECT id FROM x_persona_model where dni = ?) AND password =?) AND recibido != true";
    private static final String UPDATERECEIVED = "UPDATE x_aviso_model SET recibido = True WHERE id = ?";
    private static final String UPDATEFINISHED = "UPDATE x_aviso_model SET tomas = tomas - 1 WHERE id = ?";

    private static final String SYNCTAG = "SYNCBDSERVICE";
    private static Boolean correctSync = false;
    private DependenciaDBManager db;
    private String user;
    private String pass;


    public SyncDBTask(String user, String pass, Context context){
        this.user = user;
        this.pass = pass;
        this.db = new DependenciaDBManager(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            PostgresDBConnection instance = PostgresDBConnection.getInstance();
            Connection conn = instance.getConnection();
            SQLiteToServer(conn);
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
        Log.i(SYNCTAG,"Avisos sincronizados en la base de datos local SQLite");
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

