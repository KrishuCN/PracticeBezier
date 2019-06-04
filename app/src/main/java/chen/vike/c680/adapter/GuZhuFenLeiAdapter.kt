package chen.vike.c680.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import chen.vike.c680.bean.GridViewInfoBean
import com.lht.vike.a680_v1.R

import java.util.ArrayList

import chen.vike.c680.Interface.ViewItemClick

/**
 * Created by lht on 2018/5/24.
 * 按分类找人才
 */

class GuZhuFenLeiAdapter(private val mContext: Context, list_fenlei: List<GridViewInfoBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var view: View
    private var list_fenlei = ArrayList<GridViewInfoBean>()
    var click: ViewItemClick? = null

    init {
        this.list_fenlei = list_fenlei as ArrayList<GridViewInfoBean>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        view = LayoutInflater.from(mContext).inflate(R.layout.guzhu_four_item_item, parent, false)
        return FenLeiViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val fenLeiViewHolder = holder as FenLeiViewHolder
        fenLeiViewHolder.let {
            fenLeiViewHolder.textView!!.text = list_fenlei[position].name
            fenLeiViewHolder.imageView.setImageResource(list_fenlei[position].image)
        }
        fenLeiViewHolder.imageView.setOnClickListener { click!!.shortClick(position) }

    }

    override fun getItemCount(): Int {
        return list_fenlei.size
    }

    private inner class FenLeiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var imageView: ImageView = itemView.findViewById(R.id.four_img)
        internal var textView: TextView? = null

        init {
            textView = itemView.findViewById(R.id.four_text)
        }
    }
}
