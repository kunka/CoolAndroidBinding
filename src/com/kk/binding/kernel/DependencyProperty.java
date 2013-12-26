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
package com.kk.binding.kernel;

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 */
public class DependencyProperty {
    private Class<?> propertyType;
    private Class<?> ownerType;
    private String propertyName;
    private Object defaultValue;

    /**
     * @return the propertyType
     */
    public Class<?> getPropertyType() {
        return propertyType;
    }

    /**
     * @return the ownerType
     */
    public Class<?> getOwnerType() {
        return ownerType;
    }

    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @return the defaultValue
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    public DependencyProperty(String propertyName, Class<?> propertyType, Class<?> ownerType, Object defaultValue) {
        this.propertyType = propertyType;
        this.ownerType = ownerType;
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    public DependencyProperty clone() {
        return new DependencyProperty(propertyName, propertyType, ownerType, defaultValue);
    }

}
