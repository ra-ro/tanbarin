package com.google.tanbarin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBAcl
import com.nifcloud.mbaas.core.NCMBFile


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


class fragment_noti : Fragment.AppCompatActivity() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        //********** SDKの初期化 **********
        NCMB.initialize(applicationContext, applicationKey, clientKey)
        //▼▼▼起動時に処理▼▼▼


        //▲▲▲起動時に処理▲▲▲
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noti, container, false)
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

/*


class fragment_noti: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ********** SDKの初期化 **********
        NCMB.initialize(applicationContext, applicationKey, clientKey)
        //▼▼▼起動時に処理▼▼▼


        //▲▲▲起動時に処理▲▲▲
    }


}*/
