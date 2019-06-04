package chen.vike.c680.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import chen.vike.c680.bean.ServicePingjiaGson
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lht.vike.a680_v1.R

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/04/01  上午 11:19
 *     desc  : 服务详情评价Adapter
 *
 * </pre>
 */
class ServiceDetailsEvaluationAdapter(context: Context, list: ArrayList<ServicePingjiaGson.ListBean>) : RecyclerView.Adapter<ServiceDetailsEvaluationAdapter.PingjiaViewHolder>() {

    var context: Context? = null
    var list: ArrayList<ServicePingjiaGson.ListBean>? = null

    init {
        this.context = context
        this.list = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PingjiaViewHolder {
        return PingjiaViewHolder(LayoutInflater.from(context).inflate(R.layout.fuwu_pinjia_item, null))
    }

    override fun onBindViewHolder(p0: PingjiaViewHolder, p1: Int) {
        list?.let {
            p0.name.text = list!![p1].username_vk
            p0.content.text = Html.fromHtml(list!![p1].pj_con)
            p0.date.text = list!![p1].pj_time

            val options = RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)

            Glide.with(context!!).load(list!![p1].imageurl).apply(options).into(p0.imageView)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class PingjiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val imageView: ImageView
         val name: TextView
         val content: TextView
         val date: TextView

        init {
            imageView = itemView.findViewById(R.id.icon)
            name = itemView.findViewById(R.id.name)
            content = itemView.findViewById(R.id.content)
            date = itemView.findViewById(R.id.date)
        }

    }
}
