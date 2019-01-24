package com.google.tanbarin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_noti.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import android.widget.PopupWindow
import com.google.tanbarin.R.layout.popup_layout
import android.support.v4.app.DialogFragment
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.*
import android.view.Window.FEATURE_NO_TITLE





// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

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
    R.drawable.omomuki40,
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
R.drawable.kankou26
)
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
                view.buildImage
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
/*
class TestDialog() : DialogFragment() {
    //ダイアログのタイトルを保存する変数　ただここにタイトルを保存しただけではスマホ縦横時に消えてしまう
    //var mTitle: String by Delegates.notNull<String>()
    var mTitle: String = ""

    //コンストラクタで引数を渡すことができないのでBuilderパターンを利用する
    companion object {
        //Builderでタイトルを渡し上げて、このクラスのインスタンスを返してあげる
        fun Builder(title: String): TestDialog {
            //Bundleを利用してタイトルを一時的に保存する
            val bundle: Bundle = Bundle().also { it.putString("Title", title) }
            val testDialog: TestDialog = TestDialog().also { it.arguments = bundle }
            return testDialog
        }
    }

    //onCreate内でメンバ変数に保存すれば、スマホを横に動かしても保存される
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //argumentsから保存したタイトル名を取得するのでメンバ変数に保存する
        mTitle = arguments?.getString("Title") as String
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(activity!!)

        //タイトルを設定する
        dialogBuilder.setTitle(mTitle)
        return dialogBuilder.create()
    }
}*/
/*
class ConfirmDialog : DialogFragment() {

    var title = "title"
    var msg = "msg"
    var okText = "OK"
    var cancelText = "cancel"
    /** ok押下時の挙動 */
    var onOkClickListener : DialogInterface.OnClickListener? = null
    /** cancel押下時の挙動 デフォルトでは何もしない */
    var onCancelClickListener : DialogInterface.OnClickListener? = DialogInterface.OnClickListener { _, _ -> }

    companion object {
        fun newInstance(): ConfirmDialog {
            val instance = ConfirmDialog()
            return instance
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
            .setMessage(msg)
            .setPositiveButton(okText, onOkClickListener)
            .setNegativeButton(cancelText, onCancelClickListener)
        // Create the AlertDialog object and return it
        return builder.create()
    }
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? {
        val content: View = i.inflate(R.layout.popup_layout, c)
        return content
    }

    override fun onPause() {
        super.onPause()
        // onPause でダイアログを閉じる場合
        dismiss()
    }
}
*/
class AddListDialog: DialogFragment() {

    companion object {
        fun newInstance(): AddListDialog {
            val instance = AddListDialog()
            return instance
        }
    }

    var name: String? = null
    var detail: String? = null
    var imageNum: Int? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (getArguments() != null) {
            name = getArguments()?.getString("name","");
            detail = getArguments()?.getString("detail","")
            imageNum = getArguments()?.getInt("imageNum",0)

        }
        //val dialog = Dialog(activity!!)
        // タイトル非表示
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        // フルスクリーン
        dialog.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setContentView(R.layout.popup_layout)
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        // 背景を透明にする
        //dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // ボタンのリスナ
        dialog.findViewById<Button>(R.id.close_button).setOnClickListener {
                dismiss()
        }
        dialog.findViewById<TextView>(R.id.popName).text = name
        dialog.findViewById<TextView>(R.id.popDetail).text = detail
        Log.d("naraki", "data;" + imageNum)
        dialog.findViewById<ImageView>(R.id.popImage).setImageBitmap(BitmapFactory.decodeResource(activity!!.resources, images[imageNum!!]))


        return dialog
    }
/*
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? {
        val content: View = i.inflate(R.layout.popup_layout, c)
        return content
    }
*/
}
class fragment_home : android.support.v4.app.Fragment() {
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

