package com.shanjing.drycargo.adapter;

import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * RecyclerView Adapter的基类
 */

public abstract class BaseRVAdapter<T> extends BaseQuickAdapter<T, BaseRVHolder> {
    public BaseRVAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    public void onBindViewHolder(BaseRVHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void convert(BaseRVHolder helper, T item) {
        onBindVH(helper, item, helper.getLayoutPosition() - getHeaderLayoutCount());
    }

    public abstract void onBindVH(final BaseRVHolder holder, T data, int position);


    protected void setCheckedChange(CompoundButton cb, final int position) {
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onViewCheckedChangeListener != null) {
                    onViewCheckedChangeListener.onViewCheckedChanged(buttonView, isChecked, position);
                }
            }
        });
    }

    /**
     * @param onViewClickListener
     */
    public OnViewCheckedChangeListener onViewCheckedChangeListener;

    public void setOnViewCheckedChangeListener(OnViewCheckedChangeListener onViewCheckedChangeListener) {
        this.onViewCheckedChangeListener = onViewCheckedChangeListener;
    }

    public interface OnViewCheckedChangeListener {
        void onViewCheckedChanged(CompoundButton buttonView, boolean isChecked, int position);
    }

}
