package com.shanjing.drycargo.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shanjing.drycargo.R;
import com.shanjing.drycargo.api.GitHubService;
import com.shanjing.drycargo.api.MyRetrofit;
import com.shanjing.drycargo.bean.PhotoBean;
import com.shanjing.drycargo.utils.ButtonUtils;
import com.shanjing.drycargo.utils.ImgDonwload;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private Button btn, btn_shuaxin;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String url;
    private static ProgressDialog mSaveDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initView();
        mSaveDialog = ProgressDialog.show(MainActivity.this, "提示", "加载中，请稍等...", true);
        initData();
    }

    private void initView() {
        //获取控件
        iv = findViewById(R.id.iv);
        btn = findViewById(R.id.btn);
        btn_shuaxin = findViewById(R.id.btn_shuaxin);

        //点击下载图片
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick(R.id.btn)) {//不是重复点击
                    //检测是否有写的权限
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        // 没有写的权限，去申请写的权限，会弹出对话框
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    } else {
                        ImgDonwload.donwloadImg(MainActivity.this, url, String.valueOf(System.nanoTime()));
                    }
                } else {//重复点击
                    Toast.makeText(MainActivity.this, "请勿重复点击", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_shuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick(R.id.btn)) {//不是重复点击
                    mSaveDialog = ProgressDialog.show(MainActivity.this, "提示", "加载中，请稍等...", true);
                    initData();
                } else {//重复点击
                    Toast.makeText(MainActivity.this, "请勿重复点击", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ImgDonwload.donwloadImg(MainActivity.this, url, String.valueOf(System.nanoTime()));
        } else {
            Toast.makeText(this, "同意权限后方能下载", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        GitHubService service = MyRetrofit.getGitHubService();
        final Call<PhotoBean> repos = service.dataPhoto();

        repos.enqueue(new Callback<PhotoBean>() {
            @Override
            public void onResponse(Call<PhotoBean> call, final Response<PhotoBean> response) {
                if (response.body().getImgurl() != null) {
                    mSaveDialog.dismiss();
                    url = response.body().getImgurl();
                    //根据链接加载图片
                    Glide.with(MainActivity.this).load(url).into(iv);
                }
            }

            @Override
            public void onFailure(Call<PhotoBean> call, Throwable t) {
                mSaveDialog.dismiss();
                Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
