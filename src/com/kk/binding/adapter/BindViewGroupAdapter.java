package com.kk.binding.adapter;

import android.view.ViewGroup;

import com.kk.binding.kernel.DependencyObject;
import com.kk.binding.kernel.OnDataContextChanged;
import com.kk.binding.kernel.PropertyChangedEventArgs;
import com.kk.binding.util.BindDesignLog;
import com.kk.binding.view.BindViewUtil;

import java.util.List;

/**
 * Created by hk on 13-12-5.
 */
public class BindViewGroupAdapter<T extends ViewGroup> extends ViewGroupAdapter {
    private int mResId;

    public BindViewGroupAdapter(T viewGroup, int resId) {
        super(viewGroup);
        mResId = resId;
        final DependencyObject dp = BindViewUtil.getDependencyObject(viewGroup);
        dp.setOnDataContextTargetChangedListener(new OnDataContextChanged() {
            @Override
            public boolean onDataContextChanged(DependencyObject dpo, PropertyChangedEventArgs args) {
                BindDesignLog.d("Binding-BindViewGroupAdapter", "setDataContextTargetChanged "
                        + "\n viewGroup = " + (getViewGroup().isInEditMode() ? getViewGroup().getClass().toString() : getViewGroup().toString())
                        + "\n oldValue = " + (args.getOldValue() != null ? args.getOldValue().toString() : null)
                        + "\n newValue = " + (args.getNewValue() != null ? args.getNewValue().toString() : null));

                Object obj = args.getNewValue();
                if (obj instanceof List<?>) {
                    setAdapter(new SimpleBindListAdapter(getViewGroup().getContext(), mResId, (List<?>) obj));
                } else if (obj instanceof Object[]) {
                    setAdapter(new SimpleBindListAdapter(getViewGroup().getContext(), mResId, ((Object[]) obj)));
                } else {
                    setAdapter(null);
                }
                return true;
            }
        });
    }
}
