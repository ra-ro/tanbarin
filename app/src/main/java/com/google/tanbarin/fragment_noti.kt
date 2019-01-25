package com.google.tanbarin

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
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


class fragment_noti : Fragment() {
    // TODO: Rename and change types of parameters


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
    //internal var b2: Button? = null
    internal var iv: ImageView? = null

    internal var pr: ImageView? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //-----------SDKの初期化 **********
        //NCMB.initialize(applicationContext, applicationKey, clientKey) ←java用
        NCMB.initialize(activity!!, applicationKey, clientKey)

        b1 = view.findViewById<Button>(R.id.button_send) //as Button
        b2 = view.findViewById<Button>(R.id.button_pic) //as Button
        iv = view.findViewById<ImageView>(R.id.imageView) //as ImageView
        pr = view.findViewById<ImageView>(R.id.preview) //as ImageView
        b2?.setOnClickListener { requestCameraPermission() }
        b1?.visibility = View.INVISIBLE

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data)
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
            b2?.text = "再撮影"
            b2?.setOnClickListener { requestCameraPermission() }




        }
    }

    private fun cameraSend(dataByte : ByteArray, acl : NCMBAcl){
        //通信実施
        val file = NCMBFile("test.png", dataByte, acl)
        b1?.visibility = View.INVISIBLE
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

