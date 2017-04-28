package com.funstill.kelefun.widget;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * @author liukaiyang
 * @since 2017/4/28 18:46
 */

public class ImagePreview extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URL = "image_url";
//    public static final String EXTRA_IMAGE_TITLE = "image_title";
    private Intent startPreview(Activity context,String file) {
        Intent intent = new Intent(context, ImagePreview.class);
        intent.putExtra(EXTRA_IMAGE_URL, file);
        return intent;
    }
}
