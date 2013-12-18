/**
 *
 */
package binding.listener;

import android.view.View;

import binding.kernel.ICommand;
import binding.util.BindDesignLog;

/**
 * @author xuanjue.hk
 * @date 2013-2-28
 */
public abstract class ListenerToCommand {
    private ICommand mCommand;
    private Object param;

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public ICommand getCommand() {
        return mCommand;
    }

    public void setCommand(ICommand command) {
        mCommand = command;
    }

    public void executeCommand(View view, Object... args) {
        if (mCommand != null)
            try {
                mCommand.execute(view, args);
            } catch (Exception e) {
                BindDesignLog.d("ListenerToCommand", "executeCommand failed " + e.toString());
            }
    }

    public abstract void registerToView(View v);
}
