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
package com.kk.binding.converter;

import android.view.View;

/**
 * @author xuanjue.hk
 * @date 2013-5-28
 */
public class TrueToVisibleConverter implements IValueConverter {

    /*
     * (non-Javadoc)
     *
     * @see binding.kernel.IConverter#converter(java.lang.Object)
     */
    @Override
    public Object converter(Object source) {
        return Boolean.parseBoolean(String.valueOf(source)) ? View.VISIBLE : View.GONE;
    }
}
