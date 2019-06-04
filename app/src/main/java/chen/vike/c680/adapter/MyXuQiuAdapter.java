/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: MyXuQiuAdapter
 * Author: chen
 * Date: 2019/1/22 11:19
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package chen.vike.c680.adapter;

import android.app.Activity;
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
import chen.vike.c680.activity.TenderBidActivity;
import chen.vike.c680.bean.XuQiuList;
import com.lht.vike.a680_v1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.Interface.ViewItemClick;

/**
 * @ClassName: MyXuQiuAdapter
 * @Description: java类作用描述
 * @Author: chen
 * @Date: 2019/1/22 11:19
 */
public class MyXuQiuAdapter  extends BaseAdapter{


        private List<XuQiuList.ListBean> list = new ArrayList<>();
        private Context context;
       private Map<String, Object> map = new HashMap<>();
       private ViewItemClick viewItemClick;
        public MyXuQiuAdapter(List<XuQiuList.ListBean> list,Context context, Map<String, Object> map) {
            this.list = list;
            this.context = context;
            this.map = map;
        }

        public void addList(List<XuQiuList.ListBean> list) {
            this.list.addAll(list);
        }

        public void refresh() {
            list.clear();
        }
    public void remove(int position){
        list.remove(position);
    }
    public ViewItemClick getViewItemClick() {
        return viewItemClick;
    }

