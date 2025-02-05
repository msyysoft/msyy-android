package g.api.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GJsonConverter<T> implements GConverter<T> {
    private GRequestCallBack tHolder;

    public GJsonConverter(GRequestCallBack tHolder) {
        this.tHolder = tHolder;
    }

    public GRequestCallBack getHolder() {
        return tHolder;
    }

    @Override
    public T convert(Class<T> tClass, String str) {
        if (tClass == null && tHolder != null)
            tClass = (Class<T>) GHttpUtils.getTClass(tHolder, 0);
        Gson gson = new GsonBuilder()
                //导出值为NULL的对象
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        T t = gson.fromJson(str.replaceAll("\"null\"", "null"), tClass);
        return t;
    }
}
