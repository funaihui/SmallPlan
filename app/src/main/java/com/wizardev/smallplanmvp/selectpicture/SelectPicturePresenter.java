package com.wizardev.smallplanmvp.selectpicture;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.wizardev.smallplanmvp.selectpicture.SelectPictureContract.Presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/25
 * desc   :
 * version: 1.0
 */
public class SelectPicturePresenter implements Presenter{
    private SelectPictureContract.View mView;
    private static final int REQUEST_CAPTURE = 2;
    private static final int REQUEST_PICTURE = 5;
    private static final int RESULT_CROP = 7;
    private static final int GALLERY_ACTIVITY_CODE = 9;
    private Uri imageUri;
    private Uri localUri = null;
    private static final String TAG = "SelectPicturePresenter";

    private Context mContext;
    @Override
    public void start() {

    }

    public SelectPicturePresenter(Context context,SelectPictureContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void selectPictureFromCamera() {
        openCamera();
    }

    @Override
    public void selectPictureFromGallery() {
        Intent gallery_Intent = new Intent(Intent.ACTION_PICK, null);
        gallery_Intent.setType("image/*");
        mView.startResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    @Override
    public void result(int requestCode, int resultCode,Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAPTURE:
                    if (null != imageUri) {
                        localUri = imageUri;
                        performCrop(localUri);
                    }
                    break;
                case REQUEST_PICTURE:
                    localUri = data.getData();
                    performCrop(localUri);
                    break;
                case RESULT_CROP:
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    //判断返回值extras是否为空，为空则说明用户截图没有保存就返回了，此时应该用上一张图，
                    //否则就用用户保存的图
                    if (extras != null) {
                        // avatar.setImageBitmap(mBitmap);
                        // storeImage(mBitmap);
                       // avatar.setImageBitmap(selectedBitmap);
                        mView.setupAvatar(selectedBitmap);
                        storeImage(selectedBitmap);
                    }
                    break;
                case GALLERY_ACTIVITY_CODE:

                    localUri = data.getData();
                    //  setBitmap(localUri);
                    performCrop(localUri);
                    break;
            }
        }
    }
    //裁剪图片
    private void performCrop(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mContext.grantUriPermission("com.android.camera", uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFile().toString());
            mView.startResult(intent,RESULT_CROP);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "你的设备不支持裁剪行为！";
            mView.toast(errorMessage);
        }
    }

    private void openCamera() {  //调用相机拍照
        Intent intent = new Intent();
        File file = getOutputMediaFile(); //工具类稍后会给出
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri = FileProvider.getUriForFile(mContext,mContext.getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            imageUri = Uri.fromFile(file);
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        mView.startResult(intent,REQUEST_CAPTURE);
    }

    //建立保存头像的路径及名称
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + mContext.getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        String mImageName = "avatar.png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    //保存图像
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

}
