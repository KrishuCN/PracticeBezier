package chen.vike.c680.activity;

/**
 * Created by Mr.Z on 2016/8/23.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import chen.vike.c680.views.TuKuMyImageView;
import chen.vike.c680.adapter.TuKuChildAdapter;
import chen.vike.c680.tools.TuKuNativeImageLoader;

public class TuKuShowImageActivity extends Activity implements View.OnClickListener{
    private GridView mGridView;
    private List<String> list;
    private TuKuChildAdapter adapter;
    private TextView number_text;
    private LinearLayout wancheng_text;
    private int NUMBER=0;
    int TOTAL = 0;
//    public List<String> stringList =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_activity);
        wancheng_text=(LinearLayout) findViewById(R.id.wancheng_text);
        number_text=(TextView) findViewById(R.id.number_text);
        try {
            int number = Integer.parseInt(getIntent().getStringExtra("number"));
            TOTAL= number;
        }catch (Exception e){

        }
        number_text.setText(String.valueOf(0));
        mGridView = (GridView) findViewById(R.id.child_grid);
        list = getIntent().getStringArrayListExtra("data");
        mGridView.setAdapter(ChildAdapter);
        wancheng_text.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==wancheng_text){
            //退出然后拿到自己想要的东西回去，传给服务器。
            List<Integer> lists=getSelectItems();
            for(int i=0;i<lists.size();i++){
                PickPhotoActivity.Companion.getStringList().add(list.get(lists.get(i)));
            }
            setResult(2);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        Toast.makeText(this, "选中 " + adapter.getSelectItems().size() + " item", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }
    //加载列表
    private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
  BaseAdapter ChildAdapter=new BaseAdapter() {
    private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
    /**
     * 用来存储图片的选中情况
     */


    @Override
    public int getCount() {
        if(list != null) {
            return list.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }else {
            return 0;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        String path = list.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(TuKuShowImageActivity.this).inflate(R.layout.grid_child_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (TuKuMyImageView) convertView.findViewById(R.id.child_image);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
            //用来监听ImageView的宽和高
            viewHolder.mImageView.setOnMeasureListener(new TuKuMyImageView.OnMeasureListener() {

                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width, height);
                }
            });

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mImageView.setImageResource(R.mipmap.friends_sends_pictures_no);
        }
            viewHolder.mImageView.setTag(path);
            viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //如果是未选中的CheckBox,则添加动画
                    if(NUMBER<TOTAL){
                    if(!mSelectMap.containsKey(position) || !mSelectMap.get(position)){
                        addAnimation(viewHolder.mCheckBox);
                    }
                    if(isChecked){
                        NUMBER+=1;
                    }else{
                        NUMBER-=1;
                    }
                    number_text.setText(String.valueOf(NUMBER)+"/"+String.valueOf(TOTAL));
                    mSelectMap.put(position, isChecked);
                    }else if(NUMBER==TOTAL) {
                        List<Integer> lists=getSelectItems();
                        for(int i=0;i<lists.size();i++){
                            if(position==lists.get(i)){
                                NUMBER-=1;
                                number_text.setText(String.valueOf(NUMBER)+"/"+String.valueOf(TOTAL));
                                mSelectMap.put(position, isChecked);
                            }else{
                                    viewHolder.mCheckBox.setChecked(false);
                            }
                        }
                    }
                }
            });
        viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(position) ? mSelectMap.get(position) : false);

        //利用NativeImageLoader类加载本地图片
        Bitmap bitmap = TuKuNativeImageLoader.getInstance().loadNativeImage(path, mPoint, new TuKuNativeImageLoader.NativeImageCallBack() {

            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
                if(bitmap != null && mImageView != null){
                    mImageView.setImageBitmap(bitmap);
                }
            }
        });

        if(bitmap != null){
            viewHolder.mImageView.setImageBitmap(bitmap);
        }else{
            viewHolder.mImageView.setImageResource(R.mipmap.friends_sends_pictures_no);
        }

        return convertView;
    }

    /**
     * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
     * @param view
     */
    private void addAnimation(View view){
        float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }


    /**
     * 获取选中的Item的position
     * @return
     *
     */
//    List<Integer> intlist = new ArrayList<Integer>();



     class ViewHolder{
        public TuKuMyImageView mImageView;
        public CheckBox mCheckBox;
    }




};
    public List<Integer> getSelectItems(){
        List<Integer> list = new ArrayList<Integer>();
        for(Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();){
            Map.Entry<Integer, Boolean> entry = it.next();
            if(entry.getValue()){
                list.add(entry.getKey());
            }
        }

        return list;
    }
}
