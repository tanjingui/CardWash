package com.example.mac.carwash.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mac.carwash.R;
import java.util.List;

/**
 * Created by mac on 2018/7/11.
 */


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    List<OrderBean.Data>dataList;//存放数据
    Context context;

    public OrderAdapter(OrderBean orderBean, Context context) {
        dataList = orderBean.getData();
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
        OrderBean.Data data = dataList.get(position);
        holder.orderTime.setText(String.format(context.getResources().getString(R.string.user_order_time), data.getTIME()));
        holder.orderState.setText(data.getISSETTLEMENT());
        holder.orderNum.setText(String.format(context.getResources().getString(R.string.order_num), data.getId()));
        holder.orderFee.setText(String.format(context.getResources().getString(R.string.user_wash_fee), data.getFee()));
        holder.userName.setText(String.format(context.getResources().getString(R.string.user_name), data.getNAME()));
        holder.userLevel.setText(data.getVip());
        holder.carNum.setText(String.format(context.getResources().getString(R.string.user_car_num), data.getCarmark()));
        Glide.with(context)
                .load(data.getHead())
                .error(R.mipmap.default_avator)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .centerCrop()
                .into(holder.userAvator);
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
        return dataList.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderTime,orderState,orderNum,orderFee,userName,userLevel,carNum;
        ImageView userAvator;
        public MyViewHolder(View itemView) {
            super(itemView);
            orderTime = (TextView) itemView.findViewById(R.id.user_order_time);
            orderState = (TextView)itemView.findViewById(R.id.user_order_state);
            orderNum = (TextView)itemView.findViewById(R.id.user_order_num);
            orderFee = (TextView)itemView.findViewById(R.id.user_order_fee);
            userName = (TextView)itemView.findViewById(R.id.user_name);
            userLevel = (TextView)itemView.findViewById(R.id.user_member_level);
            carNum = (TextView)itemView.findViewById(R.id.user_car_num);
            userAvator = (ImageView)itemView.findViewById(R.id.user_avator);
        }

    }

    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
    public boolean addItem(int position, OrderBean.Data data) {
        if (position < dataList.size() && position >= 0) {
            dataList.add(position, data);
            notifyItemInserted(position);
            return true;
        }
        return false;
    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < dataList.size() && position >= 0) {
            dataList.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    //清空显示数据
    public void clearAll() {
        dataList.clear();
        notifyDataSetChanged();
    }
}