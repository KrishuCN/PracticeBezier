package chen.vike.c680.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.activity.MessageChatActivity;
import chen.vike.c680.activity.MessageSearchActivity;
import chen.vike.c680.activity.MessageSystemListActivity;
import chen.vike.c680.bean.MessageListGson;
import chen.vike.c680.main.MainActivity;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CircleImageView;
import chen.vike.c680.views.EmptySwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/2/22.
 * <p>
 * 出来未刷新
 */

public class MessageFragment extends Fragment {

    private Context con;
    private EmptySwipeRefreshLayout srl;
    private ListView lv;
    private ImageView iv_lxkf;
    private TextView et;
    private MessageListGson messageListGson;
    private MyGB myGB;
    //Fragment的View加载完毕的标记
    private boolean isViewCreate;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private List<MessageListGson.ListBean> list = new ArrayList();
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> map1 = new HashMap<>();
    private MessageListAdapter adapter;
    private final int GET_MESSAGE = 0x123;
    private final int NETWORK_EXCEPTION = 0X111;
    private final int ALL_UNREADNUM = 0X121;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = getActivity();
        IntentFilter inf = new IntentFilter();
        inf.addAction("lht.islht.refresh");
        myGB = new MyGB();
        con.registerReceiver(myGB, inf);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(con).inflate(R.layout.fragment_message, null);
        srl = (EmptySwipeRefreshLayout) v.findViewById(R.id.swipe_message);
        lv = (ListView) v.findViewById(R.id.lv_message);
        iv_lxkf = (ImageView) v.findViewById(R.id.message_lxkf);
        View v1 = LayoutInflater.from(con).inflate(R.layout.fragment_message_content, null);
        et = (TextView) v1.findViewById(R.id.message_search);
        isViewCreate = true;
        srl.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));
        lv.addHeaderView(v1);
        View view = LayoutInflater.from(con).inflate(R.layout.view_no, null);
        adapter = new MessageListAdapter();
        lv.setEmptyView(view);

        lv.setAdapter(adapter);

        viewListener();//view控件监听事件
        return v;

    }

    /**
     * view监听事件
     */
   private void viewListener(){
       et.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent in = new Intent(con, MessageSearchActivity.class);
               startActivity(in);
           }
       });

       srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               doRefresh();
               getAllUnreadNumber();
           }
       });
       lv.setOnScrollListener(new AbsListView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(AbsListView view, int scrollState) {


           }

           @Override
           public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
               boolean enable = false;
               if (lv != null && lv.getChildCount() > 0) {
                   // 检查listView第一个item是否可见
                   boolean firstItemVisible = lv.getFirstVisiblePosition() == 0;
                   // 检查第一个item的顶部是否可见
                   boolean topOfFirstItemVisible = lv.getChildAt(0).getTop() == 0;
                   // 启用或者禁用SwipeRefreshLayout刷新标识
                   enable = firstItemVisible && topOfFirstItemVisible;
               } else if (lv != null && lv.getChildCount() == 0) {
                   // 没有数据的时候允许刷新
                   enable = true;
               }
               // 把标识传给swipeRefreshLayout
               srl.setEnabled(enable);
           }
       });
       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               if (LhtTool.isLogin) {
                   if (position != 0) {
                       if (list.get(position - 1).getLinkmanid().equals("1")) {
                           Intent in = new Intent(con, MessageSystemListActivity.class);
                           startActivityForResult(in,1);
                       } else {
                           Intent intent = new Intent(getActivity(), MessageChatActivity.class);
                           intent.putExtra("ID", list.get(position - 1).getLinkmanid());
                           intent.putExtra("name", list.get(position - 1).getUsername());
                           intent.putExtra("imageurl", list.get(position - 1).getFaceimg());
                           startActivityForResult(intent, 1);
                       }
                   }
               }
           }
       });

   }
    /**
     * 得到用户id号
     */
    private void getAllUnreadNumber() {
        srl.setRefreshing(true);
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.ALL_UNREAD, map1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s = response.body().string();
                LogUtils.d("============Numberresponse:" + s);
                Message ms = new Message();
                Bundle b = new Bundle();
                b.putString("s", s);
                ms.setData(b);
                ms.what = ALL_UNREADNUM;
                hd.sendMessage(ms);

            }
        });
    }

    /**
     * 刷新
     */
    private void doRefresh() {

        srl.setRefreshing(true);
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.MESSAGE_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("=================response:" + s);
                    messageListGson = new Gson().fromJson(s, MessageListGson.class);
                    hd.sendEmptyMessage(GET_MESSAGE);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        firstRefresh();
        super.onActivityResult(requestCode, resultCode, data);

    }

    class MessageListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            if (null == list) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ID id;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null || convertView.getTag() == null) {
                convertView = LayoutInflater.from(con).inflate(R.layout.listview_message_item, null);
                id = new ID();
                id.iv = (CircleImageView) convertView.findViewById(R.id.avatar);
                id.tv = (TextView) convertView.findViewById(R.id.message_name);
                id.tv1 = (TextView) convertView.findViewById(R.id.message_content);
                id.tv2 = (TextView) convertView.findViewById(R.id.message_time);
                id.tv_yuandian = (TextView) convertView.findViewById(R.id.tv_yuandian1);
                convertView.setTag(id);
            } else {
                id = (ID) convertView.getTag();
            }
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(con).load(list.get(position).getFaceimg()).apply(options).into(id.iv);
            id.tv.setText(list.get(position).getUsername());
            id.tv1.setText(list.get(position).getContent());
            id.tv2.setText(list.get(position).getSendtime());
            if (Integer.valueOf(list.get(position).getUnreadnum()) > 0) {
                id.tv_yuandian.setVisibility(View.VISIBLE);
                id.tv_yuandian.setText(list.get(position).getUnreadnum());
            } else {
                id.tv_yuandian.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ID {
            private CircleImageView iv;
            private TextView tv;
            private TextView tv1;
            private TextView tv2;
            private TextView tv_yuandian;
        }

    }

    /**
     * 第一次刷新
     */
    private void firstRefresh() {

        map.put("pages", 1);
        map.put("num", 100);
        if (MyApplication.userInfo != null && MyApplication.userInfo.getUserID() != null) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map1.put("userid", MyApplication.userInfo.getUserID());
        }
        doRefresh();
        getAllUnreadNumber();

    }

    /**
     * 加载数据
     */
    private void lazyLoad() {
        if (isViewCreate && isUIVisible) {
            if (LhtTool.isLogin) {
                firstRefresh();
                srl.setEnabled(true);
            } else {
                list.clear();
                adapter = new MessageListAdapter();
                adapter.notifyDataSetChanged();
              //  MainActivity.tv_yuandian.setVisibility(View.GONE);
                srl.setEnabled(false);
            }
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreate = false;
            isUIVisible = false;
        }

    }

    /**
     * handler操作主线程
     */
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_MESSAGE) {

                if (null != messageListGson) {
                    if (null != list) {
                        list.clear();
                    }
                    list.addAll(messageListGson.getList());
                }
                adapter.notifyDataSetChanged();
                srl.setRefreshing(false);

            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(con, msg);
            } else if (msg.what == ALL_UNREADNUM) {
                String s = msg.getData().getString("s");
                if (Integer.valueOf(s) > 0) {
                    MainActivity.Companion.getTv_yuandian().setVisibility(View.VISIBLE);
                    MainActivity.Companion.getTv_yuandian().setText(s);
                } else {
                    MainActivity.Companion.getTv_yuandian().setVisibility(View.GONE);
                }
                srl.setRefreshing(false);
            }
        }
    };


    //此方法会使相邻界面不会加载相邻界面，只有本界面获得焦点才会加载数据，减少数据加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void onDestroy() {
        con.unregisterReceiver(myGB);
        super.onDestroy();
    }

    private class MyGB extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d("=================广播进来了");
            firstRefresh();
        }
    }
}
