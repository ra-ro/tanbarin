package com.google.tanbarin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        try {

            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open("tasteful-buildings.csv")))

            bufferedReader.lineSequence().forEach {
                Log.d("maita", "data;" + it.split(",")[0])
                //ccccvvvvbbbb
            }

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