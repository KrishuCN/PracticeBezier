package chen.vike.c680.adapter

/**
 * Created by Mr.Z on 2016/8/23.
 */

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import chen.vike.c680.views.TuKuMyImageView
import chen.vike.c680.bean.TuKuImageBean
import chen.vike.c680.tools.TuKuNativeImageLoader

import com.lht.vike.a680_v1.R

class TuKuGroupAdapter(context: Context, private val list: List<TuKuImageBean>, private val mGridView: GridView) : BaseAdapter() {
    private val mPoint = Point(0, 0)//用来封装ImageView的宽和高的对象
    protected var mInflater: LayoutInflater

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    init {
        mInflater = LayoutInflater.from(context)
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        val mImageBean = list[position]
        val path = mImageBean.topImagePath
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = mInflater.inflate(R.layout.grid_group_item, null)
            viewHolder.mImageView = convertView!!.findViewById<View>(R.id.group_image) as TuKuMyImageView
            viewHolder.mTextViewTitle = convertView.findViewById<View>(R.id.group_title) as TextView
            viewHolder.mTextViewCounts = convertView.findViewById<View>(R.id.group_count) as TextView

            //用来监听ImageView的宽和高
            viewHolder.mImageView!!.setOnMeasureListener { width, height -> mPoint.set(width, height) }

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            viewHolder.mImageView!!.setImageResource(R.mipmap.friends_sends_pictures_no)
        }

        viewHolder.mTextViewTitle!!.text = mImageBean.folderName
        viewHolder.mTextViewCounts!!.text = Integer.toString(mImageBean.imageCounts)
        //给ImageView设置路径Tag,这是异步加载图片的小技巧
        viewHolder.mImageView!!.tag = path


        //利用NativeImageLoader类加载本地图片
        val bitmap = TuKuNativeImageLoader.getInstance().loadNativeImage(path, mPoint) { bitmap, path ->
            val mImageView = mGridView.findViewWithTag<View>(path) as ImageView
            if (bitmap != null && mImageView != null) {
                mImageView.setImageBitmap(bitmap)
            }
        }

        if (bitmap != null) {
            viewHolder.mImageView!!.setImageBitmap(bitmap)
        } else {
            viewHolder.mImageView!!.setImageResource(R.mipmap.friends_sends_pictures_no)
        }


        return convertView
    }


    class ViewHolder {
        var mImageView: TuKuMyImageView? = null
        var mTextViewTitle: TextView? = null
        var mTextViewCounts: TextView? = null
    }


}
