package com.example.intelligentmanager.finance.View.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.MenuItem;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.databinding.ActivityBillInfoBinding;
import com.example.intelligentmanager.finance.View.fragment.BillInfoFragment;
import com.example.intelligentmanager.finance.ViewModel.BillInfoViewModel;


public class BillInfoActivity extends BaseActivity<BillInfoViewModel, ActivityBillInfoBinding>  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(new BillInfoViewModel());
        setBinding(DataBindingUtil.<ActivityBillInfoBinding>setContentView(this, R.layout.activity_bill_info));
        getBinding().setBillInfo(getViewModel());
        initView();
    }

    private void initView() {
        setSupportActionBar(getBinding().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getBinding().viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        getBinding().tabs.setupWithViewPager(getBinding().viewPager);
    }

    private class FragmentAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"周", "月", "年"};

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("position",+position+"");
            return BillInfoFragment.newInstance(position + "");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
