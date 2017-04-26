package com.example.intelligentmanager.finance.View.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.intelligentmanager.R;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.example.intelligentmanager.finance.ViewModel.ViewModel;


/**
 * Created by 聂敏萍 on 2015/11/29.
 */
public class BaseFragment <VM extends ViewModel, B extends ViewDataBinding> extends Fragment{
    private VM viewModel;
    private B binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Shows a {@link Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this.getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }
//
//    /**
//     * 显示吐司
//     */
//    SingleToast mSingleToast;
//
//    public void showBottomToast(String msg) {
//        if (!isResumed() || mSingleToast == null) {
//            return;
//        }
//
//        mSingleToast.showBottomToast(msg);
//    }
//
//    public void showMiddleToast(int id) {
//        if (!isResumed() || mSingleToast == null) {
//            return;
//        }
//        mSingleToast.showMiddleToast(id);
//    }
//
//    public void showMiddleToast(String msg) {
//        if (!isResumed() || mSingleToast == null) {
//            return;
//        }
//        mSingleToast.showMiddleToast(msg);
//    }
//
//    public void showBottomToast(int messageId) {
//        if (!isResumed() || mSingleToast == null) {
//            return;
//        }
//
//        mSingleToast.showBottomToast(messageId);
//    }

//    SweetAlertDialog pDialog;
//
//    /**
//     * 显示loding对话框
//     *
//     * @param context
//     */
//    protected void showLodingDialog(Context context) {
//        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//    }
//
//    /**
//     * 展示信息的对话框
//     *
//     * @param context
//     * @param content
//     * @param onSweetClickListener
//     */
//    protected void showDialog(Context context, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener) {
//        pDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
//        pDialog.setCanceledOnTouchOutside(true);
//        pDialog.setTitleText(content);
//        pDialog.setConfirmClickListener(onSweetClickListener);
//        pDialog.setCancelText("cancle");
//        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                dismissDialog();
//            }
//        });
//        pDialog.show();
//    }
//
//    protected void dismissDialog() {
//        if (pDialog != null) {
//            pDialog.dismiss();
//            pDialog = null;
//        }
//    }

}
