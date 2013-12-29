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

import android.content.Context;
import android.view.View;
import com.kk.binding.view.BindViewUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xj on 13-7-6.
 */
public class SimpleBindListAdapter extends SimpleListAdapter<Object> {
    private static final String TAG = "Binding-SimpleBindListAdapter";

    public SimpleBindListAdapter(Context context, int resId, List<Object> data) {
        super(context, resId, data);
    }

    public SimpleBindListAdapter(Context context, int mResId, Object[] obj) {
        this(context, mResId, Arrays.asList(obj));
    }

    @Override protected void onBindData(View view, Object data, int position) {
        BindViewUtil.setDataContext(view, data);
    }
}
