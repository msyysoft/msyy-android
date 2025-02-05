package g.api.tools.geasy;

import android.os.AsyncTask;
import android.os.SystemClock;

public abstract class GEasyTimeDelayAsyncTask<Result> extends AsyncTask<Void, Void, Result> {

    private int delayNum;
    private boolean run;

    public boolean isRun() {
        return run;
    }

    public void setDelayNum(int delayNum) {
        this.delayNum = delayNum;
    }

    @Override
    final protected void onPreExecute() {
        super.onPreExecute();
        run = true;
    }

    @Override
    final protected Result doInBackground(Void... params) {
        for (; delayNum > 0; delayNum--) {
            SystemClock.sleep(100);
        }
        run = false;
        return doInBackgroundSub();
    }

    protected abstract Result doInBackgroundSub();
}
