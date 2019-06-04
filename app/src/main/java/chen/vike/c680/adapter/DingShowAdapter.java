package chen.vike.c680.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import chen.vike.c680.activity.ConfirmPayActivity;
import chen.vike.c680.activity.FuKuanActivity;
import chen.vike.c680.activity.InstallmentDetailsActivity;
import chen.vike.c680.activity.InstallmentSettingActivity;
import chen.vike.c680.activity.OrderDetailsActivity;
import chen.vike.c680.activity.PayDepositActivity;
import chen.vike.c680.activity.PriceMakUpActivity;
import chen.vike.c680.bean.XuQiuList;
import com.lht.vike.a680_v1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lht on 2017/12/21.
 */


  public  class DingShowAdapter extends BaseAdapter {


        private List<XuQiuList.ListBean> list = new ArrayList<>();
        private Context context;
       private String tempType;

    public DingShowAdapter(List<XuQiuList.ListBean> list, Context context, String tempType) {
        this.list = list;
        this.context = context;
        this.tempType = tempType;
    }

    public void addList(List<XuQiuList.ListBean> list) {
            this.list.addAll(list);
        }

        public void refresh() {
            list.clear();
        }


        @Override
        public int getCount() {
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

                convertView = LayoutInflater.from(context).inflate(R.layout.view_fabu_list_item, null);
                id = new ID();
                id.tbs = (TextView) convertView.findViewById(R.id.tbs);
                id.title = (TextView) convertView.findViewById(R.id.title);
                id.money = (TextView) convertView.findViewById(R.id.money);
                id.item_zt = (TextView) convertView.findViewById(R.id.item_zt);
                id.control = (Button) convertView.findViewById(R.id.control);
                id.contro = (Button) convertView.findViewById(R.id.contro);
                convertView.setTag(id);

            } else {
                id = (ID) convertView.getTag();
            }
            id.title.setText(list.get(position).getItemname());

            if (list.get(position).getPayok().equals("0") ) {
                if (list.get(position).getTuoguan_all_btn().equals("1")) {
                    id.control.setVisibility(View.VISIBLE);
                    id.control.setText("托管赏金");
                    id.control.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FuKuanActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            context.startActivity(intent);
                        }
                    });
                }
            } else if (tempType.equals("5")) {
                id.control.setVisibility(View.VISIBLE);
                id.control.setText("确认付款");
                id.control.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ConfirmPayActivity.class);
                        intent.putExtra("ID", list.get(position).getItemid());
                        context.startActivity(intent);
                    }
                });
            } else {
                long endtime = new Date(list.get(position).getEndtime()).getTime()
                        - new Date(new SimpleDateFormat(
                        "yyyy/MM/dd HH:mm:ss").format(new Date())).getTime();
                if (endtime < -((long) 30 * 24 * 60 * 60 * 1000)) {
                    id.control.setVisibility(View.GONE);
                } else {
                    if (list.get(position).getTuoguan_all_btn().equals("1")) {
                        id.control.setVisibility(View.VISIBLE);
                        id.control.setText("增加悬赏");
                        id.control.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, PriceMakUpActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                context.startActivity(intent);
                            }
                        });
                    }
                }
            }
            if (list.get(position).getVikecn_class1ID().equals("6")) {
                id.money.setText(Html.fromHtml("<font color='#20cbcb'>计件</font>：" + list.get(position).getMoney() + "元"));
            } else {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                    if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                        if (list.get(position).getDingjin_btn().equals("1")) {
                            id.control.setVisibility(View.VISIBLE);
                            id.control.setText("托管定金");
                        }
                    } else {
                        id.control.setVisibility(View.GONE);
                    }
                    if (list.get(position).getZab_yusuan1().equals(list.get(position).getZab_yusuan2())) {
                        id.money.setText(Html.fromHtml("<font color='#20cbcb'>招标</font>：" + list.get(position).getZab_yusuan1() + "元"));
                    } else {
                        id.money.setText(Html.fromHtml("<font color='#20cbcb'>招标</font>：" + list.get(position).getZab_yusuan1() + "元-" + list.get(position).getZab_yusuan2() + "元"));
                    }
                } else if (Integer.valueOf(list.get(position).getZab_do()) == 6) {
                    id.money.setText(Html.fromHtml("<font color='#20cbcb'>雇佣</font>：" + list.get(position).getMoney() + "元"));
                } else {
                    id.money.setText(Html.fromHtml("<font color='#20cbcb'>悬赏</font>：" + list.get(position).getMoney() + "元"));
                }
            }

            if (Integer.valueOf(list.get(position).getCheck()) == 0) {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.item_zt.setText("未审核/已付定金￥" + list.get(position).getMoney());
                } else {
                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("未审核/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.item_zt.setText("未审核");
                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("未审核/未付款");
                        } else {
                            id.item_zt.setText("未审核/已付款");
                        }
                    }

                }

            } else if (Integer.valueOf(list.get(position).getCheck()) == 1) {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.item_zt.setText("审核未通过/已付定金￥" + list.get(position).getMoney());
                } else {

                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("审核未通过/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.item_zt.setText("审核未通过");
                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("审核未通过/未付款");
                        } else {
                            id.item_zt.setText("审核未通过/已付款");
                        }
                    }

                }
            } else {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.item_zt.setText("已审核/已付定金￥" + list.get(position).getMoney());
                } else {

                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("已审核/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.item_zt.setText("已审核");

                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("已审核/未付款");
                        } else {
                            id.item_zt.setText("已审核/已付款");

                        }
                    }

                }

            }


            if (list.get(position).getZab_do().equals("1") && !(Integer.valueOf(list.get(position).getJindu()) == 10)) {
                if (Integer.valueOf(list.get(position).getJindu()) < 4 && list.get(position).getPayok().equals("0")) {
                    if (list.get(position).getDingjin_btn().equals("1")) {
                        id.contro.setVisibility(View.GONE);
                        id.control.setVisibility(View.VISIBLE);
                        id.control.setText("托管定金");
                        id.control.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, PayDepositActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                if (Integer.valueOf(list.get(position).getMoney()) > 0) {
                                    intent.putExtra("MONEY", list.get(position).getMoney());
                                }
                                context.startActivity(intent);
                            }
                        });
                    }
                    if (list.get(position).getCheck().equals("1")) {
                        id.control.setVisibility(View.GONE);
                    }
                } else {
                    id.control.setVisibility(View.GONE);
                }

                if (Integer.valueOf(list.get(position).getJindu()) == 33 && list.get(position).getHasfenqi().equals("0")) {
                    if (Integer.valueOf(list.get(position).getZab_yusuan()) < 5000) {
                        if (list.get(position).getTuoguan_all_btn().equals("1")) {
                            id.contro.setVisibility(View.GONE);
                            id.control.setVisibility(View.VISIBLE);
                            id.control.setText("托管赏金");
                            id.control.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, FuKuanActivity.class);
                                    intent.putExtra("ID", list.get(position).getItemid());
                                    intent.putExtra("ZB", "11");
                                    context.startActivity(intent);
                                }
                            });
                        }
                    } else {
                        if (list.get(position).getTuoguan_all_btn().equals("1")) {
                            id.contro.setVisibility(View.VISIBLE);
                            id.control.setVisibility(View.VISIBLE);
                            id.contro.setText("托管赏金");
                            id.control.setText("我要分期");
                            id.contro.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, FuKuanActivity.class);
                                    intent.putExtra("ID", list.get(position).getItemid());
                                    intent.putExtra("ZB", "11");
                                    context.startActivity(intent);
                                }
                            });
                            id.control.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //跳转到我要分期界面
                                    Intent intent = new Intent(context, InstallmentSettingActivity.class);
                                    intent.putExtra("ID", list.get(position).getItemid());
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }

                }

                if (list.get(position).getHasfenqi().equals("1")) {
                    if (list.get(position).getTuoguan_all_btn().equals("1")) {
                        id.contro.setVisibility(View.VISIBLE);
                        id.control.setVisibility(View.VISIBLE);
                        id.contro.setText("托管赏金");
                        id.control.setText("分期详情");
                        id.contro.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, FuKuanActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                intent.putExtra("ZB", "11");
                                context.startActivity(intent);
                            }
                        });
                        id.control.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到分期详情界面
                                Intent intent = new Intent(context, InstallmentDetailsActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                context.startActivity(intent);
                            }
                        });
                    }
                    if (list.get(position).getTuoguan_all_btn().equals("0")) {
                        id.contro.setVisibility(View.GONE);
                    } else {
                        id.contro.setVisibility(View.VISIBLE);
                    }

                }

            }
            if (list.get(position).getCheck() != null) {
                if (Integer.valueOf(list.get(position).getCheck()) > 1) {
                    id.tbs.setText("(" + list.get(position).getCynum() + "稿件)");
                }
            }
            if (list.get(position).getGuyong_userid() != null) {
                if (!list.get(position).getGuyong_userid().equals("0")) {
                    id.tbs.setText("(被雇佣者:" + list.get(position).getGuyong_username() + ")");
                }
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(context, OrderDetailsActivity.class);
                        intent.putExtra("ID", list.get(position).getItemid());
                        context.startActivity(intent);

                }
            });


            return convertView;
        }

    private class ID {
            private TextView title;
            private TextView money;
            private TextView tbs;
            private TextView item_zt;
            private Button control;
            private Button contro;
        }
    }


