package com.example.vesprada.appdependencia.Background;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vesprada.appdependencia.DB.DependenciaDBManager;
import com.example.vesprada.appdependencia.DB.PostgresDBConnection;
import com.example.vesprada.appdependencia.Models.XGeoModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveLocationTask extends AsyncTask<Location, Void, Location> {
    private final String MYPREFS = "MyPrefs";
    private final String INSERTGEO = "INSERT INTO x_geolocaliz_model(id_dependiente,latitud,longitud,fecha_hora) VALUES(?,?,?,?)";
    private Context mContext;
    private DependenciaDBManager db;

    SaveLocationTask(Context applicationContext) {
        mContext = applicationContext;
        db = new DependenciaDBManager(mContext);
    }

    @Override
    protected Location doInBackground(Location... locations) {
        Location location = locations[0];
        PostgresDBConnection instance = null;

        //Obtenemos la conexión Postgres
        try {
            instance = PostgresDBConnection.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection conn = instance.getConnection();

        //Guardamos en SQLite la localización
        db.insertGeo(new XGeoModel(new Date(),location.getLatitude(),location.getLongitude()));

        //Subimos los datos a Postgres
        try {
            syncGeo(conn);
        } catch (SQLException e) {
           Log.i("POSTGRESQL","Error al actualizar las coordenadas en PostgreSQL " + e.getMessage());
        }

        return location;
    }

    @Override
    protected void onPostExecute(Location result) {
        super.onPostExecute(result);
        Log.i("LOCATIONSERVICE:", "Location saved at local SQLite DataBase " + result.getLatitude() + " " + result.getLongitude());
    }

    private void syncGeo(Connection conn) throws SQLException {
        //Enviamos los datos recogidos a Postgres
        List<XGeoModel> geoList = new ArrayList<>();
        geoList.addAll(db.getGeoRows(null));

        PreparedStatement updatePst = conn.prepareStatement(INSERTGEO);

        SharedPreferences myPreferences = mContext.getSharedPreferences(MYPREFS, Context.MODE_PRIVATE);

        conn.setAutoCommit(false);
        for (XGeoModel geo: geoList) {
            updatePst.setInt(1, myPreferences.getInt("ID",0));
            updatePst.setDouble(2, geo.getLat());
            updatePst.setDouble(3, geo.getLng());
            updatePst.setTimestamp(4, new Timestamp(geo.getFechaLong()));
            updatePst.addBatch();
        }

        try{
            updatePst.executeBatch();
            conn.commit();
            //Solamente se borraran los registros del SQLite si se ha superado con éxito el commit
            db.deleteGeo(null);
        } catch (SQLException e) {
            Log.i("POSTGRESQL","Error en batch update. Se procede a hacer un rollback()" + e.getMessage());
            conn.rollback();
        }finally {
            conn.setAutoCommit(true);
        }
    }
}