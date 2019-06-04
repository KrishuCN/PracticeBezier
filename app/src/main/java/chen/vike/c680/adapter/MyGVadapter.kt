package chen.vike.c680.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.lht.vike.a680_v1.R

import chen.vike.c680.bean.GridViewInfoBean

/**
 * Created by lht on 2017/2/22.
 */

class MyGVadapter(private val context: Context?, private val list: List<GridViewInfoBean>?) : BaseAdapter() {

    private var id: ID? = null

    override fun getCount(): Int {

        return if (list!!.isEmpty()) {
            0
        } else list.size

    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null || convertView.tag == null) {
            if (context != null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null)
            }
            id = ID()
            id!!.image = convertView!!.findViewById(R.id.gv_iv)
            id!!.name = convertView.findViewById(R.id.gv_tv)
            convertView.tag = id
        } else {
            id = convertView.tag as ID
        }
        if (list != null && list.isNotEmpty()) {
            id!!.image!!.setImageResource(list[position].image)
            id!!.name!!.text = list[position].name
        }
        return convertView
    }

    internal inner class ID {
        internal var image: ImageView? = null
        internal var name: TextView? = null
    }

}
