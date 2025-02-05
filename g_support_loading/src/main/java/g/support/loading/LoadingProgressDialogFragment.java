package g.support.loading;

import android.app.Dialog;
import android.os.Bundle;


public class LoadingProgressDialogFragment extends GDialogFragment {
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

    public static LoadingProgressDialogFragment createDialog() {
        return createDialog(false, null);
    }

    public static LoadingProgressDialogFragment createDialog(String message) {
        return createDialog(true, message);
    }


    private static LoadingProgressDialogFragment createDialog(boolean isUseMessage, String message) {
        LoadingProgressDialogFragment mLoadingDialogFragment = new LoadingProgressDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isUseMessage", isUseMessage);
        args.putString("message", message);
        mLoadingDialogFragment.setArguments(args);
        return mLoadingDialogFragment;
    }


}