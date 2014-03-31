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

import android.widget.ListView;
import com.kk.binding.kernel.DependencyObject;
import com.kk.binding.kernel.OnDataContextChanged;
import com.kk.binding.property.PropertyChangedEventArgs;
import com.kk.binding.util.BindLog;
import com.kk.binding.view.BindViewUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hk on 13-12-28.
 */
public class BindListViewAdapter<T extends ListView> extends ListViewAdapter {
    private int mResId;

    public BindListViewAdapter(T viewPager, int resId) {
        super(viewPager);
        mResId = resId;
        final DependencyObject dp = BindViewUtil.getDependencyObject(viewPager);
        dp.setOnDataContextTargetChangedListener(new OnDataContextChanged() {
            @Override
            public boolean onDataContextChanged(DependencyObject dpo, PropertyChangedEventArgs args) {
                BindLog.d("Binding-BindListViewAdapter", "setDataContextTargetChanged "
                        + "\n listView = " + (getListView().isInEditMode() ? getListView().getClass().toString() : getListView().toString())
                        + "\n oldValue = " + (args.getOldValue() != null ? args.getOldValue().toString() : null)
                        + "\n newValue = " + (args.getNewValue() != null ? args.getNewValue().toString() : null));

                Object obj = args.getNewValue();
                if (obj instanceof List<?>) {
                    if (getAdapter() == null) {
                        setAdapter(new SimpleBindListAdapter(getListView().getContext(), mResId, (List<Object>) obj));
                    } else {
                        SimpleBindListAdapter adapter = (SimpleBindListAdapter) getAdapter();
                        adapter.setData((List<Object>) obj);
                    }
                } else if (obj instanceof Object[]) {
                    if (getAdapter() == null) {
                        setAdapter(new SimpleBindListAdapter(getListView().getContext(), mResId, ((Object[]) obj)));
                    } else {
                        SimpleBindListAdapter adapter = (SimpleBindListAdapter) getAdapter();
                        adapter.setData(Arrays.asList(obj));
                    }
                } else {
                    setAdapter(null);
                }
                return true;
            }

            @Override public boolean onDataContextInvalidated(DependencyObject dpo, PropertyChangedEventArgs args) {
                return true;
            }
        });
    }
}
