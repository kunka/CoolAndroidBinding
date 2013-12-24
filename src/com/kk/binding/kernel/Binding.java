/**
 *
 */
package com.kk.binding.kernel;

import com.kk.binding.util.BindDesignLog;
import com.kk.binding.util.StringUtil;
import com.kk.binding.view.ViewFactory;

/**
 * @author xuanjue.hk
 * @date 2013-2-25
 */
public class Binding {
    private static final String TAG = "Binding";
    private String path;
    private DependencyProperty dp;
    private DependencyObject dpo;
    private Object dataContext;
    private IValueConverter valueConverter;

    public String getPath() {
        return path;
    }

    public void setPath(String propertyName) {
        this.path = propertyName;
    }

    /**
     * @return the valueConverter
     */
    public IValueConverter getValueConverter() {
        return valueConverter;
    }

    /**
     * @param valueConverter the valueConverter to set
     */
    public void setValueConverter(IValueConverter valueConverter) {
        this.valueConverter = valueConverter;
    }

    public Object getDataContext() {
        return dataContext;
    }

    public void setDependencyProperty(DependencyProperty dp) {
        this.dp = dp;
    }

    public void setDependencyObject(DependencyObject dpo) {
        this.dpo = dpo;
    }

    public DependencyProperty getDependencyProperty() {
        return dp;
    }

    public DependencyObject getDependencyObject() {
        return dpo;
    }

    public void setDataContext(Object dataContext) {
        if (this.dataContext != dataContext) {
            BindDesignLog.d(TAG, "OnBindDataContextChanged:\n"
                    + "\n propertyName = " + dp.getPropertyName()
                    + "\n path = " + path
                    + "\n oldDataContext = " + this.dataContext
                    + "\n newDataContext = " + dataContext);

            // unregister old
            if (this.dataContext instanceof INotifyPropertyChanged) {
                ((INotifyPropertyChanged) this.dataContext).setPropertyChangedListener(null);
            }

            this.dataContext = dataContext;
            // register new
            if (this.dataContext instanceof INotifyPropertyChanged) {
                ((INotifyPropertyChanged) this.dataContext).setPropertyChangedListener(new IPropertyChanged() {

                    @Override
                    public void propertyChanged(Object sender, PropertyChangedEventArgs args) {
                        if (StringUtil.compare(path, args.getName())) {
                            updateValue();
                        }
                    }
                });
            }

            updateValue();
        }
    }

    private void updateValue() {
        if (dp == null || dpo == null)
            return;

        if (!ViewFactory.BINDING_DATA_CONTEXT.equals(dp.getPropertyName())) {
            BindDesignLog.d(TAG, "updateValue: target = \n"
                    + "\n propertyName = " + dp.getPropertyName()
                    + "\n path = " + path
                    + "\n dataContext = " + dataContext);

            Object value = DependencyObject.parseBindValue(dataContext, path);
            value = DependencyObject.converterValue(value, valueConverter);
            dpo.setValue(dp, value, path);
        }
    }
}
