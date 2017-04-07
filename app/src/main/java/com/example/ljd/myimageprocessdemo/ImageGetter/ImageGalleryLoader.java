package com.example.ljd.myimageprocessdemo.ImageGetter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.ljd.myimageprocessdemo.MainActivity;

/**
 * Created by ljd-pc on 2017/4/7.
 */
public class ImageGalleryLoader extends ImageLoader {

    private int requestCode;
    private int resultCode;
    private Intent data;
    private Activity activity;
    private Bitmap bitmap;
    private String imagePath;

    public ImageGalleryLoader(int requestCode, int resultCode, Intent data,Activity activity){
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        this.activity = activity;
    }
    @Override
    public Bitmap getImage() {
        if (requestCode == MainActivity.GET_IMAGE_FROM_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = activity.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            bitmap = BitmapFactory.decodeFile(imagePath);
            c.close();
        }
        return bitmap;
    }

    public String getImagePath() {
        return imagePath;
    }
}
