package com.funstill.kelefun.event;

import android.text.Layout;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * SpannableString 和 ClickableSpan处理TextView类似微博中@用户,#话题#等跳转时
 * 使用 textview.setMovementMethod(LinkMovementMethod.getInstance())时,导致TextView 对点击事件做了拦截，
 * 而原本在 RecyclerView 中 item 自己的点击事件却失效了。
 * <p>
 * 所以写此方法修正
 * 调用方式 statusView.setOnTouchListener(TextLinkMovementMethod.getInstance());
 *
 * @author liukaiyang
 * @date 2018/4/20 10:49
 * @see <a href="https://stackoverflow.com/questions/8558732/listview-textview-with-linkmovementmethod-makes-list-item-unclickable>unclickable</a>
 * @see <a href="https://www.jianshu.com/p/68b12336d6e0">点击处理 博客</a>
 */
public class TextLinkMovementMethod implements View.OnTouchListener {

    public static TextLinkMovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new TextLinkMovementMethod();

        return sInstance;
    }

    private static TextLinkMovementMethod sInstance;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = false;
        CharSequence text = ((TextView) v).getText();
        Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
        TextView widget = (TextView) v;
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                }
                ret = true;
            }
        }
        return ret;
    }
}
