package com.funstill.kelefun.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.funstill.kelefun.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * @author liukaiyang
 * @since 2017/4/28 18:46
 */

public class ImagePreview extends Activity {
    public static final String EXTRA_IMAGE_URL = "image_url";
    private ImageView mImageView;
    private View view;
    private String url;

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
        view = findViewById(R.id.activityStatusImagePreview);
        String mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        url = mImageUrl;
        if (mImageUrl.endsWith("gif")) {
            Glide.with(this)
                    .load(mImageUrl)
                    .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.drawable.tab_item_bg)
                    .into(mImageView);
        } else {
            Glide.with(this)
                    .load(mImageUrl)
//                    .placeholder(R.drawable.tab_item_bg)
                    .into(mImageView);
        }
        view.setOnLongClickListener(v -> {
            new AlertDialog.Builder(ImagePreview.this)
                    .setMessage("保存图片")
                    .setNegativeButton(android.R.string.cancel,
                            (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(android.R.string.ok,
                            (dialog, which) -> {
                                //TODO 异步保存图片
                                new SaveImageTask().execute();
                                dialog.dismiss();
                            })
                    .show();
            return true;
        });
        view.setOnClickListener(v -> this.finish());
    }

    public class SaveImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                bitmap = Glide.with(ImagePreview.this).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            File appDir = new File(Environment.getExternalStorageDirectory(), "Kelefun");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date()) + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uri = Uri.fromFile(file);
            // 通知图库更新
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            ImagePreview.this.sendBroadcast(scannerIntent);
        }

    }
}
