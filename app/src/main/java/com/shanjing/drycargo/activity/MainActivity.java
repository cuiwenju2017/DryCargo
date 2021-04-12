package com.shanjing.drycargo.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shanjing.drycargo.R;
import com.shanjing.drycargo.adapter.BaseRVAdapter;
import com.shanjing.drycargo.adapter.BaseRVHolder;
import com.shanjing.drycargo.api.GitHubService;
import com.shanjing.drycargo.api.MyRetrofit;
import com.shanjing.drycargo.bean.GetDataType;
import com.shanjing.drycargo.bean.PhotoBean;
import com.shanjing.drycargo.utils.DensityUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private int page = 1;
    private SmartRefreshLayout srl;
    private View cl_view;
    private String realUrl;
    private BaseRVAdapter<PhotoBean.DataBean> adapter;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData(GetDataType.GETDATA);

        adapter = new BaseRVAdapter<PhotoBean.DataBean>(R.layout.item_photo) {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onBindVH(BaseRVHolder holder, final PhotoBean.DataBean data, int position) {
                final ImageView iv = holder.getView(R.id.iv);

                ViewGroup.LayoutParams params = iv.getLayoutParams();
                if (position % 2 == 0) {
                    params.height = DensityUtils.dp2px(MainActivity.this, 300);
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    iv.setLayoutParams(params);
                } else {
                    params.height = DensityUtils.dp2px(MainActivity.this, 320);
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    iv.setLayoutParams(params);
                }

                webView = new WebView(MainActivity.this);
                webView.loadUrl(data.getUrl());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient() {
                    //页面加载开始
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    //页面加载完成
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        realUrl = url;
                        //这个realUrl即为重定向之后的地址
                        Glide.with(MainActivity.this).load(realUrl).asBitmap().fitCenter().placeholder(R.mipmap.ic_launcher).into(iv);
                    }
                });

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        webView = new WebView(MainActivity.this);
                        webView.loadUrl(data.getUrl());
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.setWebViewClient(new WebViewClient() {
                            //页面加载开始
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                super.onPageStarted(view, url, favicon);
                            }

                            //页面加载完成
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                realUrl = url;
                                Bundle bundle = new Bundle();
                                bundle.putString("url", realUrl);
                                bundle.putString("id", data.get_id());
                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                intent.setClass(MainActivity.this, PhotoActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        };

        //交错网格布局（瀑布流布局）
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 绑定布局管理器
        rv.setLayoutManager(layoutManager);

        //网格布局
       /* GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(gridLayoutManager);*/

        // 默认提供5种方法（渐显ALPHAIN、缩放SCALEIN、从下到上SLIDEIN_BOTTOM，从左到右SLIDEIN_LEFT、从右到左SLIDEIN_RIGHT）
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        // 自定义动画
        /*adapter.openLoadAnimation(new BaseAnimation() {
            @Override
            public Animator[] getAnimators(View view) {
                return new Animator[]{
                        ObjectAnimator.ofFloat(view, "scaleY", 1, 1.2f, 1),
                        ObjectAnimator.ofFloat(view, "scaleX", 1, 1.2f, 1),
                        ObjectAnimator.ofFloat(view, "rotation", 0f, 180f, 360f)
                };
            }
        });*/

        rv.setAdapter(adapter);
    }

    private void initView() {
        rv = findViewById(R.id.rv);
        srl = findViewById(R.id.srl);
        cl_view = findViewById(R.id.cl_view);
        StatusBarUtil.setTranslucentForImageView(this, 0, cl_view);
        //下拉刷新
        srl.setRefreshHeader(new ClassicsHeader(this).setEnableLastTime(true));
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;
                initData(GetDataType.REFRESH);
                if (srl != null) {
                    refreshLayout.finishRefresh();
                }
            }
        });

        //上拉加载
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                initData(GetDataType.LOADMORE);
                refreshlayout.finishLoadMore(2000);
            }
        });
    }

    private void initData(final int type) {
        GitHubService service = MyRetrofit.getGitHubService();

        Call<PhotoBean> repos = service.dataPhoto(20, page);

        repos.enqueue(new Callback<PhotoBean>() {
            @Override
            public void onResponse(Call<PhotoBean> call, Response<PhotoBean> response) {
                if (response.body().getData().size() < 1) {
                    Toast.makeText(MainActivity.this, "亲，到底啦~", Toast.LENGTH_SHORT).show();
                } else {
                    switch (type) {
                        case GetDataType.GETDATA:
                        case GetDataType.REFRESH:
                            adapter.setNewData(response.body().getData());
                            break;
                        case GetDataType.LOADMORE:
                            adapter.addData(response.body().getData());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<PhotoBean> call, Throwable t) {
                Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
