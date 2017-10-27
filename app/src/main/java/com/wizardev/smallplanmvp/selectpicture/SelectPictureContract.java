package com.wizardev.smallplanmvp.selectpicture;

import android.content.Intent;
import android.graphics.Bitmap;

import com.wizardev.smallplanmvp.BasePresenter;
import com.wizardev.smallplanmvp.BaseView;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/25
 * desc   :
 * version: 1.0
 */
public interface SelectPictureContract {
    interface Presenter extends BasePresenter {
        void selectPictureFromCamera();

        void selectPictureFromGallery();

        void result(int requestCode, int resultCode,Intent data);
    }

    interface View extends BaseView<Presenter> {
        void startResult(Intent intent, int code);

        void toast(String msg);

        void setupAvatar(Bitmap bitmap);
    }
}
