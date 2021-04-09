package com.shanjing.drycargo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanjing.drycargo.R;
import com.shanjing.drycargo.bean.PhotoBean;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<PhotoBean.DataBean, BaseViewHolder> {

    private Context context;
    private String realUrl;

    public HomeAdapter(Context context, int layoutResId) {
        super(layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, PhotoBean.DataBean item) {
        final ImageView iv = helper.getView(R.id.iv);
        helper.addOnClickListener(R.id.iv);

        WebView webView = new WebView(context);
        webView.loadUrl(item.getUrl());
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
                Glide.with(context).load(realUrl).asBitmap().fitCenter().placeholder(R.mipmap.logo).into(iv);
            }
        });
    }
}
