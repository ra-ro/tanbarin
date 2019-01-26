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
const val applicationKey:String = "4be64b73110568a79692b7fced842a43ea7f8330ac9672f490c38b1cae2a04f2"
const val clientKey:String = "a6432123d33c4004f4b054151487e7bc25dddbfbdd63ef603400f5e5bd2c981c"


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
            bp.compress(Bitmap.CompressFormat.PNG, 0, byteArrayStream)
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

        val file = NCMBFile("s"+ test2 + ".png", dataByte, acl)
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
                val file = NCMBFile("test.png")
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
                        data_save(tx_name!!.text.toString(),tx_detail!!.text.toString(), "s"+ test2 + ".png", arrayListOf(ido!!,keido!!), date, "")
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
        /*
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
            */
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
    /*


        override fun onResume() {
            super.onResume()

        }
    */
    override fun onLocationChanged(location: Location) {

        Log.d("ido", location.getLatitude().toString())
        Log.d("ido", location.getLongitude().toString())
        ido = location.getLatitude()
        keido = location.getLongitude()

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
//----------------------------------------------------------------------------------

















    /*
//ニフクラ公式のサンプル（Javaをコンバートしただけ)
//複雑すぎてわかんね
//https://github.com/NIFCloud-mbaas/GeolocationPush_android

    private var mGoogleApiClient: GoogleApiClient? = null

    private var mGeofenceRequest: GeofencingRequest? = null

    private// We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
    // calling addGeofences() and removeGeofences().
    val geofencePendingIntent: PendingIntent
        get() {

            val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
            startActivity(intent)

            return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    override fun onMessageReceived(String from, Bundle data) {
        //ペイロードデータの取得
        if (data.containsKey("com.nifty.Data")) {
            try {
                JSONObject json = new JSONObject(data.getString("com.nifty.Data"));
            } catch (JSONException e) {
                //エラー処理
                Log.e(TAG, "error:" + e.getMessage());
            } catch (NCMBException e) {
                Log.e(TAG, "error:" + e.getMessage());
            }
        }

    }

    @Override
    private fun onMessageReceived(from: String, data: Bundle) {

        //ペイロードデータの取得
        if (data.containsKey("com.nifty.Data")) {
            try {
                val json = JSONObject(data.getString("com.nifty.Data"))

                //Locationデータの取得
                val point = NCMBObject("Location")
                point.setObjectId(json.getString("location_id"))
                point.fetch()

                Log.d(TAG, "location name:" + point.getString("name"))

                //geofenceの作成
                createGeofenceRequest(point)

                //Google API Clientのビルドと接続
                connectGoogleApiClient()


            } catch (e: JSONException) {
                //エラー処理
                Log.e(TAG, "error:" + e.getMessage())
            } catch (e: NCMBException) {
                Log.e(TAG, "error:" + e.getMessage())
            }

        }

        //デフォルトの通知を実行する場合はsuper.onMessageReceivedを実行する
        //super.onMessageReceived(from, data);
    }

    @Synchronized
    protected fun connectGoogleApiClient() {
        var mGoogleApiClient = GoogleApiClient.Builder(activity!!)
            .addConnectionCallbacks(activity!!)
            .addOnConnectionFailedListener(activity!!)
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient!!.connect()
    }

    private fun createGeofenceRequest(point: NCMBObject) {

        //Geofenceオブジェクトの作成
        val geofence = Geofence.Builder()
            .setRequestId(point.getString("name"))
            .setCircularRegion(
                point.getGeolocation("geo").getLatitude(),
                point.getGeolocation("geo").getLongitude(),
                GEOFENCE_RADIUS_IN_METERS
            )
            .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val builder = GeofencingRequest.Builder()
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        builder.addGeofence(geofence)
        mGeofenceRequest = builder.build()
    }

    @Override
    fun onConnected(bundle: Bundle) {
        Log.d(TAG, "Connection Succeeded.")

        val preferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val geofenceName = preferences.getString(GEOFENCE_NAME, "")

        if (!geofenceName.equals("")) {
            LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                Arrays.asList(geofenceName)
            )
        }

        val editor = preferences.edit()
        editor.putString(
            GEOFENCE_NAME,
            mGeofenceRequest!!.getGeofences().get(0).getRequestId()
        )

        LocationServices.GeofencingApi.addGeofences(
            mGoogleApiClient,
            mGeofenceRequest,
            geofencePendingIntent
        ).setResultCallback(this)
    }

    @Override
    fun onConnectionSuspended(i: Int) {
        Log.d(TAG, "Connection Suspended")
    }

    @Override
    fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "Connection Failed")
    }

    @Override
    fun onResult(result: Result) {
        Log.d(TAG, "onResult:" + result.toString())
    }

    @Override
    fun onDestroy() {
        Log.d(TAG, "Gcm service is destroyed...")
        super.onDestroy()
    }

    companion object {

        protected val TAG = "CustomListenerService"

        protected val GEOFENCE_RADIUS_IN_METERS = 500

        protected val GEOFENCE_EXPIRATION_IN_HOURS = 1

        protected val GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000

        protected val PREFS_NAME = "GeolocationPush"

        protected val GEOFENCE_NAME = "GeofenceName"
    }
}
*/


/*
    //------------以下データストア関連（現在位置）-----------------------------

    //https://qiita.com/hiro_hosono/items/54ff78376b0993b822f7

    data class itiInfo (
        var genzaiido:String = "",
        var genzaikeido:String = ""
    )
    {

    }

    //↓データストアの読み出し
    fun getitiinfo(genzaiido:String) : itiInfo {
        val genzaiiti = itiInfo()

        genzaiiti.genzaiido = genzaiido
        val query: NCMBQuery<NCMBObject> = NCMBQuery("iti")
        query.whereEqualTo("genzaiido",genzaiido)
        val results: List<NCMBObject> = try {
            query.find()
        } catch (e : Exception) { emptyList<NCMBObject>() }
        if (results.isNotEmpty()) {
            val result = results[0]
            genzaiiti.genzaikeido = result.getString("genzaikeido")
        }
        return genzaiiti

    }


    //↓データストアへの追加
    fun addtiti(iti:itiInfo) {

        val obj = NCMBObject("itiInfo")

        obj.put("genzaikeido", itiInfo.genzaikeido)
        obj.put("genzaiido", company.genzaiido)

        try {
            obj.save()
        } catch (e : Exception) {
            println("data save error : " + e.cause.toString())
        }
    }

    //↓データストアの更新
    fun updateiti(company:itiInfo) {

        val query: NCMBQuery<NCMBObject> = NCMBQuery("genzaiiti")
        query.whereEqualTo("genzaiido", company.genzaiido)

        val results: List<NCMBObject> = try {
            query.find()
        } catch (e: Exception) {
            emptyList<NCMBObject>()
        }
        if (results.isNotEmpty()) {
            val obj = results[0]
            obj.put("genzaikeido", company.genzaikeido)
            obj.put("genzaiido", company.genzaiido)

            try {
                obj.save()
            } catch (e: Exception) {
                println("company data save error : " + e.cause.toString())
            }
        }
    }

*/


}

