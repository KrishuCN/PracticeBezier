package chen.vike.c680.WeiKe.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.lht.vike.a680_v1.R

import java.util.ArrayList

import chen.vike.c680.activity.ShopDetailsActivity
import chen.vike.c680.bean.WeikeBean
import chen.vike.c680.tools.ImageLoadUtils

/**
 * Created by lht on 2017/2/28.
 */

class WeiKeRuZhuAdapter(private val context: Context, list: ArrayList<WeikeBean.ZuixinruzhuListBean>) : BaseAdapter() {
    private var list_info = ArrayList<WeikeBean.ZuixinruzhuListBean>()

    internal lateinit var id: ID

    init {
        this.list_info = list
    }

    fun addlist(list: List<WeikeBean.ZuixinruzhuListBean>) {

        list_info.addAll(list)

    }

    fun refresh() {
        list_info.clear()
    }

    override fun getCount(): Int {
        return list_info.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var myConvertView = convertView

        if (myConvertView == null || myConvertView.tag == null) {
            myConvertView = LayoutInflater.from(context).inflate(R.layout.weike_page_ruzhu_item_item, null)
            id = ID()
            id.head = myConvertView!!.findViewById<View>(R.id.wk_ruzhu_fw_img) as ImageView
            id.title = myConvertView.findViewById<View>(R.id.ruzhu_fw_title) as TextView
            id.viptype = myConvertView.findViewById<View>(R.id.ruzhu_fw_dengji_img) as ImageView
            id.cjl = myConvertView.findViewById<View>(R.id.ruzhu_fw_cjl) as TextView
            id.hpl = myConvertView.findViewById<View>(R.id.ruzhu_fw_haop) as TextView
            id.shch = myConvertView.findViewById<View>(R.id.ruzhu_fw_shanc) as TextView
            myConvertView.tag = id
        } else {
            id = myConvertView.tag as ID
        }
        ImageLoadUtils.display(context,id.head!!,list_info[position].imageurl)
        id.title!!.text = list_info[position].vkname
        id.shch!!.text = list_info[position].fanwei
        id.hpl!!.text = list_info[position].goodval
        id.cjl!!.text = list_info[position].cjnum
        Glide.with(context).load(list_info[position].check_imgurl).into(id.viptype!!)
        myConvertView.setOnClickListener {
            val mIntent = Intent(context, ShopDetailsActivity::class.java)
            mIntent.putExtra("ID", list_info[position].userid)
            context.startActivity(mIntent)
        }

        return myConvertView
    }

    internal inner class ID {
        var head: ImageView? = null
        var viptype: ImageView? = null
        var title: TextView? = null
        var cjl: TextView? = null
        var hpl: TextView? = null
        var shch: TextView? = null

    }
}
