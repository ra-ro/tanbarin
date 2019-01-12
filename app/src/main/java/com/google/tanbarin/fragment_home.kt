package com.google.tanbarin

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils.split
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.tanbarin.R.drawable.e
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*
//import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import java.util.*


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

val images = listOf(
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
    R.drawable.omomuki40
/*
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

*/
)
/** CSVクラス定義**/
class   Customer   {
    var   id :   String ?   =   null
    var   name :   String ?   =   null
    var   address :   String ?   =   null
    var   age :   Int   =   0

    constructor ( )   { }
    constructor ( id :   String ? ,   name :   String ? ,   address :   String ? ,   age :   Int )   {
        this . id   =   id
        this . name   =   name
        this . address   =   address
        this . age   =   age
    }

    override  fun  toString ( ) :   String   {
        return   "Customer [id="   +   id   +   ", name="   +   name   +   ", address="   +   address   +   ", age="   +   age   +   "]"
    }
}
data class openData(val name : String, val desc: String, val imageId: Int)
data class ViewHolder(val nameTextView: TextView, val descTextView: TextView, val flowerImgView: ImageView)
class listAdapter(context: Context, datas: List<openData>) : ArrayAdapter<openData>(context, 0, datas) {
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

        val opend = getItem(position) as openData
        holder.nameTextView.text = opend.name
        holder.descTextView.text = opend.desc
        holder.flowerImgView.setImageBitmap(BitmapFactory.decodeResource(context.resources, opend.imageId))

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

        val listView = view.findViewById(R.id.listview) as ListView
        try {

            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open("tasteful-buildings.csv")))
            val list = mutableListOf<openData>()
            var i = 0
            /*
            bufferedReader.lineSequence().forEach {
                var listB = it.split(Regex("\\r\\n"))
                var columnList = listB.get(0).split(",")
                Log.d("maita", "data;" + columnList[0])
                list.add(openData( columnList[0],  columnList[1], images[i]))
                //adapter.insert(it.split(",")[0],i)
                i++
            }
            list.forEach {
                Log.d("kaito", "data:" + it.name + " " + it.desc + " " + it.imageId)
            }
            *//*
            private   val  CUSTOMER_ID_IDX   =   0
            private   val  CUSTOMER_NAME_IDX   =   1
            private   val  CUSTOMER_ADDRESS_IDX   =   2
            private   val  CUSTOMER_AGE   =   3
            fun main( args :   Array <String> ? )   {
                var   fileReader :   BufferedReader ?   =   null

                val  customers  = mutableListOf<openData>()
            var line = bufferedReader.readLine()
            while (line != null) {
                val  tokens   =   line . split ( "," )
                if   ( tokens . size   >   0 )   {
                    val  customer   =   Customer (
                        tokens [ CUSTOMER_ID_IDX ] ,
                        tokens [ CUSTOMER_NAME_IDX ] ,
                        tokens [ CUSTOMER_ADDRESS_IDX ] ,
                        Integer . parseInt ( tokens [ CUSTOMER_AGE ] ) )
                    customers . add ( customer )
                }


            }
            br.close()*/

            //listView.adapter=adapter
            val adapter = listAdapter(activity!!, list)
            listView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()


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
}