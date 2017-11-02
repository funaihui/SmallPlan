package com.wizardev.smallplanmvp.addplan;

import android.animation.Animator;
import android.content.Intent;
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

import com.wizardev.smallplanmvp.BaseActivity;
import com.wizardev.smallplanmvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPlanActivity extends BaseActivity implements AddPlanContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_addplan_currenttime)
    TextView tvAddplanCurrenttime;
    @BindView(R.id.et_add_plan)
    EditText mAddPlan;
    @BindView(R.id.dateSwitchCompat)
    SwitchCompat mToDoDateSwitch;
    @BindView(R.id.newPlanDateEditText)
    EditText mDateEditText;
    @BindView(R.id.newPlanTimeEditText)
    EditText mTimeEditText;
    @BindView(R.id.newPlanDateTimeReminderTextView)
    TextView newPlanDateTimeReminderTextView;
    @BindView(R.id.dateLinearLayout)
    LinearLayout mDateLinearLayout;
    private String mCurrentTime;
    private static final String TAG = "AddPlanActivity";
    private AddPlanContract.Presenter mPresenter;
    private long id;

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
                    String plan = mAddPlan.getText().toString().trim();
                    if (TextUtils.isEmpty(plan)) {
                        Toast.makeText(AddPlanActivity.this, "啊哦！计划不能为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (id != 0) {
                            //说明此时为修改
                            mPresenter.updatePlan(id, plan, mCurrentTime, 1);
                        } else {
                            //1,代表未完成
                            mPresenter.addPlan(plan, mCurrentTime, 1);
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
            tvAddplanCurrenttime.setText(s);
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
            // setReminderTextView();
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
    public void setDateEditText(String dateEditText) {
        mDateEditText.setText(dateEditText);
    }

    @Override
    public void showDefaultDate() {
       mDateEditText.setText(getString(R.string.date_reminder_default));
    }


    public void setTimeEditText() {
        String dateFormat;
        if (DateFormat.is24HourFormat(this)) {
            dateFormat = "k:mm";
        } else {
            dateFormat = "h:mm a";

        }
        //  mTimeEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }
}
