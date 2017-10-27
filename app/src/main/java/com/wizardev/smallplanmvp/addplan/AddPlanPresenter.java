package com.wizardev.smallplanmvp.addplan;

import android.content.Context;
import android.content.Intent;

import com.wizardev.smallplanmvp.App;
import com.wizardev.smallplanmvp.data.Plan;
import com.wizardev.smallplanmvp.data.dao.DaoSession;
import com.wizardev.smallplanmvp.data.dao.PlanDao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/26
 * desc   :
 * version: 1.0
 */
public class AddPlanPresenter implements AddPlanContract.Presenter{
    private AddPlanContract.View mView;
    private final PlanDao mPlanDao;

    public AddPlanPresenter(Context context,AddPlanContract.View view) {
        mView = view;
        DaoSession daoSession = ((App)context).getDaoSession();
        mPlanDao = daoSession.getPlanDao();
    }

    @Override
    public void start() {

    }

    @Override
    public void obtainCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat   ("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String rightNow = formatter.format(date);
        mView.showCurrentTime(rightNow);
    }

    @Override
    public void addPlan(String content, String time, int flag) {
        Plan plan = new Plan();
        plan.setFlag(flag);
        plan.setSomething(content);
        plan.setTime(time);
        mPlanDao.insert(plan);
    }

    @Override
    public void updatePlan(long id, String content, String time, int flag) {
        Plan plan = new Plan();
        plan.setId( id);
        plan.setSomething(content);
        plan.setTime(time);
        mPlanDao.update(plan);
    }

    @Override
    public void obtainData(Intent intent) {
        if (intent != null) {
            long id = intent.getLongExtra("id", 0);
            String content = intent.getStringExtra("something");
            mView.showPlanContent(id,content);
        }
    }
}
