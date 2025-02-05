
package g.api.http;

import android.content.Context;

import java.util.Map;

public abstract class GRequestCallBack {
    protected Context context;
    //记录本次请求的数据
    protected GRequestData requestData;
    //onStart只能用一次
    public boolean startUseFlag = false;

    public GRequestCallBack(Context context) {
        this.context = context;
    }

    public void onStart() {
        if (!isStartUse()) {
            setStartUse(true);
        }
    }

    public abstract void onLoading(long total, long current, boolean isUploading);

    public abstract void onSuccess(String result);

    public void onResponseHeader(Map<String, String> headers) {
    }

    public abstract void onFailure(Exception e);

    public Context getContext() {
        return context;
    }

    public void setRequestData(GRequestData requestData) {
        this.requestData = requestData;
    }

    public GRequestData getRequestData() {
        return requestData;
    }

    public void setStartUse(boolean startUse) {
        this.startUseFlag = startUse;
    }

    public boolean isStartUse() {
        return startUseFlag;
    }
}
