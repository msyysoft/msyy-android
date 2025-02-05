package g.api.tools;

import android.view.MotionEvent;

public class TouchEventUtil {
    public interface TouchEventIFace {
        void isShouldMove(boolean isShouldMove);
    }

    private TouchEventIFace touchEventIFace;
    private float d_x = 0f;
    private float d_y = 0f;
    private double m_rate = 1.2;//手势习惯比例
    private boolean should_move = false;
    private boolean isOk = false;

    public void setRate(double rate) {
        if (rate > 0) {
            this.m_rate = rate;
        }
    }

    public void setTouchEventIFace(TouchEventIFace touchEventIFace) {
        this.touchEventIFace = touchEventIFace;
    }

    public boolean isShouldMove() {
        return should_move;
    }

    public void dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                d_x = event.getX();
                d_y = event.getY();
                should_move = true;
                isOk = false;
                if (touchEventIFace != null) {
                    touchEventIFace.isShouldMove(should_move);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOk) {
                    float m_x = event.getX();
                    float m_y = event.getY();
                    float offset_x = Math.abs(d_x - m_x);
                    float offset_y = Math.abs(d_y - m_y);
                    if (Math.max(offset_x, offset_y) > 10) {
                        isOk = true;
                        if (offset_x * m_rate >= offset_y) {
                            should_move = true;
                        } else {
                            should_move = false;
                        }
                        if (touchEventIFace != null) {
                            touchEventIFace.isShouldMove(should_move);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                should_move = false;
                isOk = false;
                if (touchEventIFace != null) {
                    touchEventIFace.isShouldMove(should_move);
                }
                break;
            default:
                break;
        }
    }
}
