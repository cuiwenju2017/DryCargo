package com.shanjing.drycargo.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shanjing.drycargo.R;
import com.shanjing.drycargo.adapter.HomeAdapter;
import com.shanjing.drycargo.api.GitHubService;
import com.shanjing.drycargo.bean.PhotoBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<PhotoBean.ResultsBean> list = new ArrayList<>();
    private int page = 1;
    private HomeAdapter homeAdapter;
    private SmartRefreshLayout srl;
    private View cl_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        //设置布局方式,2列，垂直
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        homeAdapter = new HomeAdapter(R.layout.item_photo, list);

        // 默认提供5种方法（渐显ALPHAIN、缩放SCALEIN、从下到上SLIDEIN_BOTTOM，从左到右SLIDEIN_LEFT、从右到左SLIDEIN_RIGHT）
        //homeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        // 自定义动画
        homeAdapter.openLoadAnimation(new BaseAnimation() {
            @Override
            public Animator[] getAnimators(View view) {
                return new Animator[]{
                        /* ObjectAnimator.ofFloat(view, "scaleY", 1, 1.2f, 1),
                         ObjectAnimator.ofFloat(view, "scaleX", 1, 1.2f, 1),*/
                        ObjectAnimator.ofFloat(view, "rotation", 0f, 180f, 360f)
                };
            }
        });

        rv.setAdapter(homeAdapter);

        rv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", list.get(position).getUrl());
                bundle.putString("id", list.get(position).get_id());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, PhotoActivity.class);
                startActivity(intent);
            }
        });

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
                list.clear();
                page = 1;
                initData();
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
                initData();
                refreshlayout.finishLoadMore(2000);
            }
        });
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())//支持把请求的json转换bean
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<PhotoBean> repos = service.dataPhoto("20", page);
        repos.enqueue(new Callback<PhotoBean>() {
            @Override
            public void onResponse(Call<PhotoBean> call, Response<PhotoBean> response) {
                if (response.body().getResults().size() < 1) {
                    Toast.makeText(MainActivity.this, "亲，到底啦~", Toast.LENGTH_SHORT).show();
                } else {
                    list.addAll(response.body().getResults());
                    homeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PhotoBean> call, Throwable t) {
                Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
