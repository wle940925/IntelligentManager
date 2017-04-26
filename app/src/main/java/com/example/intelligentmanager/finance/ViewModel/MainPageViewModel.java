package com.example.intelligentmanager.finance.ViewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.finance.Model.bean.BillType;
import com.example.intelligentmanager.finance.Model.config.Global;
import com.example.intelligentmanager.finance.Model.db.TimeDao;
import com.example.intelligentmanager.finance.MyApplication;
import com.example.intelligentmanager.finance.Utils.ResourceIdUtils;
import com.example.intelligentmanager.finance.View.activity.RecordActivity;
import com.example.intelligentmanager.finance.View.adapter.ExpressionAdapter;
import com.example.intelligentmanager.finance.View.widget.riseNum.ExpandGridView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by 聂敏萍 on 2017/3/14.
 */

public class MainPageViewModel  extends ViewModel{
    private static final String TAG = "**MainPageViewModel**";

    private RecordActivity recordActivity;

    public final ObservableField<String> totalMoney = new ObservableField<>();
    public final ObservableBoolean isOneDialChoosed = new ObservableBoolean();
    public final ObservableBoolean isTwoDialChoosed = new ObservableBoolean();
    public final ObservableField<BillType> defaultChooseType = new ObservableField<>();//默认选择的记账类型

    private static int pro_postion = -1;

    public MainPageViewModel(RecordActivity recordActivity) {
        this.recordActivity = recordActivity;
        totalMoney.set("0.0");
        getBillTypeListFromDB();
    }

//    @Command
//    public void onNumKeyClicked(View view) {
//        String tag = view.getTag().toString();
//        if ((tag.equals("0") || tag.equals(".") || tag.equals("+")) && stringBuffer.length() == 0)
//            return;
//        if (stringBuffer.length() != 0 && tag.equals("+")) {
//            if (stringBuffer.charAt(stringBuffer.length() - 1) == '+') {
//                return;
//            } else {
//                stringBuffer = new StringBuffer();
//            }
//        }
//        if (stringBuffer.length() != 0 && tag.equals(".")) {
//            if (stringBuffer.charAt(stringBuffer.length() - 1) == '.') {
//                return;
//            }
//        }
//        stringBuffer.append(tag);
//        totalBuffer.append(tag);
//        total.set(totalBuffer.toString());
//        totalMoney.set(calculate(totalBuffer));
//    }
//
//    @Command
//    public void onDeleteKeyClicked(View view) {
//        if (totalBuffer.length() == 0)
//            return;
//        if (stringBuffer.length() != 0) {
//            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
//        }
//        totalBuffer.deleteCharAt(totalBuffer.length() - 1);
//        totalMoney.set(totalBuffer.length() == 0 ? "0.0" : calculate(totalBuffer));
//        total.set(totalBuffer.length() == 0 ? "0.0" : totalBuffer.toString());
//    }
//
//    @Command
//    public void onClearKeyClicked(View view) {
//        totalBuffer = new StringBuffer();
//        stringBuffer = new StringBuffer();
//        total.set("0.0");
//        totalMoney.set("0.0");
//    }
//
//    @Command
//    public void onPlusKeyClicked(View view) {
//        String tag = view.getTag().toString();
//        if (tag.equals("+") && totalBuffer.length() == 0)
//            return;
//        totalBuffer.append(tag);
//    }

//    @Command
//    public void onSubmitKeyClicked(View view) {
//        if (defaultChooseType.get() != null && total.get().equals("0.0")) {
//            ToastUtils.showToast("请输入价格");
//            return;
//        }
//        BillTableDao billTableDao = new BillTableDao(recordActivity);
//        billTableDao.saveBill(new Bill(defaultChooseType.get().getTypeId() + "", totalMoney.get(), "", String.valueOf(System.currentTimeMillis()), 0));
//        ToastUtils.showToast("记账成功");
//    }

    @Command
    public View getGridChildView(final int i) {
        View view = View.inflate(recordActivity, R.layout.viewpager_item, null);
        final ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);

