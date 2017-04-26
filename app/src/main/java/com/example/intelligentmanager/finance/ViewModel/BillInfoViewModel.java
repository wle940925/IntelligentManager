package com.example.intelligentmanager.finance.ViewModel;

import android.app.AlertDialog;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.DisplayMetrics;

import com.example.intelligentmanager.finance.Model.bean.Bill;
import com.example.intelligentmanager.finance.Model.config.Global;
import com.example.intelligentmanager.finance.Model.db.BillTableDao;
import com.example.intelligentmanager.finance.Utils.TimeUtils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.example.intelligentmanager.finance.Model.config.Global.MONTH_TOTALIN;
import static com.example.intelligentmanager.finance.Model.config.Global.MONTH_TOTALOUT;

/**
 * Created by 聂敏萍 on 2017/3/13.
 */

public class BillInfoViewModel extends ViewModel {
    private AlertDialog alertDialog;
    private float toin, toout;
    public final ObservableField<String> totalIn = new ObservableField<>();//总收入
    public final ObservableField<String> totalOut = new ObservableField<>();//总支出
    private List<Bill> bills;
    private HashMap<String, Float> map = new HashMap<>();
    private PieData pieData;

    public void onInit(String mArgument) {

        BillTableDao billTableDao = new BillTableDao(appContext);
        if (mArgument.equals("0"))
            bills = billTableDao.getBillList(TimeUtils.getFirstDayTimeOfWeek());//周账单
        else if (mArgument.equals("1")) {
            bills = billTableDao.getBillList(TimeUtils.getFirstDayTimeOfMonth());//月账单
        } else
        {
            bills = billTableDao.getBillList(TimeUtils.getFirstDayTimeOfYear());//年账单
        }
        for (Bill bill : bills) {
            if (bill.getTypeId().equals("7")||bill.getTypeId().equals("8")||bill.getTypeId().equals("9")||bill.getTypeId().equals("10")||
                    bill.getTypeId().equals("11")||bill.getTypeId().equals("12")||bill.getTypeId().equals("13")) {
                toin += Float.parseFloat(bill.getMoney());
                totalIn.set(String.valueOf(toin));
                if(mArgument.equals("1")) {
                    MONTH_TOTALIN = String.valueOf(toin);
                }
            } else {
                toout += Float.parseFloat(bill.getMoney().trim());
                totalOut.set(String.valueOf(toout));
                if(mArgument.equals("1")) {
                    MONTH_TOTALOUT = String.valueOf(toout);
                }
            }
            if (map.containsKey(bill.getTypeId()))
                map.put(bill.getTypeId(), map.get(bill.getTypeId()) + Float.parseFloat(bill.getMoney()));
            else
                map.put(bill.getTypeId(), Float.parseFloat(bill.getMoney()));
        }

//        if( bills == billTableDao.getBillList(TimeUtils.getFirstDayTimeOfMonth())){
//            MONTH_TOTALIN = String.valueOf(toin);
//            MONTH_TOTALOUT = String.valueOf(toout);
//        }
        pieData = getPieData(map.size(), 100);
    }

    ObservableList<Bill> observableList = new ObservableArrayList<>();

    /**
     * 获取相应的账单list
     *
     * @return
     */
    public ObservableList<Bill> getBills() {
        observableList.addAll(bills);
        return observableList;
    }

    public PieData getPieDate() {
        return pieData;
    }

    /**
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
        ArrayList<Integer> colors = new ArrayList<Integer>();

        Set set = map.keySet();
        Iterator iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String in = String.valueOf(iterator.next());
            int index = Integer.valueOf(in);
            xValues.add(Global.types[index]);
            yValues.add(new Entry(map.get(index + "") / (toin + toout), i));
            colors.add(Global.colors[index]);
            i++;
        }
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "");/*显示在比例图上*/
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = appContext.getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px);

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }
}

