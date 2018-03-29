package com.billingrecovery.app42services;

import android.content.Context;

import com.billingrecovery.libs.AsyncApp42ServiceApi;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;

public class UploadService {

    private AsyncApp42ServiceApi asyncService;

    public UploadService(Context context) {
        asyncService = AsyncApp42ServiceApi.instance(context);
    }

    public void uploadImageCommon(String imagePath, String fileName, String desc, String userName,
                                  UploadFileType fileType, App42CallBack callBack) {
        //utils.showProgress(true, formView, progressView);

        try {
            asyncService.uploadImageForUser(fileName, userName, imagePath, fileType, desc, callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //asyncService.uploadImage(fileName, imagePath, UploadFileType.IMAGE,
        //desc, this);
    }

    /*
    public void getFile(String fileName) {
        //utils.showProgress(true, formView, progressView);
        asyncService.getFile(fileName, this);
        //asyncService.uploadImage(fileName, imagePath, UploadFileType.IMAGE,
        //desc, this);
    }*/

   /* public void getAllFilesByUser(String userName, App42CallBack app42CallBack) {

        asyncService.getAllFilesByUser(userName, app42CallBack);
    }

    public void getFileByUser(String strFileName, String userName, App42CallBack app42CallBack) {

        asyncService.getImageByUser(strFileName, userName, app42CallBack);
    }*/

    public void removeImage(String fileName, String userName, App42CallBack app42CallBack) {
        try {
            asyncService.removeImageByUser(fileName, userName, app42CallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void getImageCount(String userName, App42CallBack app42CallBack) {
        //utils.showProgress(true, formView, progressView);

        asyncService.getImageCount(userName, app42CallBack);
        //asyncService.uploadImage(fileName, imagePath, UploadFileType.IMAGE,
        //desc, this);
    }*/

    /*public void loadImageFromUrl(final String url, final ImageView img) {
        final Handler callerThreadHandler = new Handler();
        new Thread() {
            @Override
            public void run() {
                final Bitmap bitmap = loadBitmap(url);
                callerThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(bitmap);
                    }
                });
            }
        }.start();
    }*/

    // decodes image and scales it to reduce memory consumption
   /* private Bitmap loadBitmap(String url) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            if (url.startsWith("http")) {
                InputStream in = new java.net.URL(url).openStream();
                BitmapFactory.decodeStream(in, null, o);
            } else
                BitmapFactory.decodeFile(url, o);
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < 150 && height_tmp / 2 < 150)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inPreferredConfig = Bitmap.Config.RGB_565;
            o2.inJustDecodeBounds = false;
            if (url.startsWith("http")) {
                InputStream in = new java.net.URL(url).openStream();
                return BitmapFactory.decodeStream(in, null, o2);
            } else
                return BitmapFactory.decodeFile(url, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*public void getProfileImage(String fileName, String userName, App42CallBack app42CallBack) {
        //utils.showProgress(true, formView, progressView);
        //asyncService.getImage(fileName, this);
        asyncService.getImageByUser(fileName, userName, app42CallBack);
    }
*/
   /* @Override
    public void onUploadImageSuccess(Upload response, String fileName, String userName) {
        //utils.showProgress(false, formView, progressView);
        utils.createAlertDialog("SuccessFully Uploaded : " + response);
        //getProfileImage(fileName, userName);
    }

    @Override
    public void onUploadImageFailed(App42Exception ex) {
        // utils.showProgress(false, formView, progressView);
        utils.createAlertDialog("Exception Occurred : " + ex.getMessage());
    }

    @Override
    public void onGetImageSuccess(Upload response) {
        // utils.showProgress(false, formView, progressView);
        String imageUrl = response.getFileList().get(0).getUrl();
        loadImageFromUrl(imageUrl, imgView);
    }

    @Override
    public void onGetImageFailed(App42Exception ex) {
        //utils.showProgress(false, formView, progressView);
        utils.createAlertDialog("Exception Occurred : " + ex.getMessage());
    }

    @Override
    public void onSuccess(Upload response) {
        //utils.showProgress(false, formView, progressView);
        utils.createAlertDialog(response.toString());
    }

    @Override
    public void onSuccess(Object o) {
        //utils.showProgress(false, formView, progressView);
        utils.createAlertDialog(o.toString());
    }

    @Override
    public void onException(Exception e) {
        //utils.showProgress(false, formView, progressView);
    }

    @Override
    public void onException(App42Exception e) {
        //utils.showProgress(false, formView, progressView);
    }*/
}