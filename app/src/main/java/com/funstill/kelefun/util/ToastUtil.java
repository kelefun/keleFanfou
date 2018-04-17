package com.funstill.kelefun.util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

    private static Handler mHandler = null;

    private static Toast toast = null;


    public static void showToast(final Context mContext, final String text) {
        sharedHandler(mContext).post(() -> {
            if (toast != null) {
                toast.setText(text);
                toast.setDuration(Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(mContext.getApplicationContext(), text, Toast.LENGTH_SHORT);
            }
            toast.setGravity(Gravity.TOP , 0, 220);
            toast.show();
        });
    }

    private static Handler sharedHandler(Context context) {
        if (mHandler == null) {
            mHandler = new Handler(context.getMainLooper());
        }
        return mHandler;
    }

}
