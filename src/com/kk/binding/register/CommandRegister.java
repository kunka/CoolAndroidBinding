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

import com.kk.binding.command.UrlNavCommand;
import com.kk.binding.kernel.ICommand;
import com.kk.binding.util.StringUtil;

import java.util.HashMap;

/**
 * Created by xj on 14-1-1.
 */
public class CommandRegister {
    private static final String TAG = "Binding-CommandRegister";
    private static HashMap<String, Class<? extends ICommand>> commands;

    public static HashMap<String, Class<? extends ICommand>> getCommands() {
        if (commands == null) {
            commands = new HashMap<String, Class<? extends ICommand>>(32);
            initCommandRegisterInner();
        }
        return commands;
    }

    private static void initCommandRegisterInner() {
        register("urlNavCommand", UrlNavCommand.class);
    }

    public static void register(String commandName, Class<? extends ICommand> commandType) {
        if (!StringUtil.isNullOrEmpty(commandName) && commandType != null)
            getCommands().put(commandName, commandType);
    }
}
