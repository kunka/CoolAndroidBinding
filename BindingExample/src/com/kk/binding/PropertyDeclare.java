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

import com.kk.binding.register.PropertyRegister;
import com.kk.control.RatioImageView;
import com.kk.control.WaterFallLayout;

/**
 * Created by hk on 13-12-9.
 */
public class PropertyDeclare {

    public PropertyDeclare() {
        // RatioImageView
        PropertyRegister.register("aspectRatio", float.class, RatioImageView.class, "1");
        PropertyRegister.register("adjustWidth", boolean.class, RatioImageView.class, "false");

        // WaterFallLayout
        PropertyRegister.register("orientation", String.class, WaterFallLayout.class, "vertical");
        PropertyRegister.register("horizontalPadding", float.class, WaterFallLayout.class, "0");
        PropertyRegister.register("verticalPadding", float.class, WaterFallLayout.class, "0");
        PropertyRegister.register("fixedWidthOrHeight", float.class, WaterFallLayout.class, "0");
        PropertyRegister.register("columnOrRowNum", int.class, WaterFallLayout.class, "1");
        PropertyRegister.register("divideSpace", boolean.class, WaterFallLayout.class, "false");
    }
}
