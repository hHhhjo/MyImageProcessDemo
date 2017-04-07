package com.example.ljd.myimageprocessdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ljd.myimageprocessdemo.ImageGetter.ImageGalleryLoader;
import com.example.ljd.myimageprocessdemo.ImageGetter.ImageLoader;
import com.example.ljd.myimageprocessdemo.ImageGetter.ImageLocalLoader;
import com.example.ljd.myimageprocessdemo.ImageGetter.ImageNetLoader;
import com.example.ljd.myimageprocessdemo.ImageProcess.ImageProcesser;
import com.example.ljd.myimageprocessdemo.ImageSave.SaveImage;
import com.example.ljd.myimageprocessdemo.Thread.LoadImageThread;
import com.example.ljd.myimageprocessdemo.Thread.ReverseColorThread;
import com.example.ljd.myimageprocessdemo.Thread.SaveImageThread;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private final static boolean DEBUG = true;

    private final static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1111;
    private final static int ACCESS_WIFI_STATE_REQUEST_CODE = 1112;
    private final static int INTERNET_REQUEST_CODE = 1113;
    private final static int ACCESS_NETWORK_STATE_REQUEST_CODE = 1114;
    public static final int GET_IMAGE_FROM_GALLERY_CODE = 1115;

    private final static int UPDATE_IMAGE_VIEW_ORI = 1001;
    private final static int UPDATE_IMAGE_VIEW_DEST = 1002;

    private Handler handler;

    private ImageView imgOrigin;
    private ImageView imgReverse;
    private Button btGetFromNet;
    private Button btGetFromLocal;
    private Button btReverseColor;
    private Button btSaveImage;
    private Button btGetFromGallery;

    private ImageProcesser imageProcesser;
    private ImageLoader imageLoader;

    private Bitmap srcBitmap;
    private Bitmap destBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitRuntimeAuthority();//获取运行时权限
        InitUI();//初始化UI
        Log.v(TAG,"线程："+Thread.currentThread().getId());
        HandleMessage();//处理线程结果，更新UI状态
    }

    private void InitUI(){
        imgOrigin = (ImageView)findViewById(R.id.img_view_ori);
        imgReverse = (ImageView)findViewById(R.id.img_view_rev);
        btGetFromLocal = (Button)findViewById(R.id.bt_get_from_sd);
        btGetFromNet = (Button)findViewById(R.id.bt_get_from_net);
        btGetFromGallery = (Button)findViewById(R.id.bt_get_from_gallery);
        btReverseColor = (Button)findViewById(R.id.bt_reverse);
        btSaveImage = (Button)findViewById(R.id.bt_save);
        btGetFromLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadImageFromLocal();
            }
        });
        btGetFromNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadImageFromNet();
            }
        });
        btReverseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessImage();
            }
        });
        btSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImage2SDCard();
            }
        });
        btGetFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadImageFromGallery();
            }
        });
    }

    private void HandleMessage(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UPDATE_IMAGE_VIEW_ORI:
                        srcBitmap = (Bitmap) msg.obj;
                        if(srcBitmap!=null){
                            imgOrigin.setImageBitmap(srcBitmap);
                        }
                        break;
                    case UPDATE_IMAGE_VIEW_DEST:
                        destBitmap = (Bitmap) msg.obj;
                        if(destBitmap!=null){
                            imgReverse.setImageBitmap(destBitmap);
                        }
                        break;
                }
            }
        };
    }

    private void LoadImageFromLocal(){
        imageLoader = new ImageLocalLoader();
        Runnable runnable = new LoadImageThread(imageLoader,handler,UPDATE_IMAGE_VIEW_ORI);
        Thread thread = new Thread(runnable);
        thread.start();
    }


    private void LoadImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GET_IMAGE_FROM_GALLERY_CODE);
    }

    private void LoadImageFromGalleryOnResult(int requestCode, int resultCode, Intent data){
        imageLoader = new ImageGalleryLoader(requestCode, resultCode, data,this);
        srcBitmap = imageLoader.getImage();
        imgOrigin.setImageBitmap(srcBitmap);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoadImageFromGalleryOnResult(requestCode, resultCode, data);
    }


    private void LoadImageFromNet(){
        imageLoader = new ImageNetLoader();
        Runnable runnable = new LoadImageThread(imageLoader,handler,UPDATE_IMAGE_VIEW_ORI);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void ProcessImage(){
        Runnable runnable = new ReverseColorThread(handler,UPDATE_IMAGE_VIEW_DEST,srcBitmap);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void SaveImage2SDCard(){
        String path = Environment.getExternalStorageDirectory().toString() + File.separator
                + "DCIM/Camera/save_test.png";
        Runnable runnable = new SaveImageThread(destBitmap,path);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void InitRuntimeAuthority() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission. WRITE_EXTERNAL_STORAGE )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission. WRITE_EXTERNAL_STORAGE }
                        , WRITE_EXTERNAL_STORAGE_REQUEST_CODE );
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission. ACCESS_WIFI_STATE )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission. ACCESS_WIFI_STATE }
                        , ACCESS_WIFI_STATE_REQUEST_CODE );
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission. INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission. INTERNET}
                        , INTERNET_REQUEST_CODE );
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, ACCESS_NETWORK_STATE_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果用户没有同意权限
        if (grantResults.length > 0
                && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            switch (requestCode){
                case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:{
                    Toast.makeText(this,"请授权SD卡访问权限，否则会影响程序使用",Toast.LENGTH_SHORT).show();
                    break;
                }
                case ACCESS_WIFI_STATE_REQUEST_CODE:{
                    Toast.makeText(this,"请授权WIFI访问权限，否则会影响程序使用",Toast.LENGTH_SHORT).show();
                    break;
                }
                case INTERNET_REQUEST_CODE:{
                    Toast.makeText(this,"请授权INTERNET访问权限，否则会影响程序使用",Toast.LENGTH_SHORT).show();
                    break;
                }
                case ACCESS_NETWORK_STATE_REQUEST_CODE:{
                    Toast.makeText(this,"请授权NETWORK访问权限，否则会影响程序使用",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }
}
