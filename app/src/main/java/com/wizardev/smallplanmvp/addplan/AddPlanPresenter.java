package com.wizardev.smallplanmvp.addplan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

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
            Plan plan = (Plan) intent.getSerializableExtra("planItem");
            if (plan != null) {
                long id = plan.getId();
                String content = plan.getSomething();
                mView.showPlanContent(id,content);
                Date remindDate = plan.getRemindDate();
                if (remindDate != null) {
                    String dateFormat = "d MMM, yyyy";
                    mView.setDateEditText(formatDate(dateFormat, remindDate));
                } else {
                    mView.showDefaultDate();
                }
            }else {
                mView.showDefaultDate();
            }
        }
    }


    private String formatDate(String formatString, Date dateToFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    @Override
    public void switchClickAction(SwitchCompat switchCompat) {
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // app.send(this, "Action", "Reminder Set");
                }
                else{
                    // app.send(this, "Action", "Reminder Removed");

                }

                if (!isChecked) {
                    //  mUserReminderDate = null;
                }
                //  mUserHasReminder = isChecked;
                //   setDateAndTimeEditText();
                mView.setEnterDateLayoutVisibleWithAnimations(isChecked);
                //  hideKeyboard(mToDoTextBodyEditText);
            }
        });
    }
}
