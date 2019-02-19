package com.example.vesprada.appdependencia.Background;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveLocationTask extends AsyncTask<Location, Void, Location> {
    private final String TAG = SaveLocationTask.class.getSimpleName();
    private final String INSERTGEO = "INSERT INTO x_geolocaliz_model VALUES(null,?,?,?,?,null,null,null)";
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


        for (XGeoModel geo: geoList) {
            updatePst.setInt(1,1);
            updatePst.addBatch();
        }

        try{
            updatePst.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}