package g.support.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import g.api.tools.T;
import g.support.app.alistener.ActivityFinishListener;
import g.support.app.alistener.ActivityOnBackPressedListener;

public class FragmentShellActivity extends AppCompatActivity {
    public static final String EXTRA_FRAGMENTNAME = "fragname";
    public static final String EXTRA_FRAGMENTARGS = "fragargs";

    private static String lastFragment;


    protected static boolean isWrongClick(String nowFragment) {
        if (nowFragment.equals(lastFragment)) {
            return !T.isOverWhenPressAgain(600);
        }
        lastFragment = nowFragment;
        return false;
    }

    public static Intent createIntent(Context context, Class<?> fragmentClass, Bundle fragmentArgs) {
        if (isWrongClick(fragmentClass.getName()) || context == null)
            return null;
        Intent retval = new Intent(context, FragmentShellActivity.class);
        retval.putExtra(EXTRA_FRAGMENTNAME, fragmentClass.getName());
        retval.putExtra(EXTRA_FRAGMENTARGS, fragmentArgs);

        return retval;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreateSuper(savedInstanceState);
        setContentView(getFrameShell());
        setup();
    }

    protected View getFrameShell() {
        return LayoutInflater.from(this).inflate(R.layout.activity_fragment_shell, null, false);
    }

    protected void setup() {
        Intent launchIntent = getIntent();
        final String fragclassname = launchIntent.getStringExtra(EXTRA_FRAGMENTNAME);
        final Bundle fragargs = launchIntent.getBundleExtra(EXTRA_FRAGMENTARGS);

        try {
            Class<?> fragmentClass = getClassLoader().loadClass(fragclassname);
            Fragment mBaseFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_shell_content);
            if (mBaseFragment == null) {
                mBaseFragment = (Fragment) fragmentClass.newInstance();
                mBaseFragment.setArguments(fragargs);
                FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                tr.add(R.id.fragment_shell_content, mBaseFragment);
                tr.commit();
            }
        } catch (Exception e) {
            Log.e(FragmentShellActivity.class.getName(), "", e);
            finish();
        }
    }

    public void onCreateSuper(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void pushFragmentToBackStack(Fragment f) {
        pushFragmentToBackStack(f, true);
    }

    public void pushFragmentToBackStack(Fragment f, boolean isAddToBackStack) {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.fragment_shell_content, f, f.getClass().getName());
        if (isAddToBackStack)
            tr.addToBackStack(f.getClass().getName());
        tr.commit();
    }

    private ActivityFinishListener activityFinishListener;
    private ActivityOnBackPressedListener activityOnBackPressedListener;

    public void setActivityFinishListener(ActivityFinishListener shellActivityListener) {
        this.activityFinishListener = shellActivityListener;
    }

    public void setActivityOnBackPressedListener(ActivityOnBackPressedListener activityOnBackPressedListener) {
        this.activityOnBackPressedListener = activityOnBackPressedListener;
    }

    @Override
    public void finish() {
        if (activityFinishListener != null)
            activityFinishListener.whenActivityBeforeFinish(this);
        super.finish();
        if (activityFinishListener != null)
            activityFinishListener.whenActivityAfterFinish(this);
    }

    @Override
    public void onBackPressed() {
        if (activityOnBackPressedListener != null && activityOnBackPressedListener.whenActivityOnBackPressed(this))
            return;
        super.onBackPressed();
    }
}