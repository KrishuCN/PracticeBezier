package chen.vike.c680.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import chen.vike.c680.main.BaseActivity;
import chen.vike.c680.views.ZoomImageView;
import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lht on 2017/3/15.
 * 查看大图
 */

public class ReviewImageActivity extends BaseActivity{

    private List<String> mImgs = new ArrayList<>();
    private ImageView[] mImageViews;
    private int mPosition=0;
    private TextView mPages;
    private ViewPager mViewPager;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*
         * Use full screen window and translucent action bar
         */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x88000000));
            getSupportActionBar().hide();
            // Note: if you use ActionBarSherlock use here getSupportActionBar()
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewimage_activity);

        mPages= (TextView) findViewById(R.id.pages);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            mImgs.addAll(bundle.getStringArrayList("mImgs"));
            mPosition = bundle.getInt("positon");
        }

        mImageViews = new ImageView[mImgs.size()];
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(mPosition,true);
        mViewPager.setPageMargin((int) getResources().getDisplayMetrics().density * 10);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPages.setText((position+1)+"/"+mImgs.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPages.setText((mPosition+1)+"/"+mImgs.size());

    }


    private class SamplePagerAdapter extends PagerAdapter {
        private Handler backgroundHandler;

        public SamplePagerAdapter() {
            // Create a background thread and a handler for it
            final HandlerThread backgroundThread = new HandlerThread("backgroundThread");
            backgroundThread.start();
            backgroundHandler = new Handler(backgroundThread.getLooper());
        }


        @Override
        public int getCount() {
            return mImgs.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            final ZoomImageView zoomImageView = new ZoomImageView(container.getContext());
            Glide.with(ReviewImageActivity.this).load(mImgs.get(position)).into(zoomImageView);
            /*
             * Load the new bitmap in the background thread
             */
//            backgroundHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    // Load the bitmap here. You should control the bitmap size
//                    // using the BitmapFactory.Options.
////                    final Bitmap bitmap = BitmapFactory.decodeResource(
////                            zoomImageView.getResources(), bitmapResource);
////
////                    // Show the bitmap
////                    zoomImageView.post(new Runnable() {
////                        @Override
////                        public void run() {
////                            zoomImageView.setImageBitmap(bitmap);
////                        }
////                    });
//
//                }
//            });

            container.addView(zoomImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return zoomImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

            /*
             * Recycle the old bitmap to free up memory straight away
             */
            try {
                final ImageView imageView = (ImageView) object;
                final Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                imageView.setImageBitmap(null);
                bitmap.recycle();
            } catch (Exception e) {}
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
