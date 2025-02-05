package g.api.http;

public interface GConverter<T> {
    T convert(Class<T> tClass, String str);
}
