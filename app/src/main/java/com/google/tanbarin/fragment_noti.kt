package com.google.tanbarin

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.ByteArrayOutputStream


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



    //------------以下データストア関連（現在位置）-----------------------------


    data class itiInfo (
        var genzaiido:String = "",
        var genzaikeido:String = ""
    ){}

    //↓データストアの読み出し
    fun getitinfo(genzaiido:String) : itiInfo {
        val mytanbarin = itiInfo()

        mytanbarin.genzaiido = genzaiido
        val query: NCMBQuery<NCMBObject> = NCMBQuery("tanbarin")
        query.whereEqualTo("tanbarinID",genzaiido)
        val results: List<NCMBObject> = try {
            query.find()
        } catch (e : Exception) { emptyList<NCMBObject>() }
        if (results.isNotEmpty()) {
            val result = results[0]
            mytanbarin.genzaikeido = result.getString("tanbarinName")
        }
        return mytanbarin

    }


    //↓データストアへの追加
    fun addtiti(company:itiInfo) {

        val obj = NCMBObject("Company")

        obj.put("CompanyName", company.genzaikeido)
        obj.put("companyID", company.genzaiido)

        try {
            obj.save()
        } catch (e : Exception) {
            println("Company data save error : " + e.cause.toString())
        }
    }

    //↓データストアの更新
    fun updateiti(company:itiInfo) {

        val query: NCMBQuery<NCMBObject> = NCMBQuery("Company")
        query.whereEqualTo("companyID", company.genzaiido)

        val results: List<NCMBObject> = try {
            query.find()
        } catch (e: Exception) {
            emptyList<NCMBObject>()
        }
        if (results.isNotEmpty()) {
            val obj = results[0]
            obj.put("CompanyName", company.genzaikeido)
            obj.put("companyID", company.genzaiido)

            try {
                obj.save()
            } catch (e: Exception) {
                println("company data save error : " + e.cause.toString())
            }
        }
    }




}