    public void setViewItemClick(ViewItemClick viewItemClick) {
        this.viewItemClick = viewItemClick;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.my_xuqiu_list_item, null);
                id = new ID();
                id.bianhao = convertView.findViewById(R.id.my_xuqiu_item_bianhao);
                id.title = convertView.findViewById(R.id.my_xuqiu_item_title);
                id.money = convertView.findViewById(R.id.my_xuqiu_item_price);
                id.content = convertView.findViewById(R.id.my_xuqiu_item_gaisu);
                id.shangchu = convertView.findViewById(R.id.my_xuqiu_item_shanchu);
                id.givemoney = convertView.findViewById(R.id.my_xuqiu_item_give_money);
                id.zhuangtai = convertView.findViewById(R.id.my_xuqiu_item_zhuangtai);
                convertView.setTag(id);

            } else {
                id = (ID) convertView.getTag();
            }

            id.title.setText(list.get(position).getItemname());
            id.content.setText("需求概述："+list.get(position).getCon());
            if (!list.get(position).getIs_can_delete().equals("1")){
                id.shangchu.setVisibility(View.GONE);
            }
            if (list.get(position).getPayok().equals("0")) {
                id.givemoney.setText("托管赏金");
                id.givemoney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FuKuanActivity.class);
                        intent.putExtra("ID", list.get(position).getItemid());
                        context.startActivity(intent);
                    }
                });
            } else if (map.get("type").toString().equals("5")) {
                id.givemoney.setText("确认付款");
                id.givemoney.setOnClickListener(new View.OnClickListener() {
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
                    id.givemoney.setVisibility(View.GONE);
                } else {
                    id.givemoney.setVisibility(View.VISIBLE);
                    id.givemoney.setText("增加悬赏");
                    id.givemoney.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, PriceMakUpActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            context.startActivity(intent);
                        }
                    });
                }
            }

            if (list.get(position).getVikecn_class1ID().equals("6")) {
                id.money.setText(Html.fromHtml("<font color='#20cbcb'></font>：" + list.get(position).getMoney() + "元"));
            } else {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                    if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                        id.givemoney.setVisibility(View.VISIBLE);
                        id.givemoney.setText("托管定金");
                    } else {
                        id.givemoney.setVisibility(View.GONE);
                    }
                    if (list.get(position).getZab_yusuan1().equals(list.get(position).getZab_yusuan2())) {
                        id.money.setText(Html.fromHtml("<font color='#20cbcb'></font>￥" + list.get(position).getZab_yusuan1() + "元"));
                    } else {
                        id.money.setText(Html.fromHtml("<font color='#20cbcb'></font>￥" + list.get(position).getZab_yusuan1() + "元-" + list.get(position).getZab_yusuan2() + "元"));
                    }
                } else if (Integer.valueOf(list.get(position).getZab_do()) == 6) {
                    id.money.setText(Html.fromHtml("<font color='#20cbcb'></font>￥" + list.get(position).getMoney() + "元"));
                } else {
                    id.money.setText(Html.fromHtml("<font color='#20cbcb'></font>￥" + list.get(position).getMoney() + "元"));
                }
            }

            if (Integer.valueOf(list.get(position).getCheck()) == 0) {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.zhuangtai.setText("未审核/已付定金￥" + list.get(position).getMoney());
                } else {
                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.zhuangtai.setText("未审核/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.zhuangtai.setText("未审核");
                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.zhuangtai.setText("未审核/未付款");
                        } else {
                            id.zhuangtai.setText("未审核/已付款");
                        }
                    }

                }

            } else if (Integer.valueOf(list.get(position).getCheck()) == 1) {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.zhuangtai.setText("审核未通过/已付定金￥" + list.get(position).getMoney());
                } else {

                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.zhuangtai.setText("审核未通过/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.zhuangtai.setText("审核未通过");
                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.zhuangtai.setText("审核未通过/未付款");
                        } else {
                            id.zhuangtai.setText("审核未通过/已付款");
                        }
                    }

                }
            } else {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.zhuangtai.setText("已审核/已付定金￥" + list.get(position).getMoney());
                } else {

                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.zhuangtai.setText("已审核/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.zhuangtai.setText("已审核");

                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.zhuangtai.setText("已审核/未付款");
                        } else {
                            id.zhuangtai.setText("已审核/已付款");

                        }
                    }

                }

            }

            if (map.get("type").toString().equals("2") && !(Integer.valueOf(list.get(position).getJindu()) == 10)) {
                if (Integer.valueOf(list.get(position).getJindu()) < 4 && list.get(position).getPayok().equals("0")) {
                    id.givemoney.setVisibility(View.VISIBLE);
                    id.givemoney.setText("托管定金");
                    id.givemoney.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, PayDepositActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            if (Integer.valueOf(list.get(position).getMoney()) > 0) {
                                intent.putExtra("MONEY", list.get(position).getMoney());
                            }
                            ((Activity)context).startActivityForResult(intent,1);
                        }
                    });
                    if (list.get(position).getCheck().equals("1")) {

                    }
                } else {

                }

                if (Integer.valueOf(list.get(position).getJindu()) == 33 && list.get(position).getHasfenqi().equals("0")) {
                    if (Integer.valueOf(list.get(position).getZab_yusuan()) < 5000) {

                        id.givemoney.setVisibility(View.VISIBLE);
                        id.givemoney.setText("托管赏金");
                        id.givemoney.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, FuKuanActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                intent.putExtra("ZB", "11");
                                ((Activity)context).startActivityForResult(intent,1);
                            }
                        });
                    } else {

                        id.givemoney.setVisibility(View.VISIBLE);
                        id.givemoney.setText("我要分期");
//                        id.contro.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(context, FuKuanActivity.class);
//                                intent.putExtra("ID", list.get(position).getItemid());
//                                intent.putExtra("ZB", "11");
//                                ((Activity)context).startActivityForResult(intent,1);
//                            }
//                        });
                        id.givemoney.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到我要分期界面
                                Intent intent = new Intent(context, InstallmentSettingActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                ((Activity)context).startActivityForResult(intent,1);
                            }
                        });
                    }

                }

                if (list.get(position).getHasfenqi().equals("1")) {
                    id.givemoney.setVisibility(View.VISIBLE);
                    id.givemoney.setText("分期详情");
//                    id.contro.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, FuKuanActivity.class);
//                            intent.putExtra("ID", list.get(position).getItemid());
//                            intent.putExtra("ZB", "11");
//                            ((Activity)context).startActivityForResult(intent,1);
//                        }
//                    });
                    id.givemoney.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //跳转到分期详情界面
                            Intent intent = new Intent(context, InstallmentDetailsActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            context.startActivity(intent);
                        }
                    });
                    if (list.get(position).getTuoguan_all_btn().equals("0")) {
                    } else {
                    }

                }

            }

          //  id.tbs.setText("(" + list.get(position).getCynum() + "稿件)");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (map.get("type").toString().equals("2")) {
                        if (Integer.valueOf(list.get(position).getJindu()) > 3 && Integer.valueOf(list.get(position).getJindu()) != 10) {
                            Intent intent = new Intent(context, TenderBidActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, OrderDetailsActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            context.startActivity(intent);
                        }

                    } else {
                        Intent intent = new Intent(context, OrderDetailsActivity.class);
                        intent.putExtra("ID", list.get(position).getItemid());
                        context.startActivity(intent);
                    }


                }
            });
            id.shangchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewItemClick().shortClick(position);
                }
            });
            return convertView;
        }


        private class ID {
            private TextView title,bianhao,zhuangtai,content;
            private TextView money;
            private Button shangchu,givemoney;
        }


}