        final List<BillType> list = new ArrayList<BillType>();
        if (i == 1) {
            list.addAll(billTypes.subList(0, 8));
        } else if (i == 2) {
            list.addAll(billTypes.subList(8, 15));
        }
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(recordActivity,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //defaultChooseType.set(list.get(position));
                //如果点击原来的位置
                if(pro_postion ==position){
                    ImageView imageView_pro = (ImageView)gv.getChildAt(pro_postion).findViewById(R.id.iv_type_avatar);
                    int resourceId_normal = ResourceIdUtils.getIdOfResource("type_" + list.get(pro_postion).getTypeId() + "_normal", "drawable");
                    imageView_pro.setImageResource(resourceId_normal);
                }
                //如果点击其他位置
                else {
                    if(pro_postion!=-1) {
                        ImageView imageView_pro = (ImageView) gv.getChildAt(pro_postion).findViewById(R.id.iv_type_avatar);
                        int resourceId_normal = ResourceIdUtils.getIdOfResource("type_" + list.get(pro_postion).getTypeId() + "_normal", "drawable");
                        imageView_pro.setImageResource(resourceId_normal);
                    }

                    pro_postion = position;

                    //Global.CURRENT_TYPE = list.get(position).getTypeName();
                    Global.CURRENT_TYPE = String.valueOf(list.get(position).getTypeId());
                    int resourceId_press = ResourceIdUtils.getIdOfResource("type_" + list.get(position).getTypeId() + "_press", "drawable");
                    ImageView imageView_cur = (ImageView) view.findViewById(R.id.iv_type_avatar);
                    imageView_cur.setImageResource(resourceId_press);
                }
            }
        });

        return view;
    }


//    private String calculate(StringBuffer stringBuffer) {
//        if (!stringBuffer.toString().contains("+"))
//            return stringBuffer.toString();
//        String num = new String(stringBuffer);
//        String[] nums = num.split("\\+");
//        float total = 0f;
//        for (int i = 0; i < nums.length; i++) {
//            total += Float.valueOf(nums[i]);
//        }
//        return String.valueOf(total);
//    }


    /*
    确定点的状态
     */
    public void setCurDial(int position) {
        switch (position) {
            case 0:
                isOneDialChoosed.set(true);
                isTwoDialChoosed.set(false);
                break;
            case 1:
                isOneDialChoosed.set(false);
                isTwoDialChoosed.set(true);
                break;
        }
    }

    /*
    添加viewPager监听器
     */
    public ViewPager.OnPageChangeListener getOnPagerChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurDial(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private List<BillType> billTypes;

    /*
    本地数据库获取所有记账类型
     */
    public List<BillType> getBillTypeListFromDB() {
        TimeDao timeDao = new TimeDao(MyApplication.getContext());
        billTypes = timeDao.getBillTypeList();
        return billTypes;
    }

//    /*
//    设置默认的记账类型（即预判得到的）
//     */
//    public void setDefaultType() {
//        List<BillType> newbillTypes = new ArrayList<>();
//        if (billTypes != null) {
//            newbillTypes = billTypes;
//            Collections.sort(newbillTypes, new Comparator<BillType>() {
//                @Override
//                public int compare(BillType lhs, BillType rhs) {
//                    return lhs.getTime() - lhs.getTime();
//                }
//            });
//        }
//        defaultChooseType.set(newbillTypes.get(0));
//
//        List<BillTypeList.DataEntity> dataEntities = new ArrayList<>();
//        BillTypeList billTypeList = new BillTypeList();
//        BillTypeList.DataEntity dataEntity;
//        for (BillType billType1 : billTypes) {
//            dataEntity = new BillTypeList.DataEntity();
//            dataEntity.setChoose(billType1.getTime());
//            dataEntity.setTime(System.currentTimeMillis());
//            dataEntity.setType_id(billType1.getTypeId() + 1);
//            dataEntities.add(dataEntity);
//        }
//        billTypeList.setData(dataEntities);
//        //服务器请求获取预判的类型并更新
//        MyApplication.getInstance().createApi(RestApi.class)
//                .getMaxType((String) SPUtils.get(appContext, Global.SHARE_PERSINAL_TOKEN, ""), billTypeList)
//                .subscribeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<MaxTypeResponse>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.e(TAG, "服务器获取预判类型完成");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "服务器获取预判类型失败");
//                    }
//
//                    @Override
//                    public void onNext(MaxTypeResponse maxTypeResponse) {
//                        if (maxTypeResponse.getStatus().equals("1")) {
//                            defaultChooseType.set(billTypes.get(maxTypeResponse.getType() - 1));
//                        }
//                    }
//                });
//    }
}
