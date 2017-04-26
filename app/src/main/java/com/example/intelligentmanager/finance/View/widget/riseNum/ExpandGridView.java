package com.example.intelligentmanager.finance.View.widget.riseNum;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by 聂敏萍 on 2017/3/14.
 * 自定义的gridview
 */

public class ExpandGridView extends GridView {
    public ExpandGridView(Context context) {
        super(context);
    }

    public ExpandGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
