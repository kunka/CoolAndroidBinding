/*
 * Copyright (C) 2013 kk-team.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kk.binding.adapter;

import android.view.ViewGroup;
import com.kk.binding.kernel.DependencyObject;
import com.kk.binding.kernel.OnDataContextChanged;
import com.kk.binding.property.PropertyChangedEventArgs;
import com.kk.binding.util.BindLog;
import com.kk.binding.view.BindViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
                BindLog.d("Binding-BindViewGroupAdapter", "setDataContextTargetChanged "
                        + "\n viewGroup = " + (getViewGroup().isInEditMode() ? getViewGroup().getClass().toString() : getViewGroup().toString())
                        + "\n oldValue = " + (args.getOldValue() != null ? args.getOldValue().toString() : null)
                        + "\n newValue = " + (args.getNewValue() != null ? args.getNewValue().toString() : null));

                Object obj = args.getNewValue();
                SimpleBindListAdapter adapter = (SimpleBindListAdapter) getAdapter();
                if (obj instanceof List<?>) {
                    if (adapter == null) {
                        setAdapter(new SimpleBindListAdapter(getViewGroup().getContext(), mResId, (List<Object>) obj));
                    } else {
                        adapter.setData((List<Object>) obj);
                        adapter.notifyDataSetInvalidated();
                    }
                } else if (obj instanceof Object[]) {
                    if (adapter == null) {
                        setAdapter(new SimpleBindListAdapter(getViewGroup().getContext(), mResId, ((Object[]) obj)));
                    } else {
                        adapter.setData(Arrays.asList(obj));
                        adapter.notifyDataSetInvalidated();
                    }
                } else {
                    setAdapter(null);
                }
                return true;
            }

            @Override public boolean onDataContextInvalidated(DependencyObject dpo, PropertyChangedEventArgs args) {
                if (getAdapter() != null) {
                    SimpleBindListAdapter adapter = (SimpleBindListAdapter) getAdapter();
                    Object obj = args.getNewValue();
                    if (obj instanceof List<?>) {
                        adapter.setData((List<Object>) obj);
                        getAdapter().notifyDataSetInvalidated();
                    } else if (obj instanceof Object[]) {
                        adapter.setData(Arrays.asList(obj));
                        getAdapter().notifyDataSetInvalidated();
                    } else {
                        adapter.setData(new ArrayList<Object>());
                        getAdapter().notifyDataSetInvalidated();
                    }
                }
                return true;
            }
        });
    }
}
