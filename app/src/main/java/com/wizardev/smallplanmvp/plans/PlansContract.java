package com.wizardev.smallplanmvp.plans;

import android.content.Context;
import android.widget.ImageView;
import android.support.v7.widget.PopupMenu;

import com.wizardev.smallplanmvp.BasePresenter;
import com.wizardev.smallplanmvp.BaseView;
import com.wizardev.smallplanmvp.data.Plan;

import java.util.List;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/25
 * desc   :
 * version: 1.0
 */
public interface PlansContract {
    interface Presenter extends BasePresenter {
        List<Plan> loadAllPlan();

        List<Plan> loadFinishPlan(int type);

        List<Plan> loadUnFinishPlan(int type);

        void initPopupWindow(Context context,ImageView imageView,Plan plan);

        void updatePlanStatus(long id, String content,String time,int flag);

        void deletePlan(long id);

        void judgmentHiddenOrShow();

    }

    interface View extends BaseView<Presenter> {
        void showAvatar();
        void showPopupWindow(PopupMenu popupMenu);
        void dismissPopupWindow(PopupMenu popupMenu);

        void refreshAdapter();

        void menuShowOrHidden();
    }
}
