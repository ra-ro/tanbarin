package com.google.tanbarin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import com.google.tanbarin.R.layout.fragment_dash
import kotlinx.android.synthetic.main.fragment_dash.*


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
                    putParcelableArrayList("list", list_omomuki!!)
                }
                return frg
            }
            1 -> {
                frg.arguments = args.apply {
                    putParcelableArrayList("list", list_kankou!!)
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


private lateinit var list_omomuki : ArrayList<datalist>
private lateinit var list_kankou : ArrayList<datalist>


class fragment_dash : Fragment() {
    // TODO: Rename and change types of parameters
    //********** APIキーの設定 **********
    val applicationKey:String = "4be64b73110568a79692b7fced842a43ea7f8330ac9672f490c38b1cae2a04f2"
    val clientKey:String = "a6432123d33c4004f4b054151487e7bc25dddbfbdd63ef603400f5e5bd2c981c"


    companion object {
        private const val list_o = "omo"
        private const val list_k = "kan"

        fun createInstance(list1:MutableList<datalist>, list2:MutableList<datalist>): fragment_dash {
            val frg = fragment_dash()
            val args = Bundle()

            args.putParcelableArrayList(list_o, ArrayList(list1))
            args.putParcelableArrayList(list_k, ArrayList(list2))

            frg.arguments = args
            return frg
        }
    }

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

        val args = arguments
        if (args == null) {
        } else {
            list_omomuki = args.getParcelableArrayList<datalist>(fragment_dash.list_o)!!
            list_kankou = args.getParcelableArrayList<datalist>(fragment_dash.list_k)!!

        }
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

    }


}
