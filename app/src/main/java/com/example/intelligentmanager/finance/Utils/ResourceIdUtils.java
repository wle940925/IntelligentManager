package com.example.intelligentmanager.finance.Utils;

import com.example.intelligentmanager.finance.MyApplication;

/**
 * 根据资源文件的名字获取id
 * Created by 聂敏萍 on 2017/3/13.
 */

public class ResourceIdUtils {
    /**
     * @param name 资源文件的名字
     * @param type 资源的类型
     * @return
     */
    public static int getIdOfResource(String name, String type) {
        return MyApplication.getContext().getResources().getIdentifier(name, type, MyApplication.getContext().getPackageName());
    }
}
