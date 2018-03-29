package com.funstill.kelefun.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class DynamicImageView extends AppCompatImageView {
private  int width,height;
    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取加载的图像
        Drawable drawable = getDrawable();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if(drawable!=null){
             width = MeasureSpec.getSize(widthMeasureSpec);
             height = (int) Math.ceil((float) width * (float) drawable.getIntrinsicHeight() / (float) drawable.getIntrinsicWidth());

            //如果图片高度大于屏幕的3/5,则截取图片
            int maxHeight= (int) (dm.heightPixels*(0.6));
            if(height>maxHeight){
                height=maxHeight;
            }
            setMeasuredDimension(width, height);
        }else {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }
}
