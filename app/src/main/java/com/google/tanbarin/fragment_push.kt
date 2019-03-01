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
import kotlin.collections.ArrayList
import kotlin.math.log



class fragment_push: Fragment() {

    private lateinit var _pushId: TextView
    private lateinit var _richurl: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_push, container, false)
    }


    internal var listuser: MutableList<datalist>? = mutableListOf<datalist>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(
            activity!!,
            applicationKey,
            clientKey
        )

        val query = NCMBQuery<NCMBObject>("SaveObject")
        query.addOrderByAscending("updateDate")
        query.setLimit(10)

        query.findInBackground { result, e ->
            if (e != null) {
                AlertDialog.Builder(activity!!)
                    .setTitle("waiha")
                    .setMessage("Error:" + e!!.message)
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                // 保存に成功した場合の処理
                result.forEachIndexed { index, ncmbObject ->
                    Log.d("asdfg", ncmbObject.getString("detail").toString())
                }

            }
        }

    }
}
