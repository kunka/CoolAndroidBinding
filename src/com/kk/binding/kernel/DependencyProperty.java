/**
 *
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
