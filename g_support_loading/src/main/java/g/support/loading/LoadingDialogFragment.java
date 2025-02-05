package g.support.loading;

import android.app.Dialog;
import android.os.Bundle;


public class LoadingDialogFragment extends GDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        boolean isUseMessage = false;
        String message = null;
        if (args != null) {
            if (args.containsKey("isUseMessage"))
                isUseMessage = args.getBoolean("isUseMessage");
            if (args.containsKey("message"))
                message = args.getString("message");
        }
        return new LoadingDialog(getActivity(), isUseMessage, message);
    }

    public static LoadingDialogFragment createDialog() {
        return createDialog(false, null);
    }

    public static LoadingDialogFragment createDialog(String message) {
        return createDialog(true, message);
    }


    private static LoadingDialogFragment createDialog(boolean isUseMessage, String message) {
        LoadingDialogFragment mLoadingDialogFragment = new LoadingDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isUseMessage", isUseMessage);
        args.putString("message", message);
        mLoadingDialogFragment.setArguments(args);
        return mLoadingDialogFragment;
    }
}