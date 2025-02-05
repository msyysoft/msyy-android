package g.api.event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class GSubscriber extends GSubscriberSuper {

    public GSubscriber(String tag) {
        super(tag);
    }

    public GSubscriber(String tag, boolean recycleByOther) {
        super(tag, recycleByOther);
    }

    @Subscribe
    final public void onEvent(GEvent event) {
        if (onCheck(event)) {
            switch (event.getStatus()) {
                case WAIT:
                    onWait(event);
                    break;
                case SUCC:
                    onSucc(event);
                    EventBus.getDefault().unregister(this);
                    break;
                case FAIL:
                    onFail(event);
                    EventBus.getDefault().unregister(this);
                    break;
            }
        }
    }
}
