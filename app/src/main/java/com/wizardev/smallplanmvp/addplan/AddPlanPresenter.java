package com.wizardev.smallplanmvp.addplan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateFormat;
import android.widget.CompoundButton;

import com.wizardev.smallplanmvp.GlobalValues;
import com.wizardev.smallplanmvp.R;
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
    private PlanDataSource mPlanRepository = null;
    private boolean mIsCheck;
    private String mRightNow;

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
        mRightNow = formatter.format(date);
        mView.showCurrentTime(mRightNow);
    }

    @Override
    public void savePlan(String content) {
        Plan plan = new Plan();
        plan.setFlag(1);
        plan.setTime(mRightNow);
        plan.setSomething(content);
        // mPlanRepository.addPlan(content, mRightNow, 1);
        if (mIsCheck) {
            //说明设置计划提醒
            Date date = mRemindDate;
            // String dateString = formatDate("yyyy-MM-dd HH:mm:ss", date);
            plan.setRemindDate(date);
        }
        mPlanRepository.addPlan(plan);
    }

    @Override
    public void updatePlan(String planContent) {
        long id = mPlan.getId();
        String time = mPlan.getTime();
        int flag = mPlan.getFlag();
        if (mIsCheck) {
            //说明设置计划提醒
            Date date = mRemindDate;
            // String dateString = formatDate("yyyy-MM-dd HH:mm:ss", date);
            mPlan.setRemindDate(date);
            mPlanRepository.updatePlan(mPlan);
        } else {
            mPlanRepository.updatePlan(id, planContent, time, flag);
        }
    }

    @Override
    public void obtainData(Intent intent) {
        if (intent != null) {
            mPlan = (Plan) intent.getSerializableExtra("planItem");
            if (mPlan != null) {
                long id = mPlan.getId();
                String content = mPlan.getSomething();
                mView.showPlanContent(id, content);
            }
            setReminderTextView();
        }
    }

    //处理默认显示时间的格式
    private String handleDefaultTime() {
        boolean time24 = DateFormat.is24HourFormat(mContext);
        Calendar cal = Calendar.getInstance();
        if (time24) {
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
        } else {
            cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 1);
        }
        cal.set(Calendar.MINUTE, 0);
        mRemindDate = cal.getTime();
        String timeString;
        if (time24) {
            timeString = formatDate("k:mm", mRemindDate);
        } else {
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
        //判断是否已经设置提醒时间，如果已经设置提醒时间则打开设置时间的布局并将设置的时间显示出来。
        if (mPlan != null) {
            if (mPlan.getRemindDate() != null) {
                switchCompat.setChecked(true);
                mIsCheck = true;
            } else {
                switchCompat.setChecked(false);
                mIsCheck = false;
            }
            mView.setEnterDateLayoutVisibleWithAnimations(mIsCheck);
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mIsCheck = isChecked;
                mView.setEnterDateLayoutVisibleWithAnimations(mIsCheck);
            }
        });
    }

    @Override
    public void setReminderTextView() {
        if (mRemindDate != null && mIsCheck) {
            mView.displayReminderTextView();
            if (mRemindDate.before(new Date())) {
                mView.showErrorReminderText();
                return;
            }
            Date date = mRemindDate;
            String dateString = formatDate("yyyy,MMM d ", date);
            String timeString;
            String amPmString = "";

            if (DateFormat.is24HourFormat(mContext)) {
                timeString = formatDate("k:mm", date);
            } else {
                timeString = formatDate("h:mm", date);
                amPmString = formatDate("a", date);
            }
            String finalString = String.format(mContext.getResources().getString(R.string.remind_date_and_time), dateString, timeString, amPmString);

            mView.setReminderTextView(finalString);
        } else {
            mView.hideReminderTextView();

        }
    }

    //设置日期和时间的显示格式
    private void setDateAndTimeShowFormat() {
        String dateFormat = "yyyy,MMM d ";
        String formatToUse;
        //判断是否是24小时显示
        if (DateFormat.is24HourFormat(mContext)) {
            formatToUse = "k:mm";
        } else {
            formatToUse = "h:mm a";
        }
        mView.showDateEditText(formatDate(dateFormat, mRemindDate));//设置显示的日期
        mView.showTimeEditText(formatDate(formatToUse, mRemindDate));//设置显示的时间
    }

    @Override
    public void setPickDate() {
        mView.showPickDate();
    }

    @Override
    public void setTimeDate() {
        Date date = null;
        //hideKeyboard(mToDoTextBodyEditText);
        if (mPlan != null) {
            if (mPlan.getRemindDate() != null) {
                date = mPlan.getRemindDate();
            } else {
                date = new Date();
            }
        } else {
            date = new Date();
        }
        mView.showPickTime(date);
    }

    @Override
    public void setDateAndTimeEditText() {
        if (mPlan != null) {
            mRemindDate = mPlan.getRemindDate();
        }
        setDefaultDate();
    }

    private void setDefaultDate() {
        if (mRemindDate != null) {
            setDateAndTimeShowFormat();
        } else {
            mView.showDefaultDate();
            mView.showTimeEditText(handleDefaultTime());
        }
    }

    @Override
    public void setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if (reminderCalendar.before(calendar)) {
            mView.showDateMessage("日期设置不可用！");
            return;
        }

        if (mRemindDate != null) {
            calendar.setTime(mRemindDate);
        }

        if (DateFormat.is24HourFormat(mContext)) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        } else {
            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mRemindDate = calendar.getTime();
        setReminderTextView();
        //  setDateAndTimeEditText();

        //  setDateAndTimeShowFormat();
        setDateEditText();
    }

    private void setDateEditText() {
        String dateString = formatDate("yyyy,MMM d ", mRemindDate);
        mView.showDateEditText(dateString);
    }


    @Override
    public void setTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mRemindDate != null) {
            calendar.setTime(mRemindDate);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, hour, minute, 0);
        mRemindDate = calendar.getTime();

        setReminderTextView();
        //   setDateAndTimeEditText();
        //   setDateAndTimeEditText();

        setTimeEditText();
    }

    @Override
    public void commitAction() {
        //如果设置了提醒，则开始计时
        if (mIsCheck) {
            Date remindDate;
            //开始计时
            if (mPlan != null) {
                remindDate = mPlan.getRemindDate();
            } else {
                remindDate = mRemindDate;
            }
            AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent();

            //Intent设置要启动的组件，这里启动广播
            myIntent.setAction(GlobalValues.TIMER_ACTION);
            //PendingIntent对象设置动作,启动的是Activity还是Service,或广播!
            PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, myIntent, 0);
            //注册闹钟
            alarm.set(AlarmManager.RTC_WAKEUP, remindDate.getTime(), sender);
        }
    }

    private void setTimeEditText() {
        String formatToUse;
        //判断是否是24小时显示
        if (DateFormat.is24HourFormat(mContext)) {
            formatToUse = "k:mm";
        } else {
            formatToUse = "h:mm a";
        }
        String dateString = formatDate(formatToUse, mRemindDate);
        mView.showTimeEditText(dateString);
    }
}
