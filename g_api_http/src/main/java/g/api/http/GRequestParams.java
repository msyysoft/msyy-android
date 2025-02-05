package g.api.http;

import java.util.HashMap;
import java.util.Map;

public class GRequestParams {
    private Map<String, String> headers;
    private Map<String, String> bodyParams;
    private String jsonBody;

    public void addHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        if (headers == null)
            headers = new HashMap<String, String>();
        return headers;
    }

    public Map<String, String> getBodyParams() {
        if (bodyParams == null)
            bodyParams = new HashMap<String, String>();
        return bodyParams;
    }

    public void addBodyParameter(String name, Object value) {
        if (bodyParams == null) {
            bodyParams = new HashMap<String, String>();
        }
        bodyParams.put(name, value == null ? null : value.toString());
    }

    public boolean isJsonParams() {
        return !GHttpUtils.isEmpty(jsonBody);
    }

    public void setJsonBody(String jsonBody) {
        if (bodyParams != null)
            bodyParams.clear();
        this.jsonBody = jsonBody;
    }

    public String getJsonBody() {
        if (jsonBody == null)
            return "{}";
        return jsonBody;
    }

}
