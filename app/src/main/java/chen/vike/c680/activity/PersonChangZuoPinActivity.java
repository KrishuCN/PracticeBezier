package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.vike.c680.adapter.GridViewAdapter;
import chen.vike.c680.bean.ImageBean;
import chen.vike.c680.bean.ZuoPinBean;
import chen.vike.c680.bean.ZuoPinInitBean;
import chen.vike.c680.main.PictureSelectorConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2018/5/2.
 * 修改作品
 */

public class PersonChangZuoPinActivity extends BaseStatusBarActivity {

    @BindView(R.id.change_zuopin_title)
    EditText changeZuopinTitle;
    @BindView(R.id.change_zuopin_price)
    EditText changeZuopinPrice;
    @BindView(R.id.change_zuopin_zhuangtai)
    Spinner changeZuopinZhuangtai;
    @BindView(R.id.change_gridView)
    GridView changeGridView;
    @BindView(R.id.change_zuopin_tijiao)
    Button changeZuopinTijiao;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    private Context mContext;
    private String Ztai;
    private String itemId = "";
    private final static String INITZUOPIN = "http://app.680.com/api/v4/faxian_fabu_zuopin.ashx";
    private final static String imgURL = "http://app.680.com/api/v4/upload_list.ashx";
    private final static int INITERRO = 0X123;
    private final static int INITSUCCES = 0X124;
    private final static int CHANGEERRO = 0X125;
    private final static int CHANGESUCCES = 0X126;
    private final static int ERROR = 0x127;
    private final static int IMGLOAD = 0x128;
    private final static int ZERROR = 0x136;
    private final static int ZPLOAD = 0x137;
    private final int NETWORK_EXCEPTION = 0X111;
    private List<String> imgpaths = new ArrayList<>();
    private ArrayList<File> files = new ArrayList<>();
    private LoadingDialog ld;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_zuopin_page);
        mContext = this;
        ButterKnife.bind(this);
        getTitle().setText("修改作品");

        itemId = getIntent().getStringExtra("itemid");
        initGridView();
        setSp();
        zuoPinData();
        btnListener();

    }

    private void btnListener() {
        changeZuopinTijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = changeZuopinTitle.getText().toString();
                if (changeZuopinTitle.getText().toString().equals("")) {
                    Toast.makeText(PersonChangZuoPinActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                } else if (changeZuopinPrice.getText().toString().equals(" ")) {
                    Toast.makeText(PersonChangZuoPinActivity.this, "请输入价格", Toast.LENGTH_SHORT).show();
                } else {
                    ld = new LoadingDialog(PersonChangZuoPinActivity.this).setMessage("提交中....");
                    ld.show();
                    uploadZuopin();
                }
            }
        });
    }

    /**
     * 作品数据
     */
    private ZuoPinInitBean zuoPinInitBean;

    private void zuoPinData() {
        Map<String, Object> map = new HashMap<>();
        map.put("act", "init");
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        map.put("zid", itemId);
        OkhttpTool.Companion.getOkhttpTool().post(INITZUOPIN, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try {
                    String s = response.body().string();
                    Log.e("initZuopin", s);
                    zuoPinInitBean = new Gson().fromJson(s, ZuoPinInitBean.class);
                    if (zuoPinInitBean != null) {
                        if (zuoPinInitBean.getErr_code() == 0) {
                            handler.sendEmptyMessage(INITSUCCES);
                        } else {
                            handler.sendEmptyMessage(INITERRO);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }

    /**
     * 作品状态
     */
    private void setSp() {
        ArrayAdapter<CharSequence> province_adapter = ArrayAdapter.createFromResource(this, R.array.zuopin_ztai, android.R.layout.simple_spinner_item);
        province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeZuopinZhuangtai.setAdapter(province_adapter);
        changeZuopinZhuangtai.setSelection(1, true);
        changeZuopinZhuangtai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Ztai = changeZuopinZhuangtai.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 上传图片返回
     */
    private ImageBean imageBean;
    private String imagHttpPath;

    private void updateImg() {
        Map<String, Object> mapImg = new HashMap<>();
        mapImg.put("userid", MyApplication.userInfo.getUserID());
        mapImg.put("type", "zuopin");
        // mapImg.put("debug","1");
        OkhttpTool.Companion.getOkhttpTool().updateMoreImg(imgURL, PersonUpdateZuoPinActivity.Companion.getPATH_FILES(), mapImg, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try {
                    String s = response.body().string();
                    Log.e("fileIMG", s + "");
                    imageBean = new Gson().fromJson(s, ImageBean.class);
                    if (imageBean != null) {
                        if (imageBean.getError() == 1) {
                            handler.sendEmptyMessage(ERROR);
                        } else {
                            imagHttpPath = imageBean.getUrl();
                            handler.sendEmptyMessage(IMGLOAD);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 提交修改
     */
    private ZuoPinBean zuoPinBean;

    private void uploadZuopin() {
        Map<String, Object> zuoMap = new HashMap<>();
        zuoMap.put("userid", MyApplication.userInfo.getUserID());
        zuoMap.put("act", "edit");
        zuoMap.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        zuoMap.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        zuoMap.put("crv_title", changeZuopinTitle.getText() + "");
        zuoMap.put("price", changeZuopinPrice.getText() + "");
        zuoMap.put("zid", itemId);
        if (Ztai.equals("已中标")) {
            zuoMap.put("iszb", "1");
        } else {
            zuoMap.put("iszb", "0");
        }
        if (mPicList.size() > 0) {
            switch (mPicList.size()) {
                case 1:
                    zuoMap.put("fm", mPicList.get(0));
                    break;
                case 2:
                    zuoMap.put("fm", mPicList.get(0));
                    zuoMap.put("zp", mPicList.get(1));
                    break;
                case 3:
                    zuoMap.put("fm", mPicList.get(0));
                    zuoMap.put("zp", mPicList.get(1));
                    zuoMap.put("zp1", mPicList.get(2));
                    break;
                case 4:
                    zuoMap.put("fm", mPicList.get(0));
                    zuoMap.put("zp", mPicList.get(1));
                    zuoMap.put("zp1", mPicList.get(2));
                    zuoMap.put("zp2", mPicList.get(3));
                    break;
                case 5:
                    zuoMap.put("fm", mPicList.get(0));
                    zuoMap.put("zp", mPicList.get(1));
                    zuoMap.put("zp1", mPicList.get(2));
                    zuoMap.put("zp2", mPicList.get(3));
                    zuoMap.put("zp3", mPicList.get(4));
                    break;
            }
        } else {
            Toast.makeText(PersonChangZuoPinActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
        }
        OkhttpTool.Companion.getOkhttpTool().post(INITZUOPIN, zuoMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try {
                    String s = response.body().string();
                    Log.e("zuopin", s + "");
                    zuoPinBean = new Gson().fromJson(s, ZuoPinBean.class);
                    if (zuoPinBean != null) {
                        if (zuoPinBean.getErr_code().equals("1")) {
                            handler.sendEmptyMessage(ZERROR);
                        } else {
                            handler.sendEmptyMessage(ZPLOAD);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (ld != null) {
                    ld.dismiss();
                }


            }
        });
    }

    //初始化展示上传图片的GridView
    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewAdapter(mContext, mPicList);
        changeGridView.setAdapter(mGridViewAddImgAdapter);
        changeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                    if (mPicList.size() == PersonUpdateZuoPinActivity.Companion.getMAX_SELECT_PIC_NUM()) {
                        //最多添加5张图片
                        viewPluImg(position);
                    } else {
                        //添加凭证图片
                        selectPic(PersonUpdateZuoPinActivity.Companion.getMAX_SELECT_PIC_NUM() - mPicList.size());
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }

    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(mContext, PersonPlusImageActivity.class);
        intent.putStringArrayListExtra(PersonUpdateZuoPinActivity.Companion.getIMG_LIST(), mPicList);
        intent.putExtra(PersonUpdateZuoPinActivity.Companion.getPOSITION(), position);
        startActivityForResult(intent, PersonUpdateZuoPinActivity.Companion.getREQUEST_CODE_MAIN());
    }

    /**
     * 打开相册或者照相机选择凭证图片，最多5张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private void selectPic(int maxTotal) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal);
    }

    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        if (PersonUpdateZuoPinActivity.Companion.getPATH_FILES().size() > 0) {
            PersonUpdateZuoPinActivity.Companion.getPATH_FILES().clear();
        }
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                File file = new File(compressPath);
                files.add(file);
                // mPicList.add(""); //把图片添加到将要上传的图片数组中
                PersonUpdateZuoPinActivity.Companion.getPATH_FILES().add(file);
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
        updateImg();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    refreshAdapter(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
        if (requestCode == PersonUpdateZuoPinActivity.Companion.getREQUEST_CODE_MAIN() && resultCode == PersonUpdateZuoPinActivity.Companion.getRESULT_CODE_VIEW_IMG()) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(PersonUpdateZuoPinActivity.Companion.getIMG_LIST()); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
            //  updateImg();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITERRO:
                    Toast.makeText(PersonChangZuoPinActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case INITSUCCES:
                    changeZuopinTitle.setText(zuoPinInitBean.getItem().getMiaoshu());
                    changeZuopinPrice.setText(zuoPinInitBean.getItem().getMoney());
                    if (zuoPinInitBean.getItem().getIszb().equals("是")) {
                        changeZuopinZhuangtai.setSelection(2, true);
                        Ztai = "已中标";
                    } else {
                        changeZuopinZhuangtai.setSelection(1, true);
                        Ztai = "未中标";
                    }
                    if (!zuoPinInitBean.getItem().getFm().equals("")) {
                        imgpaths.add(zuoPinInitBean.getItem().getFm());
                    }
                    if (!zuoPinInitBean.getItem().getZp().equals("")) {
                        imgpaths.add(zuoPinInitBean.getItem().getZp());
                    }
                    if (!zuoPinInitBean.getItem().getZp1().equals("")) {
                        imgpaths.add(zuoPinInitBean.getItem().getZp1());
                    }
                    if (!zuoPinInitBean.getItem().getZp2().equals("")) {
                        imgpaths.add(zuoPinInitBean.getItem().getZp2());
                    }
                    if (!zuoPinInitBean.getItem().getZp3().equals("")) {
                        imgpaths.add(zuoPinInitBean.getItem().getZp3());
                    }
                    if (!zuoPinInitBean.getItem().getZp4().equals("")) {
                        imgpaths.add(zuoPinInitBean.getItem().getZp4());
                    }
                    if (mPicList.size() > 0) {
                        mPicList.clear();
                    }
                    mPicList.addAll(imgpaths);
                    mGridViewAddImgAdapter.notifyDataSetChanged();
                    break;
                case ERROR:
                    Toast.makeText(PersonChangZuoPinActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    break;
                case IMGLOAD:
                    String[] imgPATH = imagHttpPath.split(",");
                    for (int i = 0; i < imgPATH.length; i++) {
                        mPicList.add(imgPATH[i]);
                    }
                    mGridViewAddImgAdapter.notifyDataSetChanged();
                    break;
                case ZERROR:
                    Toast.makeText(PersonChangZuoPinActivity.this, zuoPinBean.getErr_msg(), Toast.LENGTH_SHORT).show();
                    break;
                case ZPLOAD:
                    finish();
                    break;
                case NETWORK_EXCEPTION:
                    LhtTool.showNetworkException(PersonChangZuoPinActivity.this, msg);
                    break;
                    default:
                        break;
            }
        }
    };
}
