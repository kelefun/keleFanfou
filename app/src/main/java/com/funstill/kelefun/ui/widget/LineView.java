package com.funstill.kelefun.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funstill.kelefun.R;

public class LineView extends LinearLayout {
    private View dividerTop, dividerBottom;
    private LinearLayout llRoot;
    private ImageView ivLeftIcon;
    private TextView tvTextContent;
    private ImageView ivRightIcon;

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, AttributeSet attrs) {//该构造函数使其可以在XML布局中使用
        super(context, attrs);
    }

    /**
     * 初始化各个控件
     */
    public LineView init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_one_line, this, true);
        llRoot = (LinearLayout) findViewById(R.id.ll_root);
        dividerTop = findViewById(R.id.divider_top);
        dividerBottom = findViewById(R.id.divider_bottom);
        ivLeftIcon = (ImageView) findViewById(R.id.iv_left_icon);
        tvTextContent = (TextView) findViewById(R.id.tv_text_content);
        ivRightIcon = (ImageView) findViewById(R.id.iv_right_icon);
        return this;
    }

    /**
     * 我的页面每一行  icon + 文字 + 右箭头（显示/不显示） + 右箭头左边的文字（显示/不显示）+ 下分割线
     *
     * @param iconRes     icon图片
     * @param textContent 文字内容
     */
    public LineView init(int iconRes, String textContent, boolean showArrow) {
        init();
        showDivider(false, true);
        setLeftIcon(iconRes);
        setTextContent(textContent);
        showArrow(showArrow);
        return this;
    }
    /**
     * 整个一行被点击
     */
    public static interface OnLineClickListener {
        void onLineClick(View view);
    }
    public LineView setOnRootClickListener(final OnLineClickListener onRootClickListener, final String lineTag) {
        llRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llRoot.setTag(lineTag);
                onRootClickListener.onLineClick(llRoot);
            }
        });
        return this;
    }
    /**
     * 设置上下分割线的显示情况
     *
     * @return
     */
    public LineView showDivider(Boolean showDividerTop, Boolean showDivderBottom) {
        if (showDividerTop) {
            dividerTop.setVisibility(VISIBLE);
        } else {
            dividerTop.setVisibility(GONE);
        }
        if (showDivderBottom) {
            dividerBottom.setVisibility(VISIBLE);
        } else {
            dividerBottom.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 设置上分割线的颜色
     *
     * @return
     */
    public LineView setDividerTopColor(int dividerTopColorRes) {
        dividerTop.setBackgroundColor(getResources().getColor(dividerTopColorRes));
        return this;
    }


    /**
     * 设置下分割线的颜色
     *
     * @return
     */
    public LineView setDividerBottomColor(int dividerBottomColorRes) {
        dividerBottom.setBackgroundColor(getResources().getColor(dividerBottomColorRes));
        return this;
    }

    /**
     * 设置左边Icon
     *
     * @param iconRes
     */
    public LineView setLeftIcon(int iconRes) {
        ivLeftIcon.setImageResource(iconRes);
        return this;
    }

    /**
     * 设置左边Icon显示与否
     *
     * @param showLeftIcon
     */
    public LineView showLeftIcon(boolean showLeftIcon) {
        if (showLeftIcon) {
            ivLeftIcon.setVisibility(VISIBLE);
        } else {
            ivLeftIcon.setVisibility(GONE);
        }
        return this;
    }



    /**
     * 设置中间的文字内容
     *
     * @param textContent
     * @return
     */
    public LineView setTextContent(String textContent) {
        tvTextContent.setText(textContent);
        return this;
    }

    /**
     * 设置中间的文字颜色
     *
     * @return
     */
    public LineView setTextContentColor(int colorRes) {
        tvTextContent.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置中间的文字大小
     *
     * @return
     */
    public LineView setTextContentSize(int textSizeSp) {
        tvTextContent.setTextSize(textSizeSp);
        return this;
    }


    /**
     * 设置右箭头的显示与不显示
     *
     * @param showArrow
     */
    public LineView showArrow(boolean showArrow) {
        if (showArrow) {
            ivRightIcon.setVisibility(VISIBLE);
        } else {
            ivRightIcon.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 获取右边icon
     */
    public LineView setIvRightIcon(int iconRes) {

        ivRightIcon.setImageResource(iconRes);

        return this;
    }

    /**
     * 获取右边icon
     */
    public ImageView getIvRightIcon() {

        return ivRightIcon;
    }



}
