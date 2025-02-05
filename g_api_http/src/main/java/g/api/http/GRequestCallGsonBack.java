package g.api.http;


import android.content.Context;

import java.io.Serializable;

/**
 * 基于Gson解析的用户回调
 */
public abstract class GRequestCallGsonBack<T> extends GRequestCallBack implements Serializable {

    protected GConverter<T> converter;

    public GRequestCallGsonBack(Context context) {
        super(context);
        converter = generateConverter();
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {
    }

    @Override
    public void onSuccess(String result) {
        try {
            onSuccess(onConvert(result));
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
        if (context != null && !GHttpUtils.isNetworkOK(context))
            onFailure("网络异常");
        else {
            onFailure((String) null);
        }
    }

    public void onError(Exception e) {
        e.printStackTrace();
        GHttpLog.getInstance().LogHttpError(e);
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(String info);

    public T onConvert(String result) {
        return converter.convert(null, result);
    }

    public GConverter<T> generateConverter() {
        return new GJsonConverter<T>(this);
    }
}
