package com.kk.binding.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import com.kk.binding.util.BindDesignLog;
import com.kk.binding.view.BindViewUtil;

/**
 * Created by xj on 13-7-6.
 */
public class SimpleBindPagerAdapter extends PagerAdapter {
    private static final String TAG = "Binding-SimpleBindPagerAdapter";
    private Context mContext;
    private int mResId;
    private List<?> mData;

    public SimpleBindPagerAdapter(Context context, int resId, List<?> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    public SimpleBindPagerAdapter(Context context, int mResId, Object[] obj) {
        this(context, mResId, Arrays.asList(obj));
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        viewPager = container;
        if (position >= mData.size()) return null;
        BindDesignLog.d(TAG, "instantiateItem "
                + "\n position = " + position
                + "\n data = " + mData.get(position));

        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(mResId, null);
        BindViewUtil.setDataContext(v, mData.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }

    ViewGroup viewPager = null;

    @Override
    public float getPageWidth(int position) {
//        BindDesignLog.d(TAG, "getPageWidth position = " + position);
//        if (viewPager != null && viewPager.getChildCount() > position) {
//            View child = viewPager.getChildAt(position);
//            int width = 0;
//            if (child != null) {
//                width = child.getMeasuredWidth();
//            }
//            int pagerWidth = viewPager.getMeasuredWidth();
//            BindDesignLog.d(TAG, "getPageWidth "
//                    + "\n width = " + width
//                    + "\n pagerWidth = " + pagerWidth
//                    + "\n position = " + position);
//            if (width > 0 && pagerWidth > 0) {
//                return ((float) width) / pagerWidth;
//            }
//        }
//        return super.getPageWidth(position);
        return 0.8f;
    }
}
