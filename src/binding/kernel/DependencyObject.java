/**
 *
 */
package binding.kernel;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import binding.feature.IBindDataProvider;
import binding.listener.ListenerToCommand;
import binding.util.BindDesignLog;
import binding.view.ViewFactory;

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 */
public class DependencyObject {
    private static final String TAG = "Binding-DependencyObject";
    private HashMap<DependencyProperty, Object> values = new HashMap<DependencyProperty, Object>();
    private HashMap<DependencyProperty, Binding> bindings = new HashMap<DependencyProperty, Binding>();
    private HashMap<ListenerToCommand, CommandBinding> cmdBindings = new HashMap<ListenerToCommand, CommandBinding>();

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
    public HashMap<DependencyProperty, Binding> getBindings() {
        return bindings;
    }

    /**
     * @return the cmdBindings
     */
    public HashMap<ListenerToCommand, CommandBinding> getCmdBindings() {
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
        for (java.util.Map.Entry<DependencyProperty, Binding> e : getBindings().entrySet()) {
            if (!ViewFactory.BINDING_DATA_CONTEXT.equals(e.getKey().getPropertyName())) {
                e.getValue().setDataContext(targetObject);
                onDataContextChanged(e.getKey(), e.getValue());
            }
        }
        // TODO: handle command
        for (java.util.Map.Entry<ListenerToCommand, CommandBinding> e : getCmdBindings().entrySet()) {
            e.getValue().setDataContext(targetObject);
            onCmdDataContextChanged(e.getKey(), e.getValue());
        }
        return false;
    }

    /**
     * resolve the target object to bind
     *
     * @param dataContext
     */
    public void resolveTargetObject(Object dataContext) {
        for (java.util.Map.Entry<DependencyProperty, Binding> e : getBindings().entrySet()) {
            if (ViewFactory.BINDING_DATA_CONTEXT.equals(e.getKey().getPropertyName())) {
                Object value = parseBindValue(dataContext, e.getValue().getPath());
                IValueConverter converter = e.getValue().getValueConverter();
                if (converter != null) {
                    value = converter.converter(value);
                }
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
        String property = value.toString(); // property = "1"
        Object setValue = null;// value = 1
        if (propertyType.isPrimitive()) {
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
            Log.e(TAG, "getMethod failed, targetClass = \n" + originTarget.getClass().toString()
                    + "\n e = " + e.toString());
        }

        // invoke
        if (setMethod != null && originTarget != null) {
            try {
                setMethod.setAccessible(true);
                setMethod.invoke(originTarget, setValue);
                values.put(dp, value);
            } catch (Exception e) {
                Log.e(TAG, "invoke failed, targetClass = \n" + originTarget.getClass().toString()
                        + "\n setMethod = " + setMethod
                        + "\n e = " + e.toString());
            }
        }
    }

    private void onDataContextChanged(DependencyProperty dp, Binding binding) {
        if (dp == null || binding == null || binding.getDataContext() == null)
            return;

        if (!ViewFactory.BINDING_DATA_CONTEXT.equals(dp.getPropertyName())) {
            BindDesignLog.d(TAG, "OnDataContextChanged: target = \n"
                    + (originTarget != null ? (BindDesignLog.isInDesignMode() ? originTarget.getClass().toString() : originTarget.toString()) : null)
                    + "\n propertyName = " + dp.getPropertyName()
                    + "\n path = " + binding.getPath()
                    + "\n dataContext = " + binding.getDataContext());

            Object value = parseBindValue(binding.getDataContext(), binding.getPath());
            IValueConverter converter = binding.getValueConverter();
            if (converter != null) {
                value = converter.converter(value);
            }
            setValue(dp, value, binding.getPath());
        }
    }

    private void onCmdDataContextChanged(ListenerToCommand ltc, CommandBinding binding) {
        if (ltc == null || binding == null || binding.getDataContext() == null)
            return;

        BindDesignLog.d(TAG, "OnCmdDataContextChanged: target = \n"
                + (originTarget != null ? (BindDesignLog.isInDesignMode() ? originTarget.getClass().toString() : originTarget.toString()) : null)
                + "\n cmd = " + ltc.getCommand()
                + "\n param = " + ltc.getParam()
                + "\n cmdParamPath = " + binding.getCommandParamPath()
                + "\n cmdName = " + binding.getCommandName()
                + "\n dataContext = " + binding.getDataContext());

        Object value = parseBindValue(binding.getDataContext(), binding.getCommandParamPath());
        ltc.setParam(value);
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
                + "\n path = " + binding.getPath()
                + "\n dataContext = " + binding.getDataContext());

        Binding theOld = bindings.get(dp);
        if (theOld != null && theOld != binding) {
            if (theOld instanceof INotifyPropertyChanged) {
                ((INotifyPropertyChanged) binding.getDataContext()).setPropertyChangedListener(null);
            }
        }
        if (!bindings.containsKey(dp)) {
            bindings.put(dp, binding);
            if (binding.getDataContext() instanceof INotifyPropertyChanged) {
                ((INotifyPropertyChanged) binding.getDataContext()).setPropertyChangedListener(new IPropertyChanged() {

                    @Override
                    public void propertyChanged(Object sender, PropertyChangedEventArgs args) {
                        if (binding.getDataContext() != null) {
                            DependencyProperty d = null;
                            for (Entry<DependencyProperty, Binding> e : bindings.entrySet()) {
                                if (e.getValue().getPath().equals(args.getName())) {
                                    d = e.getKey();
                                    break;
                                }
                            }

                            if (d != null) {
                                if (!ViewFactory.BINDING_DATA_CONTEXT.equals(args.getName())) {
                                    Object value = parseBindValue(binding.getDataContext(), args.getName());
                                    IValueConverter converter = binding.getValueConverter();
                                    if (converter != null) {
                                        value = converter.converter(value);
                                    }
                                    setValue(d, value, args.getName());
                                }
                            }
                        }
                    }
                });
            }
        }
        onDataContextChanged(dp, binding);
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

    public void setCmdBindings(ListenerToCommand cmd, final CommandBinding binding) {
        if (cmd == null || binding == null)
            return;
        BindDesignLog.d(TAG, "setCmdBindings: target = \n"
                + (originTarget != null ? (BindDesignLog.isInDesignMode() ? originTarget.getClass().toString() : originTarget.toString()) : null)
                + "\n cmd = " + cmd.getCommand()
                + "\n commandParamPath = " + binding.getCommandParamPath()
                + "\n dataContext = " + binding.getDataContext());

        CommandBinding theOld = cmdBindings.get(cmd);
        if (theOld != null && theOld != binding) {
            // th
        }
        cmdBindings.put(cmd, binding);
        Object param = parseBindValue(binding.getDataContext(), binding.getCommandParamPath());
        cmd.setParam(param);
//        Object value = getPublicFieldValueByName(binding.getCommandName(), binding.getTargetObject());
//        cmd.setCommand((ICommand) value);
    }

    /**
     *
     */
    public void resetBinding() {
        // TODO Auto-generated method stub

    }

    /**
     * 通过反射获取类的私有属性值
     *
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
            return null;
        }
    }

    /**
     * 通过反射获取对象的公开属性值
     *
     * @param fieldName
     * @param o
     * @return
     */
    public static Object getPublicFieldValueByName(String fieldName, Object o) {
        if (o == null || fieldName == null) {
            return null;
        }
        Class c = (Class) o.getClass();
        Field[] fs = c.getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true);
            if (f.getName().equalsIgnoreCase(fieldName)) {
                try {
                    return f.get(o);
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

}