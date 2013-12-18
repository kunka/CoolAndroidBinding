/**
 *
 */
package binding.kernel;

/**
 * @author xuanjue.hk
 * @date 2013-2-27
 */
public class CommandBinding {
    private String commandName;
    private String commandParamPath;
    private Object dataContext;

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

    public void setDataContext(Object dataContext) {
        this.dataContext = dataContext;
    }

    public String getCommandParamPath() {
        return commandParamPath;
    }

    public void setCommandParamPath(String commandParamPath) {
        this.commandParamPath = commandParamPath;
    }
}
