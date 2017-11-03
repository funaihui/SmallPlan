package com.wizardev.smallplanmvp.addplan;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wizardev.smallplanmvp.BaseActivity;
import com.wizardev.smallplanmvp.R;
import com.wizardev.smallplanmvp.utils.ToastUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPlanActivity extends BaseActivity implements AddPlanContract.View, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_addplan_currenttime)
    TextView createPlanTime;
    @BindView(R.id.et_add_plan)
    EditText mAddPlan;
    @BindView(R.id.dateSwitchCompat)
    SwitchCompat mToDoDateSwitch;
    @BindView(R.id.newPlanDateEditText)
    EditText mDateEditText;
    @BindView(R.id.newPlanTimeEditText)
    EditText mTimeEditText;
    @BindView(R.id.newPlanDateTimeReminderTextView)
    TextView mReminderTextView;
    @BindView(R.id.dateLinearLayout)
    LinearLayout mDateLinearLayout;
    private String mCurrentTime;
    private static final String TAG = "AddPlanActivity";
    private AddPlanContract.Presenter mPresenter;
    private long id;

    private boolean mIsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_add_plan);
        ButterKnife.bind(this);
        setPresenter(new AddPlanPresenter(getApplicationContext(), this));
        setupToolbar();
        mPresenter.obtainCurrentTime();//设置新建计划的时间
        mPresenter.obtainData(intent);//获取传递过来的数据
        mPresenter.switchClickAction(mToDoDateSwitch);//开关按钮的点击事件
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.commit_new_plan);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_commit_plan) {
                    String planContent = mAddPlan.getText().toString().trim();
                    if (TextUtils.isEmpty(planContent)) {
                        Toast.makeText(AddPlanActivity.this, "啊哦！计划不能为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (id != 0) {
                            //说明此时为修改
                            mPresenter.updatePlan(planContent);
                        } else {
                            //1,代表未完成
                            mPresenter.savePlan(planContent);
                        }
                        setResult(0);
                        finish();
                    }
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.newPlanDateEditText, R.id.newPlanTimeEditText})
    public void clickAction(View view) {
        switch (view.getId()) {
            case R.id.newPlanDateEditText:
                //点击设置日期的EditText
                mPresenter.setPickDate();
                break;
            case R.id.newPlanTimeEditText:
                //点击设置时间的EditText
                mPresenter.setTimeDate();
                break;
        }
    }

    @Override
    public void setPresenter(AddPlanContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void showCurrentTime(String s) {
        if (s != null) {
            mCurrentTime = s;
            createPlanTime.setText(s);
        }
    }

    @Override
    public void showPlanContent(long id, String content) {
        this.id = id;
        Log.i(TAG, "showPlanContent: id " + id);
        mAddPlan.setText(content);
        if (!TextUtils.isEmpty(content)) {
            mAddPlan.setSelection(content.length());
        }
    }

    @Override
    public void setEnterDateLayoutVisibleWithAnimations(boolean checked) {
        if (checked) {
            mPresenter.setDateAndTimeEditText();
            mPresenter.setReminderTextView();
            mDateLinearLayout.animate().alpha(1.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mDateLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }
            );
        } else {
            mDateLinearLayout.animate().alpha(0.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mDateLinearLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }
            );
        }
    }

    @Override
    public void showDateEditText(String dateEditText) {
        mDateEditText.setText(dateEditText);
    }

    @Override
    public void showTimeEditText(String timeEditText) {
        mTimeEditText.setText(timeEditText);
    }

    @Override
    public void showDefaultDate() {
        mDateEditText.setText(getString(R.string.date_reminder_default));
    }

    @Override
    public void hideReminderTextView() {
        mReminderTextView.setVisibility(View.GONE);
    }

    @Override
    public void displayReminderTextView() {
        mReminderTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setReminderTextView(String finalString) {
        mReminderTextView.setTextColor(getResources().getColor(R.color.secondary_text));
        mReminderTextView.setText(finalString);
    }

    @Override
    public void showPickDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePick = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePick.show(getFragmentManager(), "datePickDialog");
    }

    @Override
    public void showPickTime(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show(getFragmentManager(), "TimeFragment");
    }

    @Override
    public void showDateMessage(String content) {
        ToastUtils.MyToast(this, content);
    }

    @Override
    public void showErrorReminderText() {
        mReminderTextView.setText(getString(R.string.date_error_check_again));
        mReminderTextView.setTextColor(Color.RED);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mPresenter.setDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mPresenter.setTime(hourOfDay, minute);
    }
}
