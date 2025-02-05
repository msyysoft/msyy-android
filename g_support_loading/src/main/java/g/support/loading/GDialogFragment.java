package g.support.loading;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class GDialogFragment extends DialogFragment {
    public void show(FragmentManager manager) {
        try {
            Fragment fragment = manager.findFragmentByTag(this.getClass().getName());
            if (fragment == null) {
                show(manager, this.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismiss(DialogFragment dialogFragment) {
        if (dialogFragment != null) {
            try {
                dialogFragment.dismiss();
                dialogFragment = null;
            } catch (Exception e) {
                dialogFragment.dismissAllowingStateLoss();
                dialogFragment = null;
                e.printStackTrace();
            }
        }
    }

    protected void setStyleTransparent() {
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    protected void setStyleFullWidth() {
        setStyleTransparent();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
    }

    protected void setStyleTop() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.TOP;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    protected void setStyleBottom() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }
}
