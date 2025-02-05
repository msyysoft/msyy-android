package g.api.event;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

public final class GEvent implements Serializable {
    private GEventStatus status;//状态
    private String tag;//tag
    private String pag;//private tag
    private Object obj;//附带对象


    /**
     * @deprecated Use {@link #GEvent(Context, String)} instead.
     */
    @Deprecated
    public GEvent(String tag) {
        this.tag = tag;
    }

    public GEvent(Context ctx, String tag) {
        this.tag = tag;
        this.pag = localizeTag(ctx, tag);
    }

    /**
     * @deprecated Use {@link #getPag()} instead.
     */
    @Deprecated
    public String getTag() {
        return tag;
    }

    public String getPag() {
        return pag;
    }

    public GEventStatus getStatus() {
        return status;
    }

    public void setStatus(GEventStatus status) {
        this.status = status;
    }

    public void post() {
        EventBus.getDefault().post(this);
    }

    public void postWithType(GEventStatus status) {
        setStatus(status);
        post();
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return obj;
    }

    public boolean checkTag(Context ctx, String tag) {
        String a = localizeTag(ctx, tag);
        String b = pag;
        if (a == null && b == null)
            return true;
        if (a != null && b != null) {
            return a.equals(b);
        }
        return false;
    }

    public static String localizeTag(Context c, String tag) {
        if (c != null)
            return c.getPackageName() + "#" + tag;
        return tag;
    }
}
