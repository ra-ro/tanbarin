package com.google.tanbarin

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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




class fragment_push: Fragment() {

    private lateinit var _pushId: TextView
    private lateinit var _richurl: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(activity!!, "4be64b73110568a79692b7fced842a43ea7f8330ac9672f490c38b1cae2a04f", "a6432123d33c4004f4b054151487e7bc25dddbfbdd63ef603400f5e5bd2c981c")

        try {
            val tmpBlank = JSONObject("{'No key':'No value'}")
            val lv = view.findViewById<ListView>(R.id.listview) //as ListView
            if (lv != null) {
                //lv.adapter = ListAdapter(activity!!, tmpBlank)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

/**/
    }
    var id: String? = null
    var url: String? = null

    //override fun onResume() {
    override fun onCreateDialog(savedInstanceState: Bundle?,data: Intent?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        //**************** ペイロード、リッチプッシュを処理する ***************
        val intent = Intent()

        //プッシュ通知IDを表示
        _pushId = dialog.findViewById<TextView>(R.id._pushId) .text = id
        val pushid = intent.getStringExtra("com.nifcloud.mbaas.PushId")
        _pushId.text = pushid

        //RichURLを表示
        _richurl = dialog.findViewById<TextView>(R.id.txtRichurl) .text = url
        val richurl = intent.getStringExtra("com.nifcloud.mbaas.RichUrl")
        _richurl.text = richurl

        //プッシュ通知のペイロードを表示
        if (intent.getStringExtra("com.nifcloud.mbaas.Data") != null) {
            try {
                val json = JSONObject(intent.getStringExtra("com.nifcloud.mbaas.Data"))
                if (json != null) {
                    val lv = dialog.findViewById<View>(R.id.lsJson) as ListView
                    lv.adapter = ListAdapter(this, json)
                }
            } catch (e: JSONException) {
                //エラー処理
            }

        }
        intent.removeExtra("com.nifcloud.mbaas.RichUrl")
        //return dialog
    }
}
