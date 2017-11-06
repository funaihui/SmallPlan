package com.wizardev.smallplanmvp.data;

import android.content.Context;

import com.wizardev.smallplanmvp.App;
import com.wizardev.smallplanmvp.data.dao.DaoSession;
import com.wizardev.smallplanmvp.data.dao.PlanDao;

import java.util.List;

/**
 * anthor : wizardev
 * email : wizarddev@163.com
 * time : 17-11-3
 * desc : 操作数据的增删改查
 * version : 1.0
 */

public class PlanRepository implements PlanDataSource{
    private final PlanDao mPlanDao;
    private Context mContext;
    private static PlanRepository mPlanRepository;

    private PlanRepository(Context context) {
        mContext = context;
        DaoSession daoSession = ((App) context).getDaoSession();
        mPlanDao = daoSession.getPlanDao();
    }

    public static PlanRepository singleInstance(Context context) {
        if (mPlanRepository != null) {
            return mPlanRepository;
        } else {
            mPlanRepository = new PlanRepository(context);
        }
        return mPlanRepository;
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
    public void addPlan(Plan plan) {
        mPlanDao.insert(plan);
    }

    @Override
    public void updatePlan(long id, String content, String time, int flag) {
        //更新时必须为每个字段设置值，否则字段会被重新设置为默认值
        Plan plan = new Plan();
        plan.setId(id);
        plan.setSomething(content);
        plan.setTime(time);
        plan.setFlag(flag);
        mPlanDao.update(plan);
    }

    @Override
    public void updatePlan(Plan plan) {
        mPlanDao.update(plan);
    }

    @Override
    public void deletePlan(long id) {
        mPlanDao.deleteByKey(id);
    }

    @Override
    public List<Plan> loadAllPlan() {
        return mPlanDao.loadAll();
    }

    @Override
    public List<Plan> loadFinishPlan() {
        return mPlanDao.queryBuilder().where(PlanDao.Properties.Flag.eq(0)).build().list();
    }

    @Override
    public List<Plan> loadUnFinishPlan() {
        return mPlanDao.queryBuilder().where(PlanDao.Properties.Flag.eq(1)).build().list();
    }
}
