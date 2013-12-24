/**
 *
 */
package com.kk.binding.kernel;

import com.kk.binding.feature.IBindDataProvider;
import com.kk.binding.listener.ListenerToCommand;
import com.kk.binding.util.BindDesignLog;
import com.kk.binding.view.ViewFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 */
public class DependencyObject {
    private static final String TAG = "Binding-DependencyObject";
    private HashMap<DependencyProperty, Object> values = new HashMap<DependencyProperty, Object>();
    private ArrayList<Binding> bindings = new ArrayList<Binding>();
    private ArrayList<CommandBinding> cmdBindings = new ArrayList<CommandBinding>();

    private Object originTarget;
    private Object dataContext;
    private Object resolvedTargetObject;
    private Object oldTargetObject;

    private OnDataContextChanged dataContextTargetChangedListener;

    public void setOnDataContextTargetChangedListener(OnDataContextChanged dataContextChangedListener) {
        this.dataContextTargetChangedListener = dataContextChangedListener;
    }

    /**
     * @return the bindings
     */
    public ArrayList<Binding> getBindings() {
        return bindings;
    }

    /**
     * @return the cmdBindings
     */
    public ArrayList<CommandBinding> getCmdBindings() {
        return cmdBindings;
    }

    /**
     * @return the originTarget
     */
    public Object getOriginTarget() {
        return originTarget;
    }

    public Object getDataContext() {
        return dataContext;
    }

    /**
     * @param dataContext
     * @return true if the child will handle it
     */
    public boolean setDataContext(Object dataContext) {
        this.dataContext = dataContext;
        resolveTargetObject(dataContext);
        Object targetObject = getResolvedTargetObject();
        boolean handled = false;
        if (targetObject != oldTargetObject && dataContextTargetChangedListener != null) {
            BindDesignLog.d(TAG, "dataContext Changed: \ntarget= "
                    + (originTarget != null ? (BindDesignLog.isInDesignMode() ? originTarget.getClass().toString() : originTarget.toString()) : null)
                    + "\n oldValue= " + (oldTargetObject != null ? oldTargetObject.toString() : null)
                    + "\n newValue= " + (targetObject != null ? targetObject.toString() : null));
            handled = dataContextTargetChangedListener.onDataContextChanged(this, new PropertyChangedEventArgs(ViewFactory.BINDING_DATA_CONTEXT, oldTargetObject, targetObject));
        }

        oldTargetObject = targetObject;
        // child will handle it
        if (handled)
            return true;

        // we do the handle here
        for (Binding binding : bindings) {
            binding.setDataContext(targetObject);
        }
        // TODO: handle command
        for (CommandBinding cmdBinding : cmdBindings) {
            cmdBinding.setDataContext(targetObject);
        }
        return false;
    }

    /**
     * resolve the target object to bind
     *
     * @param dataContext
     */
    public void resolveTargetObject(Object dataContext) {
        for (Binding binding : bindings) {
            if (binding.getDependencyProperty() != null && ViewFactory.BINDING_DATA_CONTEXT.equals(binding.getDependencyProperty().getPropertyName())) {
                Object value = parseBindValue(dataContext, binding.getPath());
                value = converterValue(value, binding.getValueConverter());
                resolvedTargetObject = value;

                return;
            }
        }
        resolvedTargetObject = dataContext;
    }

    public Object getResolvedTargetObject() {
        return resolvedTargetObject;
    }

    /**
     * @param originTarget the originTarget to set
     */
    public void setOriginTarget(Object originTarget) {
        this.originTarget = originTarget;
    }

    /**
     * @return the value
     */
    public Object getValue(DependencyProperty dp) {
        if (values.containsKey(dp)) {
            return values.get(dp);
        } else {
            return dp.getDefaultValue();
        }
    }

