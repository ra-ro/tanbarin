package com.google.tanbarin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.ListView
import com.google.tanbarin.R.layout.fragment_dash
import android.view.*
import kotlinx.android.synthetic.main.fragment_dash.*
import kotlinx.android.synthetic.main.fragment_dash.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [fragment_dash.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [fragment_dash.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TestPagerAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {
    var mcontents = context
    //どのタブにどのFragmentを実装するか記述する
    //今回はテストなのですべて同じFragmentを実装している。
    override fun getItem(position: Int): Fragment {
        var args = Bundle()
        var frg = fragment_home()
        when (position) {
            0 -> {
                frg.arguments = args.apply {
                    putString("csvfile", "tasteful-buildings.csv")
                }
                return frg
            }
            1 -> {
                frg.arguments = args.apply {
                    putString("csvfile", "tourist-facilities.csv")
                }
                return frg
            }
            else -> {
                return fragment_noti()
            }
        }
    }

    //今回はテストなので4つのタブ固定にしている
    //実際のアプリではタブの数などを管理することもあるだろう
    override fun getCount(): Int {
        return 2
    }
}

class fragment_dash : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //ViewPagerに先ほど作成したAdapterのインスタンスを渡してあげる
        return inflater.inflate(R.layout.fragment_dash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = TestPagerAdapter(childFragmentManager, activity!!)

        //TabLayoutにViewPagerのインスタンスを渡すと自動的に実装してくれる
        tabLayout.setupWithViewPager(viewPager)

        //Tabへの処理はsetupWithViewPagerをした後だったら可能
        //ここではタブ名とアイコンを設定している
        tabLayout.getTabAt(0)?.also {
            it.text = "趣のある建物"
        }
        tabLayout.getTabAt(1)?.also {
            it.text = "観光名所"
        }
        /*
        for (i: Int in 0..tabLayout.tabCount) {
            tabLayout.getTabAt(i)?.also {
                //it.setIcon(R.mipmap.ic_launcher)
                //it.text = "TAB" + i.toString()
            }
        }
        */
        /*val adapter = object : FragmentPagerAdapter(childFragmentManager()) {
            override fun getItem(position: Int): Fragment? {
                when (position) {
                    0 -> return fragment_home()
                    1 -> return fragment_noti()
                    else -> return null
                }
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return tabTitle[position]
            }

            override fun getCount(): Int {
                return tabTitle.size
            }

        }
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        val listView = view.findViewById<ListView>(R.id.listview)

        viewPager.setOffscreenPageLimit(tabTitle.size)
        viewPager.setAdapter(adapter)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        */

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
