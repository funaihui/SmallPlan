package com.wizardev.smallplanmvp.selectpicture;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wizardev.smallplanmvp.BaseActivity;
import com.wizardev.smallplanmvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.wizardev.smallplanmvp.R.id.tv_select_default;
import static com.wizardev.smallplanmvp.R.id.tv_select_photo;

public class SelectPictureActivity extends BaseActivity implements SelectPictureContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.show_avatar)
    ImageView showAvatar;
    @BindView(R.id.tv_select_camera)
    TextView tvSelectCamera;
    @BindView(tv_select_photo)
    TextView tvSelectPhoto;
    @BindView(tv_select_default)
    TextView tvSelectDefault;
    @BindView(R.id.activity_image_selecter)
    RelativeLayout activityImageSelecter;
    private SelectPictureContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        setPresenter(new SelectPicturePresenter(getApplicationContext(),this));
        ButterKnife.bind(this);
        methodRequiresTwoPermission();
    }

    //申请权限
    @AfterPermissionGranted(1)//添加注解，是为了首次执行权限申请后，回调该方法
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //已经申请过权限，直接调用相机
            // openCamera();
        } else {
            EasyPermissions.requestPermissions(this, "需要获取权限",
                    1, perms);
        }
    }

    @OnClick({R.id.tv_select_camera, R.id.tv_select_photo, R.id.tv_select_default})
    public void clickAction(View view) {
        switch (view.getId()) {
            case R.id.tv_select_camera:
                mPresenter.selectPictureFromCamera();
                break;
            case tv_select_photo:
                mPresenter.selectPictureFromGallery();
                break;
            case tv_select_default:
                break;
        }
    }

    @Override
    public void setPresenter(SelectPictureContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    @Override
    public void startResult(Intent intent, int code) {
        startActivityForResult(intent, code);//启动拍照
    }

    @Override
    public void toast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void setupAvatar(Bitmap bitmap) {
        showAvatar.setImageBitmap(bitmap);
    }
}
