package g.api.views.listviewsidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ListViewSideBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    private String[] b = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private int maxIndexNum = 26;
    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private float textSizeDP = 10;

    private int height;// 获取对应高度
    private int width; // 获取对应宽度

    private int selectColor = Color.BLUE;
    private int unSelectColor = Color.BLACK;

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public void setUnSelectColor(int unSelectColor) {
        this.unSelectColor = unSelectColor;
    }

    public void setIndex(String[] b) {
        if (b != null) {
            this.b = b;
            invalidate();
        }
    }

    public void setMaxIndexNum(int maxIndexNum) {
        this.maxIndexNum = maxIndexNum;
        invalidate();
    }

    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }


    public ListViewSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ListViewSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewSideBar(Context context) {
        super(context);
    }

    /**
     * 尺寸变化
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int singleHeight = height / maxIndexNum;// 获取每一个字母的高度

        for (int i = 0; i < b.length; i++) {
            paint.setColor(unSelectColor);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(dip2px(getContext(), textSizeDP));
            paint.setAntiAlias(true);
            // 选中的状态
            if (i == choose) {
                paint.setColor(selectColor);
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * (i + (maxIndexNum - b.length) / 2f) + singleHeight * 2 / 3;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        int singleHeight = getHeight() / maxIndexNum;// 获取每一个字母的高度
        final int c = (int) (y / singleHeight + 1 - (maxIndexNum - b.length) / 2f);

        switch (action) {
            case MotionEvent.ACTION_UP:
                //setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                //setBackgroundDrawable(new ColorDrawable(0x10000000));
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

    public void setTextSizeDP(float textSizeDP) {
        this.textSizeDP = textSizeDP;
    }

    private static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}