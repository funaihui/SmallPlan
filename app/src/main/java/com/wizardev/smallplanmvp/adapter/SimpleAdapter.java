package com.wizardev.smallplanmvp.adapter;

import android.content.Context;

import java.util.List;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/26
 * desc   :
 * version: 1.0
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {


    public SimpleAdapter(Context mContext, List<T> list, int mResId) {
        super(mContext, list, mResId);
    }
}