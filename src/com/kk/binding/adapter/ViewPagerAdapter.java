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

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by hk on 13-12-4.
 */
public class ViewPagerAdapter<T extends ViewPager> {
    private T viewPager;
    protected PagerAdapter mAdapter;

    public ViewPagerAdapter(T viewPager) {
        this.viewPager = viewPager;
    }

    public T getViewPager() {
        return viewPager;
    }

    public void setAdapter(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }
}
