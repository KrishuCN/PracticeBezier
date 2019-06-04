package chen.vike.c680.views;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

import chen.vike.c680.bean.GuZhuPageGson;
import chen.vike.c680.tools.DisplayUtil;

/**
 * android banner图
 */
public class BannerView extends FrameLayout implements OnPageChangeListener {

    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mDotLl;
    private List<GuZhuPageGson.FuwushangListBean> mList;

    private List<ImageView> dotList = null;
    private MyAdapter mAdapter = null;
    private Handler mHandler = null;
    private AutoRollRunnable mAutoRollRunnable = null;

    private int prePosition = 0;

    private HeaderViewClickListener headerViewClickListener;

    public BannerView(Context context)
    {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
        initData();
        initListener();
    }

    //初始化view
    private void initView()
    {
        View.inflate(mContext, R.layout.view_fwstj_lunbo, this);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mDotLl = (LinearLayout) findViewById(R.id.ll_dot);
        //让banner的高度是屏幕的1/4
        ViewGroup.LayoutParams vParams = mViewPager.getLayoutParams();
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            vParams.height = (int) (DisplayUtil.getMobileHeight(mContext) * 0.25);
        } else
        {
            vParams.height = (int) (DisplayUtil.getMobileWidth(mContext) * 0.35);
        }
        mViewPager.setLayoutParams(vParams);
    }

    //初始化数据
    private void initData()
    {
        dotList = new ArrayList<>();
        mAutoRollRunnable = new AutoRollRunnable();
        mHandler = new Handler();
        mAdapter = new MyAdapter(mContext);
    }

    private void initListener()
    {
        mViewPager.addOnPageChangeListener(this);
    }


    /**
     * 设置数据
     *
     * @param urlList
     */
    public void setImgUrlData(List<GuZhuPageGson.FuwushangListBean> urlList)
    {
        this.mList = urlList;
        if (mList != null && !mList.isEmpty())
        {
            //清空数据
            dotList.clear();
            mDotLl.removeAllViews();
            ImageView dotIv;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < mList.size(); i++)
            {
                dotIv = new ImageView(mContext);
                if (i == 0)
                {
                    dotIv.setBackgroundResource(R.mipmap.yuandian_fuwu);
                } else
                {
                    dotIv.setBackgroundResource(R.mipmap.yuandian);
                }
                //设置点的间距
                params.setMargins(0, 0, DisplayUtil.dip2px(mContext, 5), 0);
                dotIv.setLayoutParams(params);

                //添加点到view上
                mDotLl.addView(dotIv);
                //添加到集合中, 以便控制其切换
                dotList.add(dotIv);
            }
        }

        mAdapter = new MyAdapter(mContext);
        mViewPager.setAdapter(mAdapter);

        //设置viewpager初始位置, +10000就够了
        mViewPager.setCurrentItem(urlList.size() + 10000);
        startRoll();
    }


    /**
     * 设置点击事件
     *
     * @param headerViewClickListener
     */
    public void setOnHeaderViewClickListener(HeaderViewClickListener headerViewClickListener)
    {
        this.headerViewClickListener = headerViewClickListener;
    }


    //开始轮播
    public void startRoll()
    {
        mAutoRollRunnable.start();
    }

    // 停止轮播
    public void stopRoll()
    {
        mAutoRollRunnable.stop();
    }

    private class AutoRollRunnable implements Runnable {

        //是否在轮播的标志
        boolean isRunning = false;

        public void start()
        {
            if (!isRunning)
            {
                isRunning = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, 5000);
            }
        }

        public void stop()
        {
            if (isRunning)
            {
                mHandler.removeCallbacks(this);
                isRunning = false;
            }
        }

        @Override
        public void run()
        {
            if (isRunning)
            {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                mHandler.postDelayed(this, 5000);
            }
        }
    }

    public interface HeaderViewClickListener {
        void HeaderViewClick(int position);
    }

    private class MyAdapter extends PagerAdapter {

        //为了复用
        private List<View> imgCache = new ArrayList<>();
        private Context context;

        public MyAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public int getCount()
        {
            //无限滑动
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o)
        {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container1, final int position)
        {

            View container;
            ImageView iv = null;
            TextView title = null;
            TextView fanwei = null;
            TextView ok_count = null;
            ImageView chengxindu = null;
            TextView haopin = null;

            //获取ImageView对象
//            if (imgCache.size() > 0) {
//                container = imgCache.remove(0);
//            } else {
            container = LayoutInflater.from(context).inflate(R.layout.guzhu_tjfws_item, null);
            iv = (ImageView) container.findViewById(R.id.vp_iv);
            title = (TextView) container.findViewById(R.id.vp_tv);
            fanwei = (TextView) container.findViewById(R.id.tv7);
            ok_count = (TextView) container.findViewById(R.id.tv2);
            chengxindu = (ImageView) container.findViewById(R.id.vp_cx_iv);
            haopin = (TextView) container.findViewById(R.id.tv4);

            //这儿写数据加载，数据通过方法setImgUrlData传进来

            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


            Glide.with(mContext).load(mList.get(position % mList.size()).getImgurl()).apply(options).into(iv);
            title.setText(mList.get(position % mList.size()).getShopname());
            fanwei.setText("服务范围：" + mList.get(position % mList.size()).getFanwei());
            ok_count.setText(mList.get(position % mList.size()).getCjnum());
            switch (mList.get(position % mList.size()).getChengxindu())
            {
                case "1":
                    chengxindu.setImageResource(R.mipmap.cx_11);
                    break;
                case "2":
                    chengxindu.setImageResource(R.mipmap.cx_21);
                    break;
                case "3":
                    chengxindu.setImageResource(R.mipmap.cx_31);
                    break;
                case "4":
                    chengxindu.setImageResource(R.mipmap.cx_41);
                    break;
                case "5":
                    chengxindu.setImageResource(R.mipmap.cx_51);
                    break;
                default:
                    chengxindu.setImageResource(R.mipmap.cx_11);
                    break;
            }
            haopin.setText(mList.get(position % mList.size()).getGoodval());


            container.setOnTouchListener(new OnTouchListener() {
                private int downX = 0;
                private long downTime = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            mAutoRollRunnable.stop();
                            //获取按下的x坐标
                            downX = (int) v.getX();
                            downTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            mAutoRollRunnable.start();
                            int moveX = (int) v.getX();
                            long moveTime = System.currentTimeMillis();
                            if (downX == moveX && (moveTime - downTime < 500))
                            {//点击的条件
                                //轮播图回调点击事件
                                headerViewClickListener.HeaderViewClick(position % mList.size());
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            mAutoRollRunnable.start();
                            break;
                    }
                    return true;
                }
            });

            container1.addView(container);
            return container;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            if (object != null && object instanceof View)
            {
                View iv = (View) object;
                container.removeView(iv);
                imgCache.add(iv);
            }
        }
    }

    @Override
    public void onPageSelected(int position)
    {
        dotList.get(prePosition).setBackgroundResource(R.mipmap.yuandian_fuwu);
        dotList.get(position % dotList.size()).setBackgroundResource(R.mipmap.yuandian_xuan);
        prePosition = position % dotList.size();
    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {

    }


    //停止轮播
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        stopRoll();
    }
}
