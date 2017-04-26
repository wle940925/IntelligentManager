package com.example.intelligentmanager.finance.View.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.finance.Model.bean.SecretBill;
import com.example.intelligentmanager.finance.View.widget.riseNum.RoundImageView;

/**
 * Created by 易水柔 on 2017/3/27.
 */
public class SecretBillAdapter extends
        RecyclerView.Adapter<SecretBillAdapter.ViewHolder>
{

    private LayoutInflater mInflater;
    private SecretBill bill[];

    public SecretBillAdapter(Context context,SecretBill bill[])
    {
        mInflater = LayoutInflater.from(context);
        this.bill=bill;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }
        RoundImageView imageView;
        TextView date;
        TextView type;
        TextView remark;
        TextView money;


    }

    @Override
    public int getItemCount()
    {
        return bill.length;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.secret_bill_items,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.imageView = (RoundImageView) view
                .findViewById(R.id.sec_iv_icon);
        viewHolder.date=(TextView)view.findViewById(R.id.sec_tv_date);
        viewHolder.type=(TextView)view.findViewById(R.id.sec_tv_type_name);
        viewHolder.remark=(TextView)view.findViewById(R.id.sec_tv_type_remark);
        viewHolder.money=(TextView)view.findViewById(R.id.sec_tv_money);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        Log.i("bill",bill[i].getMoney());
        viewHolder.imageView.setImageDrawable(bill[i].getTypedrawable());
        viewHolder.date.setText(bill[i].getTime());
        viewHolder.type.setText(bill[i].getTypeName());
        if(bill[i].getRemark()!=null){
            viewHolder.remark.setText(bill[i].getRemark());
        }else {
            viewHolder.remark.setText("");
        }
        viewHolder.money.setText(bill[i].getMoney());
    }

}