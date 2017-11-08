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

import com.funstill.kelefun.ui.other.SearchActivity;
import com.funstill.kelefun.ui.other.UserHomeActivity;

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
        Context context = widget.getContext();

        if (getURL().startsWith("http://fanfou.com/")) {//用户主页
            Intent intent = new Intent(context, UserHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.putExtra(UserHomeActivity.USER_ID, getURL().replace("http://fanfou.com/", ""));
            context.startActivity(intent);
        } else if ((getURL().startsWith("/q/"))) {//话题
            Intent intent = new Intent(context, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
        } else {
            Uri uri= Uri.parse(getURL());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e("URLSpan", "Actvity was not found for intent, " + intent.toString());
            }
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}
