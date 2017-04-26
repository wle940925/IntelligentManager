package com.example.intelligentmanager.finance.View.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.intelligentmanager.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import com.example.intelligentmanager.finance.MyApplication;
import com.example.intelligentmanager.finance.Utils.ActivityManager;
import com.example.intelligentmanager.finance.View.widget.riseNum.SingleToast;
import com.example.intelligentmanager.finance.ViewModel.ViewModel;

public abstract class BaseActivity <VM extends ViewModel, B extends ViewDataBinding> extends AppCompatActivity {

    protected String TAG = "****" + this.getClass().getSimpleName() + "****";

    private VM viewModel;
    private B binding;

    private SingleToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        toast = new SingleToast(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearReferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        clearReferences();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //吐司
    protected void showBottomToast(String msg) {
        toast.showBottomToast(msg);
    }

    protected void showMiddleToast(String msg) {
        toast.showMiddleToast(msg);
    }

    protected void showNetErrorToast() {
        toast.showBottomToast(getString(R.string.internet_failed));
    }


    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public <T extends Fragment> T getFragment(String tag) {
        return (T) getFragmentManager().findFragmentByTag(tag);
    }

    public void setViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }

    public VM getViewModel() {
        if (viewModel == null) {
            throw new NullPointerException("You should setViewModel first!");
        }
        return viewModel;
    }

    public void setBinding(@NonNull B binding) {
        this.binding = binding;
    }

    public B getBinding() {
        if (binding == null) {
            throw new NullPointerException("You should setBinding first!");
        }
        return binding;
    }


    private void clearReferences() {
        Activity currActivity = MyApplication.getInstance().getCurrentActivity();
        if (this.equals(currActivity))
            MyApplication.getInstance().setCurrentActivity(null);
    }

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

}

