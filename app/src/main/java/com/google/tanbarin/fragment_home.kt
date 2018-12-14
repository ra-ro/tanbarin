package com.google.tanbarin

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.tanbarin.R.drawable.images
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

@Suppress("UNREACHABLE_CODE")
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [fragment_home.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [fragment_home.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
val names = listOf(
"あじさい",
"蓮",
"ネモフィラ",
"バラ",
"ふじ")
val descriptions = listOf(
    "アジサイ（紫陽花、学名 Hydrangea macrophylla）は、アジサイ科アジサイ属の落葉低木の一種である。広義には「アジサイ」の名はアジサイ属植物の一部の総称でもある。",
    "ハス（蓮、学名：Nelumbo nucifera）は、インド原産のハス科多年性水生植物。",
    "ネモフィラはムラサキ科ネモフィラ属（Nemophila）に分類される植物の総称。または、ルリカラクサ（瑠璃唐草、学名：Nemophila menziesii）のこと。",
    "バラ（薔薇）は、バラ科バラ属の総称である。あるいは、そのうち特に園芸種（園芸バラ・栽培バラ）を総称する。ここでは、後者の園芸バラ・栽培バラを扱うこととする。 バラ属の成形は、灌木、低木、または木本性のつる植物で、葉や茎に棘を持つものが多い。",
    "フジ（藤、学名: Wisteria floribunda）は、マメ科フジ属のつる性落葉木本。一般名称としての藤には、つるが右巻き（上から見て時計回り）と左巻きの二種類がある。")
val images = listOf(
    R.drawable.a,
    R.drawable.b,
    R.drawable.c,
    R.drawable.d,
    R.drawable.e)

data class FlowerData(val name : String, val desc: String, val imageId: Int)
val flowers = List(names.size) { i -> FlowerData(names[i], descriptions[i], images)}
data class ViewHolder(val nameTextView: TextView, val descTextView: TextView, val flowerImgView: ImageView)
class FlowerListAdapter(context: Context, flowers: List<FlowerData>) : ArrayAdapter<FlowerData>(context, 0, flowers) {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var holder: ViewHolder

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item, parent, false)
            holder = ViewHolder(
                view.nameTextView!!,
                view.descTextView,
                view.flowerImgView
            )
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val flower = getItem(position) as FlowerData
        holder.nameTextView.text = flower.name
        holder.descTextView.text = flower.desc
        holder.flowerImgView.setImageBitmap(BitmapFactory.decodeResource(context.resources, flower.imageId))

        return view!!
    }
}


class fragment_home : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        


    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)





    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val assetManager = context!!.getResources().getAssets()
        val texts = arrayOf("abc ", "bcd", "cde", "def", "efg",
            "fgh", "ghi", "hij", "ijk", "jkl", "klm")
        val dataArray = arrayOf("Kotlin","Android","iOS","Swift","Java")

        try {/*
            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open("tasteful-buildings.csv")))

            //val listView = view.findViewById<ListView>(R.id.Listview)*/
            val listView =view.findViewById(R.id.listview) as ListView
            /*
            //val listView = ListView(this)
            //setContentView(listView)
            val adapter = ArrayAdapter<String>(activity,android.R.layout.activity_list_item)
            var i = 0
            bufferedReader.lineSequence().forEach {
                Log.d("maita", "data;" + it.split(",")[0])
                adapter.insert(it.split(",")[0],i)
                i++
            }

            listView.adapter=adapter
*/
            val adapter = FlowerListAdapter(activity!!, flowers)
            listView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

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