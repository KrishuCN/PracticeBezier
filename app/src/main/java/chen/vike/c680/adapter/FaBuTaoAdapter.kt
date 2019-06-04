package chen.vike.c680.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.lht.vike.a680_v1.R

import java.util.ArrayList

import chen.vike.c680.bean.TaoCanListBean
import chen.vike.c680.Interface.ViewItemClick

/**
 * Created by lht on 2018/5/9.
 */

class FaBuTaoAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var view: View
    var viewItemClick: ViewItemClick? = null
    internal var lists: MutableList<TaoCanListBean.TaocanListBean.ColnameListBean> = ArrayList()

    fun getLists(): List<TaoCanListBean.TaocanListBean.ColnameListBean> {
        return lists
    }

    fun setLists(lists: MutableList<TaoCanListBean.TaocanListBean.ColnameListBean>) {
        this.lists = lists
    }

    fun refresh() {
        if (lists.size > 0) {
            lists.clear()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.fabu_xueqiu_taocan_list_item, null)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder = holder as MyViewHolder
        myViewHolder.tcfb.setOnClickListener { viewItemClick!!.shortClick(position) }
        myViewHolder.taocanTitle.text = lists[position].tc_name
        myViewHolder.tcJiage.text = "￥" + lists[position].tc_price
        myViewHolder.tcYuanjia.text = "￥" + lists[position].tc_oldprice
        myViewHolder.tcs1.text = "· " + lists[position].tc_note1
        myViewHolder.tcs2.text = "· " + lists[position].tc_note2
        myViewHolder.tcs3.text = "· " + lists[position].tc_note3
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    private inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val taocanTitle: TextView = itemView.findViewById(R.id.taocan_title)
        internal val tcJiage: TextView = itemView.findViewById(R.id.taocan_jiage)
        internal val tcYuanjia: TextView = itemView.findViewById(R.id.taocan_yuanjia)
        internal val tcs1: TextView = itemView.findViewById(R.id.taocan_s1)
        internal val tcs2: TextView = itemView.findViewById(R.id.taocan_s2)
        internal val tcs3: TextView = itemView.findViewById(R.id.taocan_s3)
        internal val tcfb: Button = itemView.findViewById(R.id.taocan_btn_fa)

    }
}
