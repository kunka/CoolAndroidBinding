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

import com.kk.binding.register.BindablePropertyRegister;
import com.kk.control.RatioImageView;
import com.kk.control.WaterFallLayout;

/**
 * Created by hk on 13-12-9.
 */
public class PropertyDeclare {

    public PropertyDeclare() {
        // RatioImageView
        BindablePropertyRegister.register("aspectRatio", float.class, RatioImageView.class, "1");
        BindablePropertyRegister.register("adjustWidth", boolean.class, RatioImageView.class, "false");

        // WaterFallLayout
        BindablePropertyRegister.register("orientation", String.class, WaterFallLayout.class, "vertical");
        BindablePropertyRegister.register("horizontalPadding", float.class, WaterFallLayout.class, "0");
        BindablePropertyRegister.register("verticalPadding", float.class, WaterFallLayout.class, "0");
        BindablePropertyRegister.register("fixedWidthOrHeight", float.class, WaterFallLayout.class, "0");
        BindablePropertyRegister.register("columnOrRowNum", int.class, WaterFallLayout.class, "1");
        BindablePropertyRegister.register("divideSpace", boolean.class, WaterFallLayout.class, "false");
    }
}
