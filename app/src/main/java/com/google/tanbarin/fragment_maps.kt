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
private lateinit var list_omomuki : ArrayList<datalist>
private lateinit var list_kankou : ArrayList<datalist>
private lateinit var list_userdata : ArrayList<datalist>

class fragment_maps : android.support.v4.app.Fragment(), OnMapReadyCallback, LocationListener{
    private lateinit var locationManager: LocationManager
    private lateinit var mMap : GoogleMap

    companion object {
        private const val list_o = "omo"
        private const val list_k = "kan"
        private const val list_u = "use"

        fun createInstance(list1:MutableList<datalist>, list2:MutableList<datalist>, list3:MutableList<datalist>): fragment_maps {
            val frg = fragment_maps()
            val args = Bundle()

            args.putParcelableArrayList(list_o, ArrayList(list1))
            args.putParcelableArrayList(list_k, ArrayList(list2))
            args.putParcelableArrayList(list_u, ArrayList(list2))

            frg.arguments = args
            return frg
        }
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
        val args = arguments
        if (args == null) {
        } else {
            list_omomuki = args.getParcelableArrayList<datalist>(fragment_maps.list_o)!!
            list_kankou = args.getParcelableArrayList<datalist>(fragment_maps.list_k)!!
            list_userdata = args.getParcelableArrayList<datalist>(fragment_maps.list_u)!!


        }

        try {
            MapsInitializer.initialize(activity!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMap = googlemap
        Log.d("maita", "datadatata;")
        val hirosakieki = LatLng(40.599257, 140.4851)


        //地図へのマーカーの設定方法
        mMap.addMarker(MarkerOptions().position(hirosakieki).title("弘前駅"))

        try {

            list_omomuki.forEachIndexed(){index, it ->
                Log.d("tomato", it.poss.toString())
                mMap.addMarker(MarkerOptions().position(it.poss).title(it.name))
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
