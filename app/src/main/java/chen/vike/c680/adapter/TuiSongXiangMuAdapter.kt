package chen.vike.c680.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import chen.vike.c680.activity.TaskDetailsActivity
import chen.vike.c680.bean.WeiKePageGson
import com.lht.vike.a680_v1.R

import java.util.ArrayList

import chen.vike.c680.bean.TuiSongServiceBean

/**
 * Created by lht on 2017/2/28.
 */

class TuiSongXiangMuAdapter(private val context: Context) : BaseAdapter() {
    private var list_info: MutableList<WeiKePageGson.ItemListBean>? = ArrayList()
    private var list_fuwu: MutableList<TuiSongServiceBean.ServicesBean> = ArrayList()

    var type = "项目"

    internal lateinit var id: ID

    fun getList_info(): List<WeiKePageGson.ItemListBean>? {
        return list_info
    }

    fun setList_info(list_info: MutableList<WeiKePageGson.ItemListBean>) {
        this.list_info = list_info
    }

    fun getList_fuwu(): List<TuiSongServiceBean.ServicesBean> {
        return list_fuwu
    }

    fun setList_fuwu(list_fuwu: MutableList<TuiSongServiceBean.ServicesBean>) {
        this.list_fuwu = list_fuwu
    }

    fun addlist(list: List<WeiKePageGson.ItemListBean>) {

        list_info!!.addAll(list)
    }

    fun addfulist(listfu: List<TuiSongServiceBean.ServicesBean>) {
        list_fuwu.addAll(listfu)
    }

    fun refresh() {
        if (type == "项目") {
            list_info!!.clear()
        } else {
            list_fuwu.clear()
        }
    }

    override fun getCount(): Int {
        return if (type == "项目") {
            list_info!!.size
        } else {
            list_fuwu.size
        }
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var mConvertView = convertView
        Log.e("tuisongtype", type)
        if (type == "项目") {
            id = ID()
            mConvertView = LayoutInflater.from(context).inflate(R.layout.listview_rmxm, null)
            id.tv = mConvertView.findViewById(R.id.rmxm_title)
            id.tv1 = mConvertView.findViewById(R.id.rmxm_content)
            id.tv2 = mConvertView.findViewById(R.id.money_weike)
            id.tv3 = mConvertView.findViewById(R.id.time_weike)
            id.tv4 = mConvertView.findViewById(R.id.type_weike)
            mConvertView.tag = id

            if (!list_info.isNullOrEmpty()) {
                if (!list_info!![position].itemname.isNullOrEmpty()){
                    id.tv!!.text = list_info!![position].itemname + ""
                }
                if (!list_info!![position].content.isNullOrEmpty()){
                    id.tv1!!.text = list_info!![position].content + ""
                }
                if (!list_info!![position].price.isNullOrEmpty()){
                    id.tv2!!.text = "￥" + list_info!![position].price + ""
                }
                if (!list_info!![position].endtime.isNullOrEmpty()){
                    id.tv3!!.text = list_info!![position].endtime + ""
                }
                id.tv4!!.text = ""

                mConvertView.setOnClickListener {
                    val mIntent = Intent(context, TaskDetailsActivity::class.java)
                    mIntent.putExtra("ID", list_info!![position].itemid)
                    context.startActivity(mIntent)
                }
            }
        }
        return mConvertView!!
    }

    internal inner class ID {
         var tv: TextView? = null
         var tv1: TextView? = null
         var tv2: TextView? = null
         var tv3: TextView? = null
         var tv4: TextView? = null

    }
}
