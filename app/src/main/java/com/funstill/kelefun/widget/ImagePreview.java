package com.funstill.kelefun.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;

/**
 * @author liukaiyang
 * @since 2017/4/28 18:46
 */

public class ImagePreview extends Activity {
    public static final String EXTRA_IMAGE_URL = "image_url";
    private ImageView mImageView;
    private View view;
    public static void startPreview(Activity context, String imageUrl) {
        Intent intent = new Intent(context, ImagePreview.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_image_preview);
        mImageView = (ImageView) findViewById(R.id.statusImagePreview);
        view =  findViewById(R.id.activityStatusImagePreview);
        String mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        Glide.with(this)
                .load(mImageUrl)
//                    .placeholder(R.drawable.tab_item_bg)
                .into(mImageView);
        view.setOnClickListener(v -> {
            this.finish();
        });
    }
}
