package com.example.vesprada.appdependencia.Activities

//import com.google.android.gms.location.places.Places
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.vesprada.appdependencia.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_red_button.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val REQUESTLOCATIONPERMISSION=1
    lateinit var markerPoints : ArrayList<LatLng>
    private val MYPREFS = "MyPrefs"
    private val DAYNIGHT = "dayNight"
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId= R.id.googlemap

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        preferences = getSharedPreferences(MYPREFS, Context.MODE_PRIVATE)
        cambiarModoNocturnoDiurno(preferences.getBoolean(DAYNIGHT, true))

    }

    private fun cambiarModoNocturnoDiurno(b: Boolean){

        if(b){
            findViewById<ConstraintLayout>(R.id.mapLayout).background = getDrawable(R.drawable.patternbg)
        } else {
            findViewById<ConstraintLayout>(R.id.mapLayout).background = getDrawable(R.drawable.patternbg_dark)
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val currentPlace = LatLng(38.691450,-0.496278)
        val zoom=15.0F

        enableMyLocation()

        mMap.addMarker(MarkerOptions()
                .position(currentPlace)
                .title("Current Place  Pulsa para borrar")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace,zoom))
        setMapLongClick(mMap)
        setPoiClick(mMap)
        mMap.setOnInfoWindowClickListener { marker ->

            marker.remove()
            true

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.getItemId()) {
            R.id.normal_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }
            R.id.hybrid_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                return true
            }
            R.id.satellite_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            R.id.terrain_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setMapLongClick(map: GoogleMap) {

        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                    Locale.getDefault(),
                    "Lat: %1$.5f, Long: %2$.5f Pulsa aquí para borrar",
                    latLng.latitude,
                    latLng.longitude
            )

            map.addMarker(
                    MarkerOptions()
                            .position(latLng)
                            .title(getString(R.string.dropped_pin))
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
            markerPoints.add(latLng)

        }


    }

    private fun setPoiClick(map: GoogleMap) {

        map.setOnPoiClickListener { poi ->


            val poiMarker = mMap.addMarker(
                    MarkerOptions()
                            .position(poi.latLng)
                            .title(poi.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )

            poiMarker.showInfoWindow()

        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUESTLOCATIONPERMISSION )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray){

        when (requestCode) {
            REQUESTLOCATIONPERMISSION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation()
                }
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.emergencias -> {
                var intent = Intent(this, RedButtonActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.notificaciones -> {

                var intent = Intent(this, NotificacionesActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.eventos -> {

                var intent = Intent(this, HistorialActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.googlemap -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.configuracion -> {
                var intent = Intent(this, ConfiguracionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        false
    }

}