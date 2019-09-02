package com.shanjing.drycargo.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shanjing.drycargo.R;
import com.shanjing.drycargo.utils.ButtonUtils;
import com.shanjing.drycargo.utils.ImgDonwload;

public class PhotoActivity extends AppCompatActivity {

    private String url, id;
    private ImageView iv;
    private Button btn;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo);
        //获取控件
        iv = findViewById(R.id.iv);
        btn = findViewById(R.id.btn);
        //获取上个页面穿过来的值
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        id = bundle.getString("id");
        final int permission = ActivityCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        //根据链接加载图片
        Glide.with(this).load(url).crossFade().into(iv);
        //点击下载图片
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick(R.id.btn)) {//不是重复点击
                    //检测是否有写的权限
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // 没有写的权限，去申请写的权限，会弹出对话框
                        ActivityCompat.requestPermissions(PhotoActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    } else {
                        ImgDonwload.donwloadImg(PhotoActivity.this, url, id);
                    }
                } else {//重复点击
                    Toast.makeText(PhotoActivity.this, "请勿重复点击", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
