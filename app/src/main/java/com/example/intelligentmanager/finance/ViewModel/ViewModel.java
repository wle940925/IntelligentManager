package com.example.intelligentmanager.finance.ViewModel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.example.intelligentmanager.finance.MyApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 聂敏萍 on 2017/3/13.
 */

public abstract class ViewModel extends BaseObservable{
    protected Context appContext = MyApplication.getContext();

    /* Just mark a method in ViewModel */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    protected @interface Command {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    protected @interface BindView {
    }
    // ... InstanceState in ViewModel
}
