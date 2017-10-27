package com.wizardev.smallplanmvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;

public class BaseActivity extends AppCompatActivity {

    private ImmersionBar immersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarColor(R.color.colorPrimary).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null) {
            immersionBar.destroy();
        }
    }
}