        val assetManager = activity!!.getResources().getAssets()
        val listView = view.findViewById(R.id.listview) as ListView
        val list = mutableListOf<openData>()
        var i = 0
/*
        try {
            // CSVファイルの読み込み
            val `is` = assetManager.open("tasteful-buildings.csv")
            val inputStreamReader = InputStreamReader(`is`)
            val bufferReader = BufferedReader(inputStreamReader)
                var line : String?
            do {
                line = bufferReader.readLine()
                if (line == null)
                    break
                // 各行が","で区切られていて4つの項目があるとする
                val st = StringTokenizer(line, ",")
                val first = st.nextToken()
                val second = st.nextToken()
                val third = st.nextToken()
                val fourth = st.nextToken()
                //list.add(openData(first,  second, images[i]))
                Log.d("maita", "data;" + first + " " + second)
                i++
            } while (true)
            bufferReader.close()

            val adapter = listAdapter(activity!!, list)
            listView.adapter = adapter
        } catch (e: IOException) {
            e.printStackTrace()
        }
*/
        try {

            val bufferedReader = BufferedReader(InputStreamReader(assetManager.open("tasteful-buildings.csv")))
            val list = mutableListOf<openData>()
            var imagei=0
            var i = 0
            var str = ""

            bufferedReader.lineSequence().forEachIndexed() {index, it ->
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
                    list.add(openData(columnList[0], columnList[3], images[imagei]))
                    Log.d("maita", "data;" + columnList[0] + " " + columnList[1] +" " + columnList[2] + " " + imagei)
                    imagei++
                    str = ""
                } else{

                }
                i=0
            }

            val adapter = listAdapter(activity!!, list)
            listView.adapter = adapter

        } catch (e: Exception) {
            e.printStackTrace()
        }

        listview.setOnItemClickListener { adapterView, view, position, id ->
            val name = view.findViewById<TextView>(R.id.nameTextView).text
            val detail = view.findViewById<TextView>(R.id.descTextView).text
            val imageNum = view.findViewById<ImageView>(R.id.buildImage).drawable
            val imageNum2 = view.findViewById<ImageView>(R.id.buildImage).imageAlpha
            val imageNum3 = view.findViewById<ImageView>(R.id.buildImage).imageMatrix
            val imageNum4 = view.findViewById<ImageView>(R.id.buildImage).drawable
            val imageNum5 = view.findViewById<ImageView>(R.id.buildImage).drawable



            Log.d("naraki", "data1;" + imageNum)
            Log.d("naraki", "data2;" + imageNum2)
            Log.d("naraki", "data3;" + imageNum3)
            Log.d("naraki", "data4;" + imageNum4)
            Log.d("naraki", "data5;" + imageNum5)



            Toast.makeText(activity!!, "clicked: $name", Toast.LENGTH_LONG).show()

            //アクティビティではインスタンスを生成してshowメソッドを実施するだけ
            val newFlagment = AddListDialog.newInstance()
            val bundle = Bundle()
            bundle.putString("name", name.toString())
            bundle.putString("detail", detail.toString())
            bundle.putInt("imageNum", position)
            newFlagment.setArguments(bundle)
            newFlagment.show(fragmentManager, "dialog")
            //var dialog: ConfirmDialog = ConfirmDialog()
            //dialog.show(childFragmentManager, "c")


            //val frg = childFragmentManager.findFragmentById(R.id.)
            /*
            requireFragmentManager()
            childFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame, fragment_noti())
                .commit()
            */
            /*
            val popupWindow = PopupWindow(activity!!)
            popupWindow.setWindowLayoutMode(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            popupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.omomuki01))
            val popupView = layoutInflater.inflate(R.layout.popup_layout, null)
            popupWindow.setContentView(popupView)
            popupWindow.setOutsideTouchable(true)
            popupWindow.setFocusable(true)
            popupWindow.showAtLocation(activity!!.findViewById(R.id.close_button), Gravity.CENTER, 0, 0)
            */
            /*
            childFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame, listDetails())
                .commit()
             */
        }
    }
}