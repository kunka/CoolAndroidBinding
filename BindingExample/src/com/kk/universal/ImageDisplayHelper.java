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

package com.kk.universal;

import android.util.Log;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.DebugInfoBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xj on 13-7-7.
 */
public class ImageDisplayHelper {
    private static final String TAG = "ImageDisplayHelper";
    private static DisplayImageOptions options =
            new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .displayer(new DebugInfoBitmapDisplayer())
                    .cacheOnDisc(true)
                    .build();

    public static void displayImageInProperSize(ImageView imageView, String url, int w, int h) {
        if (imageView != null && url != null) {
            String properUrl = configUrlWithSize(url, new ImageSize(w, h));
            Log.d(TAG, "displayImageInProperSize imageView = \n" + imageView
                    + "\n properUrl = " + properUrl);
            ImageLoader.getInstance().displayImage(properUrl, imageView, options, null);
        }
    }

    // demo like this:
    // http://lorempixel.com/200/200/food/1 --> http://lorempixel.com/300/300/food/1
    public static String configUrlWithSize(final String url, final ImageSize imageSize) {
        Pattern pattern = Pattern.compile("/\\d+/\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        String append = "/" + imageSize.getWidth() + "/" + imageSize.getHeight();
        return matcher.find() ? matcher.replaceAll(append) : url;
    }
}
