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

package com.kk.binding.register;

import com.kk.binding.converter.FalseToVisibleConverter;
import com.kk.binding.converter.NotNullToVisibleConverter;
import com.kk.binding.converter.NullToVisibleConverter;
import com.kk.binding.converter.TrueToVisibleConverter;
import com.kk.binding.converter.IValueConverter;
import com.kk.binding.util.StringUtil;

import java.util.HashMap;

/**
 * Created by xj on 14-1-6.
 */
public class ConverterRegister {
    private static final String TAG = "Binding-ConverterRegister";
    private static HashMap<String, Class<? extends IValueConverter>> converters;

    public static HashMap<String, Class<? extends IValueConverter>> getConverters() {
        if (converters == null) {
            converters = new HashMap<String, Class<? extends IValueConverter>>(32);
            registerInner();
        }
        return converters;
    }

    private static void registerInner() {
        register("TrueToVisibleConverter", TrueToVisibleConverter.class);
        register("FalseToVisibleConverter", FalseToVisibleConverter.class);
        register("NullToVisibleConverter", NullToVisibleConverter.class);
        register("NotNullToVisibleConverter", NotNullToVisibleConverter.class);
    }

    public static void register(String converterName, Class<? extends IValueConverter> converterType) {
        if (!StringUtil.isNullOrEmpty(converterName) && converterType != null)
            getConverters().put(converterName, converterType);
    }
}
