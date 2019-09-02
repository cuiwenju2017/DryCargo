package com.shanjing.drycargo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanjing.drycargo.R;
import com.shanjing.drycargo.bean.PhotoBean;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<PhotoBean.ResultsBean, BaseViewHolder> {

    public HomeAdapter(int layoutResId, @Nullable List<PhotoBean.ResultsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhotoBean.ResultsBean item) {
        helper.addOnClickListener(R.id.iv);
        Glide.with(mContext).load(item.getUrl()).crossFade().into((ImageView) helper.getView(R.id.iv));
    }

}
