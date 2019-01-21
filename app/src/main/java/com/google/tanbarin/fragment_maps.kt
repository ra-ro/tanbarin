package com.google.tanbarin

import android.support.v4.content.ContextCompat.startActivity
import android.Manifest
//import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
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
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_maps.*


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


class fragment_maps : android.support.v4.app.Fragment(), LocationListener{
    private lateinit var locationManager: LocationManager
    private lateinit var mMap : GoogleMap
    // TODO: Rename and change types of parameters
    //private val mSampleData = ClsListData()

    var mContext: Context? = null

    companion object {
        fun createInstance(mc: Context): fragment_maps {
            // インスタンス？　MainActivityで生成時に呼ばれている関数
            val tmpDetailFragment = fragment_maps()
            val args = Bundle()
            tmpDetailFragment.mContext = mc
            tmpDetailFragment.arguments = args
            return tmpDetailFragment
        }
    }

/*
    //アクティビティのインスタンスを保存する変数
    private var listener: OnFragmentInteractionListener? = null

    //アクティビティのインスタンスの取得
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun locationStart()
    }
*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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
        return inflater.inflate(R.layout.activity_maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*val view = layoutInflater.inflate(R.layout.fragment_maps, container, false)

        val mapFragment = activity!!.supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(map)                // ここでNullPointerExceptionが発生します
       return view*/
    }

    override fun onResume() {
        super.onResume()

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
