package com.wizardev.smallplanmvp.plans;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.wizardev.smallplanmvp.R;
import com.wizardev.smallplanmvp.data.Plan;
import com.wizardev.smallplanmvp.data.PlanDataSource;
import com.wizardev.smallplanmvp.data.PlanRepository;

import java.util.List;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/25
 * desc   :
 * version: 1.0
 */
public class PlansPresenter implements PlansContract.Presenter {
    private PlansContract.View mPlansView;
    private PlanDataSource mPlanRepository;
    private Context mContext;

    public PlansPresenter(Context context, PlansContract.View view) {
        mPlansView = view;
        mPlanRepository = PlanRepository.singleInstance(context);
        mContext = context;
    }

    @Override
    public void start() {

    }

    @Override
    public List<Plan> loadAllPlan() {
        return mPlanRepository.loadAllPlan();
    }

    @Override
    public List<Plan> loadFinishPlan() {
        return mPlanRepository.loadFinishPlan();
    }

    @Override
    public List<Plan> loadUnFinishPlan() {
        return mPlanRepository.loadUnFinishPlan();
    }

    @Override
    public void initPopupWindow(final Context context, ImageView imageView, final Plan plan) {
        final PopupMenu popupMenu = new PopupMenu(context, imageView, Gravity.LEFT | Gravity.TOP);
        popupMenu.inflate(R.menu.context_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //将事件设置为已做
                    case R.id.menu_have_done:
                        long idUpdate = plan.getId();
                        //0嗲表
                        updatePlanStatus(idUpdate, plan.getSomething(), plan.getTime(), 0);
                        mPlansView.dismissPopupWindow(popupMenu);
                        mPlansView.refreshAdapter();
                        break;
                    case R.id.menu_delete:
                        long id = plan.getId();
                        deletePlan(id);
                        mPlansView.dismissPopupWindow(popupMenu);
                        mPlansView.refreshAdapter();
                        break;
                }
                return true;
            }
        });
        mPlansView.showPopupWindow(popupMenu);
    }

    @Override
    public void updatePlanStatus(long id, String content, String time, int flag) {
        mPlanRepository.updatePlan(id,content,time,flag);
    }

    @Override
    public void deletePlan(long id) {
        mPlanRepository.deletePlan(id);
    }

    @Override
    public void judgmentHiddenOrShow() {
        mPlansView.menuShowOrHidden();
    }
}
