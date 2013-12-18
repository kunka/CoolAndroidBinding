package com.kk.binding.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by hk on 13-12-4.
 */
public class ViewPagerAdapter<T extends ViewPager> {

    public interface OnItemClickListener<T> {
        void onItemClick(T viewGroup, View child, int position);
    }

    private T viewPager;
    protected PagerAdapter mAdapter;
    private OnItemClickListener<T> mOnItemClickListener;

    public ViewPagerAdapter(T viewPager) {
        this.viewPager = viewPager;
    }

    public T getViewPager() {
        return viewPager;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }
}
