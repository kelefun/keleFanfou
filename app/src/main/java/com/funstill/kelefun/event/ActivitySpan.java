package com.funstill.kelefun.event;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

/**
 * span点击跳转至activity
 * (比如应用于类似微博的@用户 点击跳转至用户主页)
 *
 * @author liukaiyang
 * @since 2017/11/6 14:46
 */

@SuppressLint("ParcelCreator")
public class ActivitySpan extends URLSpan {

    public ActivitySpan(String url) {
        super(url);
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}
