package com.kk.binding.adapter;

import android.support.v4.view.ViewPager;

import java.util.List;

import com.kk.binding.kernel.DependencyObject;
import com.kk.binding.kernel.OnDataContextChanged;
import com.kk.binding.kernel.PropertyChangedEventArgs;
import com.kk.binding.util.BindDesignLog;
import com.kk.binding.view.BindViewUtil;

/**
 * Created by hk on 13-12-5.
 */
public class BindViewPagerAdapter<T extends ViewPager> extends ViewPagerAdapter {
    private int mResId;

    public BindViewPagerAdapter(T viewPager, int resId) {
        super(viewPager);
        viewPager.setOffscreenPageLimit(3);
        mResId = resId;
        final DependencyObject dp = BindViewUtil.getDependencyObject(viewPager);
        dp.setOnDataContextTargetChangedListener(new OnDataContextChanged() {
            @Override
            public boolean onDataContextChanged(DependencyObject dpo, PropertyChangedEventArgs args) {
                BindDesignLog.d("Binding-BindViewPagerAdapter", "setDataContextTargetChanged "
                        + "\n viewPager = " + (getViewPager().isInEditMode() ? getViewPager().getClass().toString() : getViewPager().toString())
                        + "\n oldValue = " + (args.getOldValue() != null ? args.getOldValue().toString() : null)
                        + "\n newValue = " + (args.getNewValue() != null ? args.getNewValue().toString() : null));

                Object obj = args.getNewValue();
                if (obj instanceof List<?>) {
                    setAdapter(new SimpleBindPagerAdapter(getViewPager().getContext(), mResId, (List<?>) obj));
                } else if (obj instanceof Object[]) {
                    setAdapter(new SimpleBindPagerAdapter(getViewPager().getContext(), mResId, ((Object[]) obj)));
                } else {
                    setAdapter(null);
                }
                return true;
            }
        });
    }
}
