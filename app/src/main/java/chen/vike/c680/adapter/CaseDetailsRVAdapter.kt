package chen.vike.c680.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/04/01  下午 3:42
 *     desc  : Adapter单独提出来
 *
 * </pre>
 */
class CaseDetailsRVAdapter(context: Context, list: ArrayList<String>) : RecyclerView.Adapter<CaseDetailsRVAdapter.ImgViewHolder>() {
    private var context: Context? = null
    private var list: ArrayList<String>? = null

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImgViewHolder {
        val imageView = ImageView(context)
        imageView.adjustViewBounds = true
        imageView.maxWidth = context?.resources?.displayMetrics?.widthPixels!! - 40
        imageView.layoutParams = RecyclerView.LayoutParams(context?.resources?.displayMetrics!!.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        return ImgViewHolder(imageView)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(p0: ImgViewHolder, p1: Int) {
        when (p1) {
            0 -> p0.imageView.setPadding(20, 0, 20, 10)
            list?.size -> p0.imageView.setPadding(20, 10, 20, 0)
            else -> p0.imageView.setPadding(20, 10, 20, 10)
        }

        Glide.with(context!!).load(list!![p1] + "?123").into(p0.imageView)
    }


    inner class ImgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView as ImageView

    }
}