    /**
     * TODO:cache the methods to optimize performance
     *
     * @param value the value to set
     */
    public void setValue(DependencyProperty dp, Object value, String path) {
        if (originTarget == null)
            return;
        Object theOldValue = values.get(dp);
        if (theOldValue == value)
            return;

        if (BindEngine.getBindValueSetter() != null && BindEngine.getBindValueSetter().setValue(originTarget, dp, value, path)) {
            BindDesignLog.d(TAG, "BindValueSetter setValue: target = \n"
                    + (BindDesignLog.isInDesignMode() ? originTarget.getClass().toString() : originTarget.toString())
                    + "\n propertyName = " + dp.getPropertyName()
                    + "\n propertyType = " + dp.getPropertyType()
                    + "\n path = " + path
                    + "\n value = " + (value != null ? value.toString() : null));

            values.put(dp, value);
            return;
        }

        String fieldName = dp.getPropertyName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String setMethodName = "set" + firstLetter + fieldName.substring(1);
        Method setMethod = null;

        // parse value
        Class<?> propertyType = dp.getPropertyType();//type = int.class
        Object setValue = null;// value = 1
        if (value != null && propertyType.isPrimitive()) {
            String property = value.toString(); // property = "1"
            if (propertyType == int.class) {
                setValue = Integer.valueOf(property);
            } else if (propertyType == byte.class) {
                setValue = Byte.valueOf(property);
            } else if (propertyType == char.class) {
                setValue = String.valueOf(property);
            } else if (propertyType == short.class) {
                setValue = Short.valueOf(property);
            } else if (propertyType == boolean.class) {
                setValue = Boolean.valueOf(property);
            } else if (propertyType == long.class) {
                setValue = Long.valueOf(property);
            } else if (propertyType == float.class) {
                setValue = Float.valueOf(property);
            } else if (propertyType == double.class) {
                setValue = Short.valueOf(property);
            } else {
                if (BindDesignLog.isInDesignMode())
                    throw new RuntimeException("Wrong base type in " + propertyType.toString());
            }
        } else {
            setValue = value;
        }

        BindDesignLog.d(TAG, "setValue: target = \n"
                + (BindDesignLog.isInDesignMode() ? originTarget.getClass().toString() : originTarget.toString())
                + "\n propertyName = " + dp.getPropertyName()
                + "\n setMethodName = " + setMethodName
                + "\n propertyType = " + dp.getPropertyType()
                + "\n path = " + path
                + "\n value = " + (value != null ? value.toString() : null)
                + "\n setValue = " + (setValue != null ? (setValue.getClass().toString() + setValue.toString()) : null));

        // get set method
        try {
            setMethod = originTarget.getClass().getMethod(setMethodName, dp.getPropertyType());
        } catch (Exception e) {
            BindDesignLog.e(TAG, "getMethod failed, targetClass = \n" + originTarget.getClass().toString()
                    + "\n e = " + e.toString());
        }

        // invoke
        if (setMethod != null && originTarget != null) {
            try {
                setMethod.setAccessible(true);
                setMethod.invoke(originTarget, setValue);
                values.put(dp, value);
            } catch (Exception e) {
                BindDesignLog.e(TAG, "invoke failed, targetClass = \n" + originTarget.getClass().toString()
                        + "\n setMethod = " + setMethod
                        + "\n e = " + e.toString());
            }
        }
    }

    public void setCmdBindings(ListenerToCommand ltc, final CommandBinding commandBinding) {
        if (ltc == null || commandBinding == null)
            return;
        BindDesignLog.d(TAG, "setCmdBindings: target = \n"
                + (originTarget != null ? (BindDesignLog.isInDesignMode() ? originTarget.getClass().toString() : originTarget.toString()) : null)
                + "\n commandName = " + commandBinding.getCommandName()
                + "\n commandParamPath = " + commandBinding.getCommandParamPath());
        commandBinding.setListenerToCommand(ltc);
        cmdBindings.add(commandBinding);
    }

    /**
     * @param binding the bindings to set
     */
    public void setBindings(DependencyProperty dp, final Binding binding) {
        if (dp == null || binding == null)
            return;
        BindDesignLog.d(TAG, "setBindings: target = \n"
                + (originTarget != null ? (BindDesignLog.isInDesignMode() ? originTarget.getClass().toString() : originTarget.toString()) : null)
                + "\n propertyName = " + dp.getPropertyName()
                + "\n path = " + binding.getPath());
        binding.setDependencyProperty(dp);
        binding.setDependencyObject(this);
        bindings.add(binding);
    }

    public static Object parseBindValue(Object target, String bindExpression) {
        if (bindExpression == null || target == null)
            return null;
        if (bindExpression.startsWith("data\\.")) {
            if (target instanceof IBindDataProvider) {
                target = ((IBindDataProvider) target).getData();
                bindExpression = bindExpression.substring(5);
            }
        }
        if (target == null)
            return null;
        if (bindExpression.startsWith("$"))
            return bindExpression.substring(1);
        Object value = target;
        String[] list = bindExpression.split("\\.");
        for (String aList : list) {

            if (aList.endsWith("]")) {
                value = parseArray(target, aList);
            } else {
                if (target instanceof Map<?, ?>) {
                    value = ((Map<?, ?>) target).get(aList);
                } else {
                    value = getFieldValueByName(aList, target);
                }
            }
            target = value;
            if (target == null)
                break;
        }
        return value;
    }

    public static Object parseArray(Object target, String expression) {
        int length = expression.length();
        int last = expression.lastIndexOf("[");
        if (last > 0 && length - last > 1) {
            String indexStr = expression.substring(last + 1, length - 1);
            int index = 0;
            try {
                index = Integer.parseInt(indexStr);
            } catch (NumberFormatException e) {
                index = -1;
            }
            if (index >= 0) {
                target = parseBindValue(target, expression.substring(0, last));
                if (target == null)
                    return null;
                if (target instanceof List<?>) {
                    List<?> list = (List<?>) target;
                    if (index < list.size())
                        return list.get(index);
                } else if (target instanceof Object[]) {
                    Object[] objects = (Object[]) target;
                    if (index < objects.length)
                        return objects[index];
                }
            }
        }
        return null;
    }

    /**
     *
     */
    public void resetBinding() {
        // TODO Auto-generated method stub

    }

    /**
     * @param fieldName
     * @param o
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        if (o == null || fieldName == null) {
            return null;
        }
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            return method.invoke(o);
        } catch (Exception e) {
            BindDesignLog.d(TAG, "getFieldValueByName: e = " + e);
            return null;
        }
    }

    public static Object converterValue(Object value, IValueConverter converter) {
        if (converter == null) return value;
        try {
            return converter.converter(value);
        } catch (Exception e) {
            BindDesignLog.e(TAG, "converter exception " + e.toString());
            return null;
        }
    }

}