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
package com.kk.binding.command;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.kk.binding.kernel.BindEngine;
import com.kk.binding.kernel.ICommand;
import com.kk.binding.util.BindDesignLog;

/**
 * Created by hk on 13-12-13.
 */
public class UrlNavCommand implements ICommand {
    @Override
    public void execute(View view, Object... args) {
        if (args.length > 0 && args[0] instanceof String) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse((String) args[0]));
            if (BindEngine.instance().getContext() != null) {
                try {
                    BindEngine.instance().getContext().startActivity(intent);
                } catch (Exception e) {
                    BindDesignLog.e("UrlNavCommand execute exception ", e.toString());
                }
            }
        }
    }
}
