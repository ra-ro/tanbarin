package com.google.tanbarin

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBAcl
import com.nifcloud.mbaas.core.NCMBFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


data class openDataMain(val name : String, val desc: String, val imageId: Int, val poss:LatLng)

class MainActivity : AppCompatActivity() {
    val images1 = listOf(
        R.drawable.omomuki01,
        R.drawable.omomuki02,
        R.drawable.omomuki03,
        R.drawable.omomuki04,
        R.drawable.omomuki05,
        R.drawable.omomuki06,
        R.drawable.omomuki07,
        R.drawable.omomuki08,
        R.drawable.omomuki09,
        R.drawable.omomuki10,
        R.drawable.omomuki11,
        R.drawable.omomuki12,
        R.drawable.omomuki13,
        R.drawable.omomuki14,
        R.drawable.omomuki16,
        R.drawable.omomuki17,
        R.drawable.omomuki18,
        R.drawable.omomuki19,
        R.drawable.omomuki20,
        R.drawable.omomuki21,
        R.drawable.omomuki22,
        R.drawable.omomuki23,
        R.drawable.omomuki24,
        R.drawable.omomuki25,
        R.drawable.omomuki26,
        R.drawable.omomuki27,
        R.drawable.omomuki28,
        R.drawable.omomuki29,
        R.drawable.omomuki30,
        R.drawable.omomuki31,
        R.drawable.omomuki32,
        R.drawable.omomuki33,
        R.drawable.omomuki34,
        R.drawable.omomuki35,
        R.drawable.omomuki36,
        R.drawable.omomuki37,
        R.drawable.omomuki38,
        R.drawable.omomuki39,
        R.drawable.omomuki40,
        R.drawable.omomuki35,
        R.drawable.omomuki36,
        R.drawable.omomuki37,
        R.drawable.omomuki38,
        R.drawable.omomuki39,
        R.drawable.omomuki40,
        R.drawable.omomuki35,
        R.drawable.omomuki36,
        R.drawable.omomuki37,
        R.drawable.omomuki38,
        R.drawable.omomuki39,
        R.drawable.omomuki40
    )
    val images2 = listOf(
        R.drawable.kankou01,
        R.drawable.kankou02,
        R.drawable.kankou03,
        R.drawable.kankou04,
        R.drawable.kankou05,
        R.drawable.kankou06,
        R.drawable.kankou07,
        R.drawable.kankou08,
        R.drawable.kankou09,
        R.drawable.kankou10,
        R.drawable.kankou11,
        R.drawable.kankou12,
        R.drawable.kankou13,
        R.drawable.kankou14,
        R.drawable.kankou15,
        R.drawable.kankou16,
        R.drawable.kankou17,
        R.drawable.kankou18,
        R.drawable.kankou19,
        R.drawable.kankou20,
        R.drawable.kankou21,
        R.drawable.kankou22,
        R.drawable.kankou23,
        R.drawable.kankou24,
        R.drawable.kankou25,
        R.drawable.kankou26,
        R.drawable.kankou22,
        R.drawable.kankou23,
        R.drawable.kankou24,
        R.drawable.kankou25,
        R.drawable.kankou26,
        R.drawable.kankou22,
        R.drawable.kankou23,
        R.drawable.kankou24,
        R.drawable.kankou25,
        R.drawable.kankou26
    )


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment_home.createInstance(list_omomuki!!))
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment_dash.createInstance(list_omomuki!!, list_kankou!!))
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment_noti())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_maps -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment_maps.createInstance(list_omomuki!!, list_kankou!!))
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    internal var list_omomuki: MutableList<datalist>? = mutableListOf<datalist>()
    internal var list_kankou: MutableList<datalist>? = mutableListOf<datalist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)  //superコールの前にスタイル設定（LauncherScreenを入れたので）
        super.onCreate(savedInstanceState)

        val assetManager = this.getResources().getAssets()
        val gcoder = Geocoder(this, Locale.getDefault())

        var i = 0
        var str = ""
        var imagei=0
        var poss = LatLng(0.0,0.0)
        try {
            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open("tasteful-buildings.csv")))
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
                        poss = LatLng(lat, lng)
                    }
                    list_omomuki!!.add(datalist(columnList[0], columnList[3], images1[imagei], poss))
                    Log.d("naraki", poss.toString())
                    imagei++
                    str = ""
                } else {

                }
                i = 0
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        i = 0
        str = ""
        imagei=0
        poss = LatLng(0.0,0.0)
        try {
            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open("tourist-facilities.csv")))
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
                        poss = LatLng(lat, lng)
                    }
                    list_kankou!!.add(datalist(columnList[0], columnList[3], images2[imagei], poss))
                    imagei++
                    str = ""
                } else {

                }
                i = 0
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }



        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}
