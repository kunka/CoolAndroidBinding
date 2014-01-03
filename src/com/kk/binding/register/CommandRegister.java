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
import com.kk.binding.util.StringUtil;

import java.util.HashMap;

/**
 * Created by xj on 14-1-1.
 */
public class CommandRegister {
    private static final String TAG = "Binding-CommandRegister";
    private static HashMap<String, Class<?>> commandsHashMap;

    public static HashMap<String, Class<?>> getCommandsHashMap() {
        if (commandsHashMap == null) {
            commandsHashMap = new HashMap<String, Class<?>>(32);
            initCommandRegisterInner();
        }
        return commandsHashMap;
    }

    private static void initCommandRegisterInner() {
        register("urlNavCommand", UrlNavCommand.class);
    }

    public static void register(String commandName, Class<?> commandType) {
        if (!StringUtil.isNullOrEmpty(commandName) && commandType != null)
            getCommandsHashMap().put(commandName, commandType);
    }
}
