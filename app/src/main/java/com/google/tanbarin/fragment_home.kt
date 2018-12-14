package com.google.tanbarin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.InputStreamReader



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
        try {
            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open("tasteful-buildings.csv")))

            //val listView = view.findViewById<ListView>(R.id.Listview)
            val listView =view.findViewById(R.id.listview) as ListView
            //val listView = ListView(this)
            //setContentView(listView)
            val adapter = ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1)
            var i = 0
            bufferedReader.lineSequence().forEach {
                Log.d("maita", "data;" + it.split(",")[0])
                adapter.insert(it.split(",")[0],i)
                i++
            }

            listView.adapter=adapter


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