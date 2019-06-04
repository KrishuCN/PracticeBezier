//package chen.vike.c680.weike.frag;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
//import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
//import com.lht.vike.a680_v1.R;
//import com.stx.xhb.xbanner.XBanner;
//import com.stx.xhb.xbanner.transformers.Transformer;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import chen.vike.c680.adapter.WeikeAdapter;
//import chen.vike.c680.weike.frag.view.NormalFooterView;
//import chen.vike.c680.weike.frag.view.NormalHeadView;
//
///**
// * Created by lht on 2017/10/25.
// */
//
//public class WeikeContentFrag extends Fragment {
//    private View view;
//    private Context context;
//   private RecyclerView recyclerView;
//    private PullToRefreshLayout refreshLayout;
//    private LinearLayoutManager manager;
//    private WeikeAdapter weikeAdapter;
//    private XBanner headBanner;
//    private List<String> bannerImages = new ArrayList<>();//图片轮播地址集合
//    private List<String> bannerImageNames = new ArrayList<>();//图片轮播图片的名称
//    private List<String> contentLists = new ArrayList<>();//列表数据
//    private NormalHeadView headView = null;
//    private NormalFooterView footerView = null;
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getActivity();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = LayoutInflater.from(context).inflate(R.layout.fragment_weike_content_recy,null);
//        init();
//        return view;
//    }
//   private void init(){
//       iniview();
//   }
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//    /**
//     * 初始化
//     */
//    private void iniview(){
//        recyclerView = (RecyclerView) view.findViewById(R.id.weike_contnt_recy);
//        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.weike_content_refresh);
//        manager = new LinearLayoutManager(context);
//        headBanner = (XBanner) view.findViewById(R.id.weike_img_banner);
//        headView = new NormalHeadView(context);
//        footerView = new NormalFooterView(context);
//        setview();
//    }
//    private void setview(){
//       initList();
//         recyclerView.setLayoutManager(manager);
//        // weikeAdapter = new WeikeAdapter(contentLists,context);
//        recyclerView.setAdapter(weikeAdapter);
//        refreshListener();
//    }
//    public void initList() {
//        for (int i = 0; i < 10; i++) {
//            contentLists.add(String.valueOf(i));
//        }
//    }
//    /**
//     * 上拉下拉刷新的监听
//     */
//    private void refreshListener(){
//    refreshLayout.setHeaderView(headView);
//    refreshLayout.setFooterView(footerView);
//        refreshLayout.setRefreshListener(new BaseRefreshListener() {
//            @Override
//            public void refresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                           initList();
//                        handler.sendEmptyMessage(1);
//                        weikeAdapter.setLists(contentLists);
//                        weikeAdapter.notifyDataSetChanged();
//                    }
//                },2000);
//            }
//
//            @Override
//            public void loadMore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        handler.sendEmptyMessage(1);
//                        weikeAdapter.setLists(contentLists);
//                        refreshLayout.finishLoadMore();
//                    }
//                },2000);
//            }
//        });
//}
//    private void bannerData(){
//        headBanner.setData(bannerImages,bannerImages);
//        headBanner.setmAdapter(new XBanner.XBannerAdapter() {
//            @Override
//            public void loadBanner(XBanner banner, View view, int position) {
//                Glide.with(context).load(bannerImages.get(position)).into((ImageView) view);
//            }
//        });
//        // 设置XBanner的页面切换特效
//        headBanner.setPageTransformer(Transformer.Default);
//       // 设置XBanner页面切换的时间，即动画时长
//        headBanner.setPageChangeDuration(1000);
//    }
//}
