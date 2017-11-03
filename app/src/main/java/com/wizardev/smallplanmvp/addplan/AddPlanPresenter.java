package com.wizardev.smallplanmvp.addplan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.CompoundButton;

import com.wizardev.smallplanmvp.data.Plan;
import com.wizardev.smallplanmvp.data.PlanDataSource;
import com.wizardev.smallplanmvp.data.PlanRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/26
 * desc   :
 * version: 1.0
 */
public class AddPlanPresenter implements AddPlanContract.Presenter {
    private AddPlanContract.View mView;

    private Context mContext;
    private Plan mPlan;
    private static final String TAG = "AddPlanPresenter";
    private Date mRemindDate;
    private  PlanDataSource mPlanRepository = null;

    public AddPlanPresenter(Context context, AddPlanContract.View view) {
        mView = view;
        mContext = context;
        mPlanRepository = PlanRepository.singleInstance(context);
    }

    @Override
    public void start() {

    }

    @Override
    public void obtainCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String rightNow = formatter.format(date);
        mView.showCurrentTime(rightNow);
    }

    @Override
    public void savePlan(String content, String time, int flag) {
       mPlanRepository.addPlan(content,time,flag);
    }

    @Override
    public void updatePlan(long id, String content, String time, int flag) {
        mPlanRepository.updatePlan(id,content,time,flag);
    }

    @Override
    public void obtainData(Intent intent) {
        if (intent != null) {
            mPlan = (Plan) intent.getSerializableExtra("planItem");
            setReminderTextView();
            /*if (plan != null) {
                long id = plan.getId();
                String content = plan.getSomething();
                mView.showPlanContent(id, content);
                Date remindDate = plan.getRemindDate();
                if (remindDate != null) {
                    String dateFormat = "d MMM, yyyy";
                    String formatToUse;
                    //判断是否是24小时显示
                    if (DateFormat.is24HourFormat(mContext)) {
                        formatToUse = "k:mm";
                    } else {
                        formatToUse = "h:mm a";
                    }
                    mView.setDateEditText(formatDate(dateFormat, remindDate));//设置显示的日期
                    mView.showTimeEditText(formatDate(formatToUse,remindDate));//设置显示的时间
                } else {
                 //   handleDefaultTime();
                    mView.showDefaultDate();
                    mView.showTimeEditText(handleDefaultTime());
                }
            } else {
                mView.showDefaultDate();
                mView.showTimeEditText(handleDefaultTime());
            }*/
        }
    }

    private String handleDefaultTime() {
        boolean time24 = DateFormat.is24HourFormat(mContext);
        Calendar cal = Calendar.getInstance();
        if(time24){
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);
        }
        else{
            cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+1);
        }
        cal.set(Calendar.MINUTE, 0);
        mRemindDate = cal.getTime();
        String timeString;
        if(time24){
            timeString = formatDate("k:mm", mRemindDate);
        }
        else{
            timeString = formatDate("h:mm a", mRemindDate);
        }
        return timeString;
    }


    private String formatDate(String formatString, Date dateToFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    @Override
    public void switchClickAction(SwitchCompat switchCompat) {
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // app.send(this, "Action", "Reminder Set");
                } else {
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

    @Override
    public void setReminderTextView() {

        if (mPlan != null) {
            long id = mPlan.getId();
            String content = mPlan.getSomething();
            mView.showPlanContent(id, content);
            mRemindDate = mPlan.getRemindDate();
            if (mRemindDate != null) {
                String dateFormat = "d MMM, yyyy";
                String formatToUse;
                //判断是否是24小时显示
                if (DateFormat.is24HourFormat(mContext)) {
                    formatToUse = "k:mm";
                } else {
                    formatToUse = "h:mm a";
                }
                mView.showDateEditText(formatDate(dateFormat, mRemindDate));//设置显示的日期
                mView.showTimeEditText(formatDate(formatToUse, mRemindDate));//设置显示的时间
            } else {
                //   handleDefaultTime();
                mView.showDefaultDate();
                mView.showTimeEditText(handleDefaultTime());
            }
        } else {
            mView.showDefaultDate();
            mView.showTimeEditText(handleDefaultTime());
            mView.hideReminderTextView();
            Log.i(TAG, "mPlan: "+mPlan);
        }
    }

    @Override
    public void setPickDate() {
        mView.showPickDate();
    }

    @Override
    public void setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if(reminderCalendar.before(calendar)){
            mView.showDateMessage("日期设置不可用！");
            return;
        }

        if(mRemindDate!=null){
            calendar.setTime(mRemindDate);
        }

        if(DateFormat.is24HourFormat(mContext)){
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        } else{
            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mRemindDate = calendar.getTime();
     //   setReminderTextView();
//        setDateAndTimeEditText();
        String dateFormat = "yyyy,MMM d ";
        mView.showDateEditText(formatDate(dateFormat, mRemindDate));//设置显示的日期
      //  setDateEditText();
    }
}
