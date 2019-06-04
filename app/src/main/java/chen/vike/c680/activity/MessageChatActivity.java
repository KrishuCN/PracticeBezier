package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import chen.vike.c680.bean.ChatMessageGson;
import chen.vike.c680.bean.MsgBean;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/25.
 * <p>
 * 聊天界面
 */

public class MessageChatActivity extends BaseStatusBarActivity {


    @BindView(R.id.action_back)
    ImageView actionBack;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.bar_img)
    ImageView barImg;
    private SwipeRefreshLayout srl;
    private ListView lv;
    private EditText et;
    private Button bt;
    private ChatMessageGson chatMessageGson;
    private MessageChatAdapter adapter;
    private String ID;
    private String mContent;
    private boolean onceIn;
    private List<MsgBean> list = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> map1 = new HashMap<>();
    private final int MESSAGE_GET = 0x123;
    private final int NETWORK_EXCEPTION = 0x121;
    private final int SEND_MESSAGE = 0x122;
    private final int SHUAXIN_MESSAGE = 0X111;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);

        getTitle().setText(getIntent().getStringExtra("name"));
        getBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        srl =  findViewById(R.id.chat_srl);
        lv =  findViewById(R.id.chat_lv);
        et =  findViewById(R.id.chat_et);
        bt =  findViewById(R.id.chat_bt);

        srl.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));


        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatMessage();
            }
        });
        ID = getIntent().getStringExtra("ID");
        map.put("fromuserid", ID);

        map.put("num", 10);
        map.put("pages", 1);
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map1.put("userid", MyApplication.userInfo.getUserID());
            map1.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map1.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            getChatMessage();
        }


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().toString().equals("")) {
                    CustomToast.showToast(MessageChatActivity.this, "消息不能为空!", Toast.LENGTH_SHORT);
                } else {

                    sendMessage();
                    mContent = et.getText().toString();
                    LogUtils.d("==============mContent:" + mContent);
                    et.setText("");
                    bt.setClickable(false);
                    bt.setText("发送中");

                }
            }
        });

        adapter = new MessageChatAdapter(MessageChatActivity.this, new ArrayList());

        lv.setAdapter(adapter);
        //保证listview最后一条消息处于edittext上面
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(lv.getWindowToken(), 0);
            }
        });


    }

    private void getChatMessage() {
        srl.setRefreshing(true);
        if (null != chatMessageGson) {
            map.put("pages", chatMessageGson.getPagerInfo().getNextPageIndex());
        }
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.CHAT_MESSAGE, map, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("===============response:" + s);
                    chatMessageGson = new Gson().fromJson(s, ChatMessageGson.class);
                    hd.sendEmptyMessage(MESSAGE_GET);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void sendMessage() {

        EMMessage message = EMMessage.createTxtSendMessage(et.getText().toString(), ID);
        message.setChatType(EMMessage.ChatType.Chat);
        message.setAttribute("ischat", "1");
        message.setAttribute("from_uid", MyApplication.userInfo.getUserID());
        message.setAttribute("tui_con_class", MyApplication.userInfo.getNickame());
        message.setAttribute("gourl", MyApplication.userInfo.getIcon());
        EMClient.getInstance().chatManager().sendMessage(message);
        map1.put("type", "byID");
        map1.put("linkmanid", ID);
        map1.put("crv_content", et.getText().toString());
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.SEND_MESSAGE, map1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s = response.body().string();
                LogUtils.d("=====================response:" + s);
                Message ms = new Message();
                ms.what = SEND_MESSAGE;
                Bundle b = new Bundle();
                b.putString("s", s);
                ms.setData(b);
                hd.sendMessage(ms);

            }
        });


    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            //收到消息

            String content = null;
            for (int i = 0; i < messages.size(); i++) {
                String[] s = messages.get(i).getBody().toString().split(":");
                content = s[1].substring(1, s[1].length() - 1);
                if (messages.get(i).getFrom().equals(ID)) {
                    MsgBean msg_new = new MsgBean();
                    msg_new.setType(MsgBean.TYPE_RECIVE);
                    msg_new.setContent(content);
                    msg_new.setImageUrl(messages.get(i).getStringAttribute("gourl", null));
                    list.add(msg_new);
                    adapter.addsendList(list);
                    hd.sendEmptyMessage( SHUAXIN_MESSAGE);
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            LogUtils.d("===================messages:" + messages);
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            LogUtils.d("===================messages:" + messages);
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            LogUtils.d("===================messages:" + message);
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            LogUtils.d("===================messages:" + change);
        }
    };


    @Override
    protected void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        LhtTool.isChatActivity = false;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        LhtTool.isChatActivity = true;
        super.onResume();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_GET) {
                if (null != chatMessageGson) {

                    if (chatMessageGson.getPagerInfo().getCurrPageIndex() != 1 && chatMessageGson.getPagerInfo().getNextPageIndex() == 0 || chatMessageGson.getPagerInfo().getNextPageIndex() == 0 && onceIn) {
                        CustomToast.showToast(MessageChatActivity.this, "没有更多了", Toast.LENGTH_SHORT);
                    } else {
                        list.clear();
                        for (int i = 0; i < chatMessageGson.getList().size(); i++) {
                            MsgBean msg1 = new MsgBean();
                            if (chatMessageGson.getList().get(i).getLinkmanid().equals(MyApplication.userInfo.getUserID())) {
                                msg1.setType(MsgBean.TYPE_SEND);
                            } else {
                                msg1.setType(MsgBean.TYPE_RECIVE);
                            }
                            msg1.setContent(chatMessageGson.getList().get(i).getContent());
                            msg1.setImageUrl(chatMessageGson.getList().get(i).getFaceimg());
                            list.add(0, msg1);
                        }
                        adapter.addList(list);
//                        lv.setSelection(9);
                    }
                }
                adapter.notifyDataSetChanged();
                lv.setSelection(adapter.getCount() - 1);
                srl.setRefreshing(false);
                onceIn = true;

            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(MessageChatActivity.this, msg);
            } else if (msg.what == SEND_MESSAGE) {

                String[] s = msg.getData().getString("s").split(",");
                switch (s[0]) {
                    case "unlogin":
                        CustomToast.showToast(MessageChatActivity.this, "请登录~", Toast.LENGTH_LONG);
                        break;
                    case "null_con":
                        CustomToast.showToast(MessageChatActivity.this, "内容不能为空~", Toast.LENGTH_LONG);
                        bt.setClickable(true);
                        bt.setText("发送");
                        break;
                    case "notuser":
                        CustomToast.showToast(MessageChatActivity.this, "接收人用户无效~", Toast.LENGTH_LONG);
                        break;
                    case "fail":
                        CustomToast.showToast(MessageChatActivity.this, "发送失败，请稍后再试~", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        bt.setClickable(true);
                        bt.setText("发送");
                        MsgBean msg1 = new MsgBean();
                        msg1.setType(MsgBean.TYPE_SEND);
                        msg1.setContent(mContent);
                        msg1.setImageUrl(MyApplication.userInfo.getIcon());
                        list.add(msg1);
                        adapter.refreshList();
                        adapter.addsendList(list);
                        adapter.notifyDataSetChanged();
                        break;
                    case "self":
                        CustomToast.showToast(MessageChatActivity.this, "不能对自己发送消息~", Toast.LENGTH_LONG);
                        break;
                    case "noun":
                        CustomToast.showToast(MessageChatActivity.this, "用户名不能为空~", Toast.LENGTH_LONG);
                        break;
                }

            } else if (msg.what == SHUAXIN_MESSAGE) {
                adapter.notifyDataSetChanged();
            }
        }
    };


}
