/**
 *
 */
package com.kk.binding.kernel;

import com.kk.binding.listener.ListenerToCommand;
import com.kk.binding.util.BindDesignLog;
import com.kk.binding.util.StringUtil;

/**
 * @author xuanjue.hk
 * @date 2013-2-27
 */
public class CommandBinding {
    private static final String TAG = "CommandBinding";
    private String commandName;
    private String commandParamPath;
    private Object dataContext;
    private ListenerToCommand ltc = null;

    /**
     * @param commandName the commandName to set
     */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    /**
     * @return the commandName
     */
    public String getCommandName() {
        return commandName;
    }

    public Object getDataContext() {
        return dataContext;
    }

    public String getCommandParamPath() {
        return commandParamPath;
    }

    public void setCommandParamPath(String commandParamPath) {
        this.commandParamPath = commandParamPath;
    }

    public void setListenerToCommand(ListenerToCommand ltc) {
        this.ltc = ltc;
    }

    public void setDataContext(Object dataContext) {
        if (this.dataContext != dataContext) {
            BindDesignLog.d(TAG, "OnCmdBindDataContextChanged:\n"
                    + "\n command = " + (ltc != null ? ltc.getCommand() : "null")
                    + "\n param = " + (ltc != null ? ltc.getParam() : "null")
                    + "\n cmdParamPath = " + commandParamPath
                    + "\n cmdName = " + commandName
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
                        if (StringUtil.compare(commandName, args.getName())) {
                            updateValue();
                        }
                    }
                });
            }
            updateValue();
        }
    }

    public void updateValue() {
        if (ltc != null) {
            Object param = DependencyObject.parseBindValue(dataContext, commandParamPath);
            ltc.setParam(param);
            BindDesignLog.d(TAG, "updateValue: paramValue = " + param);

            // custom command
            if (ltc.getCommand() == null) {
                Object value = DependencyObject.parseBindValue(dataContext, commandName);
                BindDesignLog.d(TAG, "updateValue: command = " + (value != null ? value.toString() : null));
                ltc.setCommand((ICommand) value);
            }
        }
    }
}

