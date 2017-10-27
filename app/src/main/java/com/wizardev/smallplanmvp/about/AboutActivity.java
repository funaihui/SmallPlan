package com.wizardev.smallplanmvp.about;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.wizardev.smallplanmvp.R;

public class AboutActivity extends AppCompatActivity {
    private static final String TAG = "AboutActivity";
    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).init();
        setContentView(R.layout.activity_about);
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);

            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getResources().getString(R.string.about));
        TextView version = (TextView) findViewById(R.id.tv_about_version);
        version.setText(versionName);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void toBlog(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
        intent.setData(Uri.parse("http://www.wizardev.com"));//为Intent设置数据
        startActivity(intent);//将Intent传递给Activity
    }
    public void toProjectPage(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
        intent.setData(Uri.parse("https://github.com/funaihui/SmallPlan"));//为Intent设置数据
        startActivity(intent);//将Intent传递给Activity
    }
}
