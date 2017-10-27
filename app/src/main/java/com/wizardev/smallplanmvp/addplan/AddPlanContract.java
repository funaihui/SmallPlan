package com.wizardev.smallplanmvp.addplan;

import android.content.Intent;

import com.wizardev.smallplanmvp.BasePresenter;
import com.wizardev.smallplanmvp.BaseView;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/26
 * desc   :
 * version: 1.0
 */
public interface AddPlanContract {
    interface Presenter extends BasePresenter{
        void obtainCurrentTime();

        void addPlan(String content, String time, int flag);
        void updatePlan(long id,String content, String time, int flag);
        void obtainData(Intent intent);
    }

    interface View extends BaseView<Presenter> {
        void showCurrentTime(String s);

        void showPlanContent(long id,String content);
    }

}
