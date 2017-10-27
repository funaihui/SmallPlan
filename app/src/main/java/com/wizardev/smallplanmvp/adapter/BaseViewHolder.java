package com.wizardev.smallplanmvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/26
 * desc   :
 * version: 1.0
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SparseArray<View> views;
    private static final String TAG = "BaseViewHolder";
    private BaseAdapter.OnItemClickListener mItemClickListener;
    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        views = new SparseArray<>();
        this.mItemClickListener = listener;
        itemView.setOnClickListener(this);
    }

    public View getView(int viewId){
        return getId(viewId);
    }
    protected <T extends View> T  getId(int resId){
        View view = views.get(resId);
        if (view == null){
            view = itemView.findViewById(resId);
            views.put(resId,view);
        }
        return (T) view;
    }

    public TextView getTextView(int viewId){
        return getId(viewId);
    }


    @Override
    public void onClick(View view) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(view,getLayoutPosition());
        }
    }
}
