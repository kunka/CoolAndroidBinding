/*
 * Copyright (C) 2014 kk-team.com.
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

package com.kk.binding;

import android.widget.ImageView;
import com.kk.binding.kernel.DependencyProperty;
import com.kk.binding.kernel.IBindValueSetter;
import com.kk.control.RatioImageView;
import com.kk.universal.ImageDisplayHelper;

/**
 * Created by hk on 13-12-23.
 */
public class ValueSetter implements IBindValueSetter {
    private static final String TAG = "ValueSetter";

    @Override
    public boolean setValue(Object target, DependencyProperty dp, final Object value, String path) {
        if ("url".equals(dp.getPropertyName()) && ImageView.class.isAssignableFrom(dp.getOwnerType())) {
            if (RatioImageView.class.isAssignableFrom(dp.getOwnerType())) {
                configRatioImageViewWithUrl((RatioImageView) target, (String) value);
            } else {
                configImageViewWithUrl((ImageView) target, (String) value);
            }
            return true;
        }
        return false;
    }

    private void configRatioImageViewWithUrl(final RatioImageView ratioImageView, final String url) {
        if (!configImageViewWithUrl(ratioImageView, url)) {
            ratioImageView.setOnSizeChangedListener(new RatioImageView.OnSizeChangedListener() {
                @Override
                public void onSizeChanged(RatioImageView ratioImageView, int w, int h, int oldw, int oldh) {
//                    L.d(TAG, "onSizeChanged imageView size = (" + ratioImageView.getMeasuredWidth()
//                            + "," + ratioImageView.getMeasuredHeight() + ")");
                    configImageViewWithUrl(ratioImageView, url);
                }
            });
        }
    }

    private boolean configImageViewWithUrl(final ImageView imageView, final String url) {
//        L.d(TAG, "imageView size = (" + imageView.getMeasuredWidth()
//                + "," + imageView.getMeasuredHeight() + ")");
        if (imageView.getMeasuredWidth() > 0 && imageView.getMeasuredHeight() > 0) {
            ImageDisplayHelper.displayImageInProperSize(imageView, url
                    , imageView.getMeasuredWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight()
                    , imageView.getMeasuredHeight() - imageView.getPaddingTop() - imageView.getPaddingBottom());
            return true;
        } else {
//            L.w(TAG, "imageView size zero");
            return false;
        }
    }

}
