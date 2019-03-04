package com.google.tanbarin

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.InputStreamReader
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_noti.view.*
import com.nifcloud.mbaas.core.*
import org.json.JSONObject
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.util.*
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import android.location.LocationListener
import android.location.LocationProvider
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.text.format.DateFormat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.w3c.dom.Text
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [fragment_noti.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [fragment_noti.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

//********** APIキーの設定 **********
const val applicationKey:String = "d8d285600ea72d85f252115ba60abfad241d3ad6c6485c20ad54a9a2d2135498"
const val clientKey:String = "582f275e0947105e7d9ad29e9140b7f68026ecd081b43b73c5c656adff7632a1"


class fragment_noti : Fragment(), LocationListener {
    // TODO: Rename and change types of parameters
    private lateinit var locationManager: LocationManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noti, container, false)
    }

    internal var b1: Button? = null
    internal var b2: Button? = null
    internal var iv: ImageView? = null
    internal var pr: ImageView? = null
    internal var tx_name: EditText? = null
    internal var tx_detail: EditText? = null
    internal var ido: Double? = null
    internal var keido: Double? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        //-----------SDKの初期化 **********
        //NCMB.initialize(applicationContext, applicationKey, clientKey) ←java用
        NCMB.initialize(activity!!, applicationKey, clientKey)

        val date: Date = Date()
        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
        val test1: String = DateFormat.format("yyyy/MM/dd kk:mm:ss", date).toString()
        val test2: String = DateFormat.format("yyyyMMddkkmmss", date).toString()


        Log.d("dat", date.toString())
        Log.d("dat", calendar.toString())
        Log.d("taita", test1)
        b1 = view.findViewById<Button>(R.id.button_send) //as Button
        b2 = view.findViewById<Button>(R.id.button_pic) //as Button
        iv = view.findViewById<ImageView>(R.id.imageView) //as ImageView
        pr = view.findViewById<ImageView>(R.id.preview) //as ImageView
        tx_name = view.findViewById<EditText>(R.id.spot_name) //as ImageView
        tx_detail = view.findViewById<EditText>(R.id.detail) //as ImageView

        b2?.setOnClickListener { requestCameraPermission() }
        b1?.visibility = View.INVISIBLE
        pr?.visibility = View.INVISIBLE
        tx_detail?.visibility = View.INVISIBLE
        tx_name?.visibility = View.INVISIBLE

        locationStart()


    }

    fun data_save(spot_name:String, detail:String, image_name:String, location:ArrayList<Double>, date:Date, others: String){
        //******* NCMB file download *******
        val obj = NCMBObject("SaveObject")
        obj.put("spot_name", spot_name)
        obj.put("detail", detail)
        obj.put("image_name", image_name)
        obj.put("location", location)
        obj.put("date", date)
        obj.put("others", others)

        obj.saveInBackground { e ->
            if (e != null) {
                // 保存に失敗した場合の処理
                AlertDialog.Builder(activity!!)
                    .setTitle("waiha")
                    .setMessage("Error:" + e!!.message)
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                // 保存に成功した場合の処理

            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data)
        locationStart()

        if (data == null || data.extras == null) {
            return
        } else {
            val bp = data.extras!!.get("data") as Bitmap
            //******* NCMB file upload *******
            val byteArrayStream = ByteArrayOutputStream()
            bp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream)
            val dataByte = byteArrayStream.toByteArray()

            //ACL 読み込み:可 , 書き込み:可
            val acl = NCMBAcl()
            acl.publicReadAccess = true
            acl.publicWriteAccess = true

            pr?.setImageBitmap(bp)
            b1?.text = "送信"
            b1?.setOnClickListener { cameraSend(dataByte, acl) }
            b1?.visibility = View.VISIBLE
            pr?.visibility = View.VISIBLE
            b2?.text = "再撮影"
            b2?.setOnClickListener { requestCameraPermission() }
            tx_detail?.visibility = View.VISIBLE
            tx_name?.visibility = View.VISIBLE




        }
    }

    private fun cameraSend(dataByte : ByteArray, acl : NCMBAcl){
        //通信実施
        val date: Date = Date()
        val test1: String = DateFormat.format("yyyyMMddkkmmss", date).toString()
        val test2: String = DateFormat.format("yyyyMMddkkmmss", date).toString()

        var fileName = "s"+ test2 + ".png"
        val file = NCMBFile(fileName, dataByte, acl)
        b1?.visibility = View.INVISIBLE
        pr?.visibility = View.INVISIBLE
        tx_detail?.visibility = View.INVISIBLE
        tx_name?.visibility = View.INVISIBLE
        b2?.text = "撮影"
        file.saveInBackground { e ->
            val result: String
            if (e != null) {
                //保存失敗
                AlertDialog.Builder(activity!!)
                    .setTitle("Notification from NIFCloud")
                    .setMessage("Error:" + e.message)
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                //******* NCMB file download *******
                val file = NCMBFile(fileName)
                file.fetchInBackground { dataFetch, er ->
                    if (er != null) {
                        //失敗処理
                        AlertDialog.Builder(activity!!)
                            .setTitle("Notification from NIFCloud")
                            .setMessage("Error:" + er.message)
                            .setPositiveButton("OK", null)
                            .show()
                    } else {
                        //成功処理
                        val bMap = BitmapFactory.decodeByteArray(dataFetch, 0, dataFetch.size)
                        iv?.setImageBitmap(bMap)
                        data_save(tx_name!!.text.toString(),tx_detail!!.text.toString(), fileName, arrayListOf(ido!!,keido!!), date, "")
                    }
                }


            }
        }
    }

    private fun requestCameraPermission() {
        Dexter.withActivity(activity!!)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted
                    openCamera()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 0)
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }


    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "packageName", null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }


    private fun locationStart() {

        // Fine か Coarseのいずれかのパーミッションが得られているかチェックする
        // 本来なら、Android6.0以上かそうでないかで実装を分ける必要がある
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            /** fine location のリクエストコード（値は他のパーミッションと被らなければ、なんでも良い）*/
            var requestCode = 1;

            if (ContextCompat.checkSelfPermission(activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)

                Log.d("debug", "checkSelfPermission false")
                return
            }
        }

        var locationProvider = ""

        // GPSが利用可能になっているかどうかをチェック
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        }
        // GPSプロバイダーが有効になっていない場合は基地局情報が利用可能になっているかをチェック
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        // いずれも利用可能でない場合は、GPSを設定する画面に遷移する
        else {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
            Log.d("debug", "not gpsEnable, startActivity")
            return
        }

        /** 位置情報の通知するための最小時間間隔（ミリ秒） */
        var minTime:Long = 500
        /** 位置情報を通知するための最小距離間隔（メートル）*/
        var  minDistance:Float = 1f

        // 利用可能なロケーションプロバイダによる位置情報の取得の開始
        // FIXME 本来であれば、リスナが複数回登録されないようにチェックする必要がある
        locationManager.requestLocationUpdates(
            locationProvider,
            minTime,
            minDistance,
            this)
        // 最新の位置情報
        var location = locationManager.getLastKnownLocation(locationProvider);

        if (location != null) {
            //TextView textView = (TextView) findViewById(R.id.location);
            //textView.setText(String.valueOf( "onCreate() : " + location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug", "checkSelfPermission true")
                // 位置測定を始めるコードへ跳ぶ
                locationStart()

            } else {
                // それでも拒否された時の対応
                val toast = Toast.makeText(
                    activity!!,
                    "これ以上なにもできません", Toast.LENGTH_SHORT
                )
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

    override fun onLocationChanged(location: Location) {

        Log.d("ido", location.getLatitude().toString())
        Log.d("ido", location.getLongitude().toString())
        ido = location.getLatitude()
        keido = location.getLongitude()

    }



    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

}

