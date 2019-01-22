package com.google.tanbarin

import android.support.v4.content.ContextCompat.startActivity
import android.Manifest
//import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.*
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.app.Fragment
import android.support.v4.app.SupportActivity
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
//import com.google.android.gms.maps.GoogleMap
import com.google.tanbarin.R.drawable.e
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.maps.SupportMapFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [fragment_maps.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [fragment_maps.newInstance] factory method to
 * create an instance of this fragment.
 *
 */


class fragment_maps : android.support.v4.app.Fragment(), OnMapReadyCallback, LocationListener{
    private lateinit var locationManager: LocationManager
    private lateinit var mMap : GoogleMap

    fun newInstance(): fragment_maps {
        val fragment = newInstance()
        val args = Bundle()
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

/*
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
*/

        // Inflate the layout for this fragment
        /*

        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000)
        }else {
            locationStart()
            if (::locationManager.isInitialized) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    50f,
                    this
                )
            }

        }
        */
        return inflater.inflate(R.layout.activity_maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }


    override fun onMapReady(googlemap: GoogleMap) {

        // Need to call MapsInitializer before doing any CameraUpdateFactory calls
        val gcoder = Geocoder(activity, Locale.getDefault())

        try {
            MapsInitializer.initialize(activity!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMap = googlemap
        Log.d("maita", "datadatata;")

        val hanedaAirport = LatLng(35.5554, 139.7544)
        val kankuAirport = LatLng(34.4320024, 135.2303939)
        val hirosakieki = LatLng(40.599257, 140.4851)




        //地図へのマーカーの設定方法
        mMap.addMarker(MarkerOptions().position(hirosakieki).title("弘前駅"))
        val assetManager = activity!!.getResources().getAssets()

        try {
            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open("tasteful-buildings.csv")))
            var i = 0
            var str = ""


                bufferedReader.lineSequence().forEachIndexed() { index, it ->
                    if(index==0) {
                        return@forEachIndexed
                    }
                    str += it
                    str.toList().forEach {
                        if (it == '\"') {
                            i++
                        }
                    }
                    if (i % 2 == 0) {
                        var columnList = str.split(",")
                        Log.d("naraki", columnList[7] + " " + columnList[8])
                        var posadd = gcoder.getFromLocationName(columnList[1].toString(), 1)
                        if (posadd != null) {
                            var pos = posadd.get(0)
                            var lat = pos.latitude
                            var lng = pos.longitude
                            var poss = LatLng(lat, lng)
                            Log.d("maita", columnList[0])
                            mMap.addMarker(MarkerOptions().position(poss).title(columnList[0]))
                        }
                        str = ""
                    } else {

                    }
                    i = 0
                }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        //地図の移動　画面上に表示される場所を指定する
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(hirosakieki))

        //マップのズーム絶対値指定　1: 世界 5: 大陸 10:都市 15:街路 20:建物 ぐらいのサイズ
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))

        //1段階(レベル)のズーム
        //mMap.moveCamera(CameraUpdateFactory.zoomIn())

        //複数段階のズーム
        //mMap.moveCamera(CameraUpdateFactory.zoomBy(2F))

        //地図を移動して特定段階のズーム
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hirosakieki, 15F))

        //指定ピクセルだけスクロール
        //mMap.moveCamera(CameraUpdateFactory.scrollBy(100F, 100F))

        //カメラの回転、東西南北の向き、見下ろす角度などの指定
        /*
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().let {
            //位置の指定必須　ないとエラーになる
            it.target(hanedaAirport)
            //東西南北の指定　北を0として右回りで360度
            it.bearing(90F)
            //カメラの見下ろす角度の指定　真下を0として90度
            it.tilt(10F)
            //ズームを指定しないと元にサイズに戻されるので事実上指定必須
            it.zoom(15F)
            it.build()
        }))
        */

    }


    private fun setUpMap() {
        // mMap.getUiSettings().setZoomControlsEnabled(true)//拡大縮小ボタン表示
        val sydney = LatLng(40.0, 133.0)
        val test= "data"
        val test2="omese"
        if (mMap == null) {
            Log.d("maita", "data;" )
            println("$test")
        }
        println("$test2")
        Log.d("maita", "datadatata;" )
        // Add a marker in Sydney and move the camera
        mMap.addMarker(MarkerOptions().position(sydney).title("tgdusdfg"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15F))
    }



/*
    private fun locationStart() {
        Log.d("debug", "locationStart()")

        // Instances of LocationManager class must be obtained using Context.getSystemService(Class)
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled")
        } else {
            // to prompt setting up GPS
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
            Log.d("debug", "not gpsEnable, startActivity")
        }

        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)

            Log.d("debug", "checkSelfPermission false")
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            50f,
            this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug", "checkSelfPermission true")
                // 位置測定を始めるコードへ跳ぶ
                locationStart()

            }else {
                // それでも拒否された時の対応
                val toast = Toast.makeText(activity!!,
                    "これ以上なにもできません", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

    }
*/
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        when (status) {
            LocationProvider.AVAILABLE ->
                Log.d("debug", "LocationProvider.AVAILABLE")
            LocationProvider.OUT_OF_SERVICE ->
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE")
            LocationProvider.TEMPORARILY_UNAVAILABLE ->
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE")
        }
    }
/*


    override fun onResume() {
        super.onResume()

    }
*/
    override fun onLocationChanged(location: Location) {
        // Latitude
        //val textView1 = findViewById<TextView>(R.id.text_view1)
        //val str1 = "Latitude:" + location.getLatitude()
        //textView1.text = str1

        // Longitude
        //val textView2 = findViewById<TextView>(R.id.text_view2)
        //val str2 = "Longtude:" + location.getLongitude()
        //textView2.text = str2
    }

/*
    private fun setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = (childFragmentManager.findFragmentById(R.layout.fragment_maps) as SupportMapFragment)
                .getMap()
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap()
            }
        }
    }*/

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */

}
