package com.example.mac.carwash.main.order;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.jsonBean.PayResponseBean;
import com.example.mac.carwash.jsonBean.UnmemberWarshCarRecordsInfo;
import com.example.mac.carwash.jsonBean.UnmemberWarshCarRecordsInfo.Data;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 2018/7/11.
 */

   //非会员  和  会员都是显示同样的item子项布局，非会员隐藏头像，姓名，会员等级
    //非会员订单字段叫 id
public class UnMemberOrderAdapter extends RecyclerView.Adapter<UnMemberOrderAdapter.MyViewHolder> {
    List<Data>mDataList;//存放数据
    Context context;
    public UnMemberOrderAdapter(List<Data> dataList, Context context) {
        mDataList = dataList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //设置textView显示内容为list里的对应项
        Data data = mDataList.get(position);
        holder.orderTime.setText(String.format(context.getResources().getString(R.string.user_order_time), data.getTIME()));
        holder.orderState.setText((data.getISSETTLEMENT()==0)?"未结算":"已结算");
        //holder.orderNum.setText(String.format(context.getResources().getString(R.string.order_num), data.getId()+""));
        holder.orderIndex.setText(String.format(context.getResources().getString(R.string.order_index), data.getRownum_()+""));
        holder.orderFee.setText(String.format(context.getResources().getString(R.string.user_wash_fee), data.getFee()));
       holder.userName.setText(String.format(context.getResources().getString(R.string.user_car_num), data.getCarmark()));
       holder.userLevel.setText("非会员用户");
        holder.carNum.setText("");
        holder.userAvator.setImageResource(R.mipmap.unmember_avator);
        //子项的点击事件监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "点击子项" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mDataList==null) return 0;
        return mDataList.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderTime,orderState,orderIndex,orderFee,userName,userLevel,carNum;
        ImageView userAvator;
        public MyViewHolder(View itemView) {
            super(itemView);
            orderTime = (TextView) itemView.findViewById(R.id.user_order_time);
            orderState = (TextView)itemView.findViewById(R.id.user_order_state);
            orderIndex = (TextView)itemView.findViewById(R.id.user_order_index);
            orderFee = (TextView)itemView.findViewById(R.id.user_order_fee);
            userName = (TextView)itemView.findViewById(R.id.user_name);
            userLevel = (TextView)itemView.findViewById(R.id.user_member_level);
            userLevel.setTextColor(Color.parseColor("#68228B"));
            carNum = (TextView)itemView.findViewById(R.id.user_car_num);
            userAvator = (ImageView)itemView.findViewById(R.id.user_avator);
        }

    }

    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
    public boolean addItem(int position, UnmemberWarshCarRecordsInfo.Data data) {
        if (position < mDataList.size() && position >= 0) {
            mDataList.add(position, data);
            notifyItemInserted(position);
            return true;
        }
        return false;
    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < mDataList.size() && position >= 0) {
            mDataList.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    //清空显示数据
    public void clearAll() {
        mDataList.clear();
        notifyDataSetChanged();
    }







    /*-----------------------------------------结算请求--------------------------------------------------*/
    private void openPayForMemberRequest(int OrderId,int PayWay) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sqlKey", "CP_SETTLEMENT_XICHE_ORDER");
        resMap.put("settlementWay", PayWay);
        resMap.put("bcid",OrderId);
        resMap.put("sqlType", "proc");
        WebServiceHelp mServiceHelp = new WebServiceHelp(context,"iPadService.asmx", "loadData", PubData.class,true,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                Gson gson = new Gson();
                PayResponseBean payResponseBean = gson.fromJson(json, PayResponseBean.class);
                String code = payResponseBean.getCode();
                if (code.equals("00")) {
                    Toast.makeText(context, "结算成功！", Toast.LENGTH_SHORT).show();
                } else if(code.equals("99")){
                    Toast.makeText(context, "重复结算！", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "结算失败，未知错误！ code"+code, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        mServiceHelp.start(resMap,context);
    }

}