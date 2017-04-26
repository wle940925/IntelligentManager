package com.example.intelligentmanager.finance.Model.config;

import android.graphics.Color;

/**
 * Created by 聂敏萍 on 2017/3/13.
 * 保存一些全局的参数
 */

public class Global {
    /**
     * 记账类型的数组
     */
    public static final String[] types = new String[]{
            "餐饮", "零食", "交通", "购物", "学习", "娱乐","转账支出",
            "生活费", "奖金", "兼职", "实习", "零花钱", "薪资", "转账收入", "其他"
    };
    public static final int[] colors = new int[]{
            Color.rgb(234, 103, 68),Color.rgb(247, 186, 91),
            Color.rgb(118, 136, 242),Color.rgb(255, 183, 0),
            Color.rgb(63, 171, 233), Color.rgb(255, 147, 54),
            Color.rgb(146, 141, 255),
            Color.rgb(255, 136, 34),Color.rgb(86, 178, 255),
            Color.rgb(255, 147, 54),Color.rgb(72, 217, 207),
            Color.rgb(255, 105, 105), Color.rgb(255, 136, 34),
            Color.rgb(146, 141, 255), Color.rgb(88, 200, 77)
    };

    /**
     * 要缓存的设置信息
     */
    public static final String SHARE_SETTING_UPLOAD = "setting_upload";//设置是否上传
    /**
     * 要缓存的个人财务信息
     */
    public static final String SHARE_PERSONAL_TOTAL_IN = "personal_total_in";//个人历史总收入
    public static final String SHARE_PERSONAL_TOTAL_OUT = "personal_total_out";//个人历史总支出
    public static final String SHARE_PERSINAL_TOKEN = "personal_token";//token

    public static String CURRENT_TYPE="餐饮";
    public static String MONTH_TOTALIN="0.00";
    public static String MONTH_TOTALOUT="0.00";
}
