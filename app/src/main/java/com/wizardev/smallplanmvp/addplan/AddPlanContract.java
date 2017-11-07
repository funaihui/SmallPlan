package com.wizardev.smallplanmvp.addplan;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;

import com.wizardev.smallplanmvp.BasePresenter;
import com.wizardev.smallplanmvp.BaseView;

import java.util.Date;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/26
 * desc   :
 * version: 1.0
 */
public interface AddPlanContract {
    interface Presenter extends BasePresenter {
        //获得创建计划时的时间
        void obtainCurrentTime();

        //添加计划到本地数据库
        void savePlan(String content);

        //更新数据
        void updatePlan(String planContent);

        //获得Activity传递过来的数据
        void obtainData(Intent intent);

        //点击开关的事件
        void switchClickAction(SwitchCompat switchCompat);

        //设置提醒的文字
        void setReminderTextView();

        void setPickDate();//设置选择的日期

        void setTimeDate();//设置选择的时间

        void setDateAndTimeEditText();

        void setDate(int year, int month, int day);

        void setTime(int hour, int minute);

        void commitAction();//提交按钮执行的动作

    }

    interface View extends BaseView<Presenter> {
        //显示创建计划的时间
        void showCurrentTime(String s);

        //展示计划的内容
        void showPlanContent(long id, String content);

        //设置layout显示和消失时的动画
        void setEnterDateLayoutVisibleWithAnimations(boolean isChecked);

        //显示设置的日期
        void showDateEditText(String dateEditText);

        //显示设置的时间
        void showTimeEditText(String timeEditText);

        //日期栏显示默认的文字
        void showDefaultDate();

        //隐藏提醒时间的文字
        void hideReminderTextView();

        void displayReminderTextView();

        void setReminderTextView(String finalString);

        void showPickDate();

        void showPickTime(Date date);

        void showDateMessage(String content);

        void showErrorReminderText();

    }

}
