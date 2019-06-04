package chen.vike.c680.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import chen.vike.c680.activity.ServiceDetailsActivity
import chen.vike.c680.bean.GuZhuBean
import chen.vike.c680.views.CircleImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lht.vike.a680_v1.R
import java.util.*

/**
 * Created by lht on 2017/2/23.
 */

class MyListViewadapter(private val context: Context?, list: MutableList<GuZhuBean.CainixihuanBean>) : BaseAdapter() {
    //用于测试所以用的默认权限，上线要改回私有的
    internal var list_info: MutableList<GuZhuBean.CainixihuanBean>? = ArrayList()

    internal lateinit var id: ID

    init {
        this.list_info = list
    }

    fun addlist(list: List<GuZhuBean.CainixihuanBean>) {

        list_info!!.addAll(list)

    }

    fun refresh() {
        list_info!!.clear()
    }

    override fun getCount(): Int {
        return list_info!!.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var mConvertView = convertView
        if (context != null) {
            if (mConvertView == null || mConvertView.tag == null) {

                mConvertView = LayoutInflater.from(context).inflate(R.layout.listview_fwzs, null)
                id = ID()
                id.iv_touxiang = mConvertView!!.findViewById(R.id.rmtjfw_img)
                id.tv_title = mConvertView.findViewById(R.id.guzhu_fuwu_title)
                id.tv_money = mConvertView.findViewById(R.id.guzhu_fuwu_jine)
                id.tv_pingfen = mConvertView.findViewById(R.id.guzhu_fuwu_pingfen)
                id.tv_count = mConvertView.findViewById(R.id.guzhu_fuwu_chengjiaol)
                id.tv_city = mConvertView.findViewById(R.id.guzhu_fuwu_address)
                id.head_img = mConvertView.findViewById(R.id.guzhu_fuwu_head_img)
                id.gongsiName = mConvertView.findViewById(R.id.guzhu_fuwu_gongsi_name)
                mConvertView.tag = id

            } else {

                id = mConvertView.tag as ID

            }

            val options = RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)

            Glide.with(context)
                    .load(list_info!![position].imgurl)
                    .apply(options)
                    .into(id.iv_touxiang!!)

            Glide.with(context).load(list_info!![position].imgurl)
                    .apply(options)
                    .into(id.head_img!!)

            if (list_info != null && list_info!!.size > 0) {
                id.tv_title!!.text = list_info!![position].fuwu_name
                id.tv_money!!.text = list_info!![position].price + ""
                id.tv_pingfen!!.text = "服务评分 " + list_info!![position].pingfen
                id.tv_count!!.text = "已成交 " + list_info!![position].deal_count + "笔"
                id.tv_city!!.text = list_info!![position].cityname
                id.gongsiName!!.text = list_info!![position].dianpu_name
                mConvertView.setOnClickListener {
                    val mIntent = Intent(context, ServiceDetailsActivity::class.java)
                    mIntent.putExtra("ID", list_info!![position].fuwu_id)
                    context.startActivity(mIntent)
                }

            }
        }
        return mConvertView!!
    }

    internal inner class ID {
        var iv_touxiang: ImageView? = null
        var tv_title: TextView? = null
        var tv_money: TextView? = null
        var tv_city: TextView? = null
        var tv_count: TextView? = null //成交量
        var tv_pingfen: TextView? = null
        var gongsiName: TextView? = null
        var head_img: CircleImageView? = null
    }
}

