package binding.command;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import binding.kernel.BindEngine;
import binding.kernel.ICommand;
import binding.util.BindDesignLog;

/**
 * Created by hk on 13-12-13.
 */
public class UrlNavCommand implements ICommand {
    @Override
    public void execute(View view, Object... args) {
        if (args.length > 0 && args[0] instanceof String) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse((String) args[0]));
            if (BindEngine.instance().getContext() != null) {
                try {
                    BindEngine.instance().getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    BindDesignLog.d("UrlNavCommand", e.toString());
                }
            }
        }
    }
}
