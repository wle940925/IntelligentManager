package com.example.intelligentmanager.finance.View.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.finance.Model.bean.BillType;
import com.example.intelligentmanager.finance.Utils.ResourceIdUtils;

import java.util.List;
/**
 * Created by ASUS on 2017/3/14.
 */

public class ExpressionAdapter extends ArrayAdapter<BillType> {
    public ExpressionAdapter(Context context, int textViewResourceId, List<BillType> billTypes) {
        super(context, textViewResourceId, billTypes);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.gridview_item, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivTypeImage = (ImageView) convertView.findViewById(R.id.iv_type_avatar);
        viewHolder.tvTypeName = (TextView) convertView.findViewById(R.id.tv_type_name);
        int resId = ResourceIdUtils.getIdOfResource("type_" + getItem(position).getTypeId() + "_normal", "drawable");
        viewHolder.ivTypeImage.setImageResource(resId);
        viewHolder.tvTypeName.setText(getItem(position).getTypeName());
//        viewHolder.ivTypeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                resetRes();
////                Global.CURRENT_TYPE = getItem(position).getTypeName();
////                int resourceId = ResourceIdUtils.getIdOfResource("type_" + getItem(position).getTypeId() + "_press", "drawable");
////                viewHolder.ivTypeImage.setImageResource(resourceId);
//            }
//        });
        return convertView;
    }

    private void resetRes() {

    }

    public class ViewHolder {
        public ImageView ivTypeImage;
        public TextView tvTypeName;
    }
}
