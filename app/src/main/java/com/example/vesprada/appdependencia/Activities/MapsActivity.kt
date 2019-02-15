package com.example.vesprada.appdependencia.Activities

import android.Manifest
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton

import com.example.vesprada.appdependencia.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import java.util.*
import com.google.android.gms.maps.model.PointOfInterest
import android.content.res.Resources.NotFoundException
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat
//import com.google.android.gms.location.places.Places
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var btEmergency: ImageButton
    lateinit var btCitas: ImageButton
    lateinit var btNotifications: ImageButton
    private lateinit var mMap: GoogleMap
    private val REQUESTLOCATIONPERMISSION=1
    lateinit var markerPoints : ArrayList<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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


        val currentPlace= LatLng(38.691450,-0.496278)
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

}