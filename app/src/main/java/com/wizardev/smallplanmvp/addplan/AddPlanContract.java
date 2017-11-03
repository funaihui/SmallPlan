package com.wizardev.smallplanmvp.addplan;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;

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
    interface Presenter extends BasePresenter {
        //获得创建计划时的时间
        void obtainCurrentTime();
        //添加计划到本地数据库
        void savePlan(String content, String time, int flag);
        //更新数据
        void updatePlan(long id, String content, String time, int flag);
        //获得Activity传递过来的数据
        void obtainData(Intent intent);
        //点击开关的事件
        void switchClickAction(SwitchCompat switchCompat);
        //设置提醒的文字
        void setReminderTextView();

        void setPickDate();

        void setDate(int year, int month, int day);

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

        void setReminderTextView();

        void showPickDate();

        void showDateMessage(String content);
    }

}
