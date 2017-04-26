package com.example.intelligentmanager.finance.View.fragment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.databinding.FragmentBillInfoBinding;
import com.example.intelligentmanager.finance.Model.bean.Bill;
import com.example.intelligentmanager.finance.Model.db.BillTableDao;
import com.example.intelligentmanager.finance.View.adapter.TimeLineAdapter;
import com.example.intelligentmanager.finance.ViewModel.BillInfoViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;

/**
 * Created by 聂敏萍 on 2015/11/29.
 */
public class BillInfoFragment extends BaseFragment<BillInfoViewModel, FragmentBillInfoBinding> implements TimeLineAdapter.OnItemClickLitener  {
    private static final String ARGUMENT="argument" ;
    private String mArgument="0";
    private TimeLineAdapter timeLineAdapter;
    private BillTableDao billTableDao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billTableDao = new BillTableDao(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null)
        mArgument=ARGUMENT.trim();
        mArgument = bundle.getString(ARGUMENT).trim();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setViewModel(new BillInfoViewModel());
        setBinding(DataBindingUtil.<FragmentBillInfoBinding>inflate(getActivity().getLayoutInflater(), R.layout.fragment_bill_info, container, false));
        getBinding().setBillInfo(getViewModel());
        getViewModel().onInit(mArgument);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showChart(getBinding().pie, getViewModel().getPieDate());
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        timeLineAdapter = new TimeLineAdapter(getActivity(), getViewModel().getBills(), this);
        getBinding().recyclerview.setAdapter(timeLineAdapter);
    }

    /**
     * 传入需要的参数，设置给arguments
     *
     * @param argument
     * @return
     */
    public static BillInfoFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        BillInfoFragment contentFragment = new BillInfoFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    //recyclerView的单击事件
    @Override
    public void onItemClick(View view, int position) {
             dialog(position);
    }
    protected void dialog( final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定隐藏吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bill bill = getViewModel().getBills().get(position);
                billTableDao.rmBill(bill);
                
                dialog.dismiss();
                getActivity().finish();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }); builder.create().show();
    }

    //recyclerView的长按事件
    @Override
    public void onItemLongClick(View view, int position) {

    }

    //显示饼状图
    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription("");

        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setUsePercentValues(true);  //显示成百分比

        pieChart.setCenterText("");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);  //最右边显示
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
    }
}
