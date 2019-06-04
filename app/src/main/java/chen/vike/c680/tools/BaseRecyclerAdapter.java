package chen.vike.c680.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Z on 2016/5/30.
 *
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    protected final List<T> mData;
    protected final Context mContext;
    protected LayoutInflater mInflater;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;

    public BaseRecyclerAdapter(List<T> mData, Context mContext) {
        this.mData = (mData != null) ? mData : new ArrayList<T>();
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerViewHolder holder = new RecyclerViewHolder(mInflater.inflate(getItemLayoutId(viewType), parent, false), mContext);
       //点击后下面两个都会运行。刚刚进入通知列表时也会显示
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(holder.itemView, holder.getAdapterPosition(), (mData != null) ? mData.get(holder.getAdapterPosition()-1) : null);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition(), (mData != null) ? mData.get(holder.getAdapterPosition()-1) : null);
                    return true;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindData((RecyclerViewHolder) holder, position, mData.get(position));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    abstract public int getItemLayoutId(int viewType);

    abstract public void bindData(RecyclerViewHolder holder, int position, T item);

    public interface OnItemClickListener<T> {
         void onItemClick(View itemView, int pos, T item);
    }

    public interface OnItemLongClickListener<T> {
         void onItemLongClick(View itemView, int pos, T item);
    }


}
