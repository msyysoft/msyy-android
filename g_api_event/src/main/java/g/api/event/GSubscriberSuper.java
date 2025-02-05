package g.api.event;

import org.greenrobot.eventbus.EventBus;


public abstract class GSubscriberSuper {
    private String tag;
    private boolean recycleByOther;

    public GSubscriberSuper(String tag) {
        this(tag, false);
    }

    public GSubscriberSuper(String tag, boolean recycleByOther) {
        this.tag = tag;
        this.recycleByOther = recycleByOther;
        EventBus.getDefault().register(this);
    }

    protected void onWait(GEvent event){}

    protected void onSucc(GEvent event){}

    protected void onFail(GEvent event){}

    protected boolean onCheck(GEvent event) {
        boolean result = false;
        if (tag != null && event.getTag() != null) {
            if (tag.equals(event.getTag()))
                result = true;
        }
        if (result == false && recycleByOther) {
            EventBus.getDefault().unregister(this);
        }
        return result;
    }

}
