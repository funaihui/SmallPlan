package com.wizardev.smallplanmvp.plans;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.wizardev.smallplanmvp.App;
import com.wizardev.smallplanmvp.R;
import com.wizardev.smallplanmvp.data.Plan;
import com.wizardev.smallplanmvp.data.dao.DaoSession;
import com.wizardev.smallplanmvp.data.dao.PlanDao;

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
    private PlanDao mPlanDao;
    private Context mContext;

    public PlansPresenter(Context context, PlansContract.View view) {
        mPlansView = view;
        DaoSession daoSession = ((App) context).getDaoSession();
        mPlanDao = daoSession.getPlanDao();
        mContext = context;
    }

    @Override
    public void start() {

    }

    @Override
    public List<Plan> loadAllPlan() {
        return mPlanDao.loadAll();
    }

    @Override
    public List<Plan> loadFinishPlan(int type) {
        return null;
    }

    @Override
    public List<Plan> loadUnFinishPlan(int type) {
        return null;
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
                        updatePlanStatus(idUpdate,plan.getSomething(),plan.getTime(),0);
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
        Plan plan = new Plan();
        plan.setId(id);
        plan.setFlag(flag);
        plan.setSomething(content);
        plan.setTime(time);
        mPlanDao.update(plan);
    }

    @Override
    public void deletePlan(long id) {
        mPlanDao.deleteByKey(id);
    }

    @Override
    public void judgmentHiddenOrShow() {
        mPlansView.menuShowOrHidden();
    }
}
