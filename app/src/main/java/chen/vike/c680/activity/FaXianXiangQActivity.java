package chen.vike.c680.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.JavaScriptInterface;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lht on 2018/4/2.
 *
 * 发现详情
 */

public class FaXianXiangQActivity extends BaseStatusBarActivity {

    @BindView(R.id.wb)
    WebView wb;
    @BindView(R.id.chongxin_jiazai)
    Button chongxinJiazai;
    @BindView(R.id.zanwu_wangluo)
    LinearLayout zanwuWangluo;

    private String url;
    private LoadingDialog ld;

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        ButterKnife.bind(this);

        ld = new LoadingDialog(this).setMessage("加载中....");
        // ld.show();
        url = getIntent().getStringExtra("weburl");
        if (url != null) {
            if (url.indexOf("?") > 0) {
                url = url + "&isapp=1";
            } else {
                url = url + "?isapp=1";
            }
        }

        getTitle().setText(getIntent().getStringExtra("title"));
        wb.requestFocusFromTouch();//支持获取手势焦点，输入用户名、密码或其他

        WebSettings webSettings = wb.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小

        webSettings.setSupportZoom(true);//支持缩放，默认为true。是下面那个的前提
        webSettings.setBuiltInZoomControls(true);//设置内置的缩放控件
        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
//        webSettings.setTextZoom(3);//设置文本的缩放倍数，默认为 100//设置成3你会看到很奇怪的现象

        //设为true会有泄露错误
        webSettings.setDisplayZoomControls(false);//隐藏原生的缩放控件

//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局

//        webSettings.setSupportMultipleWindows(true);//多窗口
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        webSettings.setAllowFileAccess(true);//设置可以访问文件
//        webSettings.setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        /**
         * “ncp”是js调用的  “callOnJs”webView上跳转回Activity的方法
         * “ ncp.callOnJs("case", t + "," + itemid + "," + cyid);”  js上调用跳转的例句
         * Android容器设置桥连对象
         */
        wb.addJavascriptInterface(new JavaScriptInterface(this), "ncp"); ///  ncp.callOnJs("case", t + "," + itemid + "," + cyid);

        //以下设置可自定义网页字体风格及大小，不过这会用不到
//        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
//        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
//        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8

        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                LhtTool.setCookieManager(url);
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                if (url.contains("tel:")) {

                    if (ActivityCompat.checkSelfPermission(FaXianXiangQActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return true;
                    }
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                    startActivity(intent);
                    //这个超连接,java已经处理了，webview不要处理了
                    return true;
                }

                if (getIntent().getStringExtra("title").equals("商标注册服务") || getIntent().getStringExtra("title").equals("版权登记服务")) {
                    LogUtils.d("==================url:" + url);
                    view.loadUrl(url + "?dover=2");
                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //在这里让加载动画结束
                if (null != ld) {
                    ld.dismiss();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                wb.setVisibility(View.GONE);
                zanwuWangluo.setVisibility(View.VISIBLE);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        wb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

//                // 增加Javascript异常监控
//                CrashReport.setJavascriptMonitor(webView, true);
//                if(newProgress==100){
//                    if (myDialog != null){
//                        myDialog.dismiss2();
//                    }
//                }
                super.onProgressChanged(view, newProgress);

            }
        });
        chongxinJiazai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LhtTool.setCookieManager(url);
                wb.loadUrl(url);
                wb.setVisibility(View.VISIBLE);
                zanwuWangluo.setVisibility(View.GONE);
            }
        });
        LhtTool.setCookieManager(url);
        wb.loadUrl(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 1) {
            LhtTool.setCookieManager(url);
            wb.loadUrl(url);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ld != null) {
            ld.dismiss();
        }
    }
}
