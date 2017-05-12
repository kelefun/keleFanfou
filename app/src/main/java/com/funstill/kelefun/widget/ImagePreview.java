package com.funstill.kelefun.widget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.funstill.kelefun.R;
import com.funstill.kelefun.util.ToastUtil;
import com.funstill.library.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.black));
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

    public class SaveImageTask extends AsyncTask<Void, Void, File> {

        @Override
        protected File doInBackground(Void... params) {
            File sourceFile = null;
            try {
                sourceFile = Glide.with(ImagePreview.this).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return sourceFile;
        }

        @Override
        protected void onPostExecute(final File sourceFile) {
            if(sourceFile!=null){
                File targetFile=FileUtils.createCameraFile(ImagePreview.this, "KeleFun");
                //copy file
                copyFile(sourceFile,targetFile);
                Uri uri = Uri.fromFile(targetFile);
                // 通知图库更新
                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                ImagePreview.this.sendBroadcast(scannerIntent);
                ToastUtil.showToast(getApplicationContext(),"图片已保存成功");
            }else {
                ToastUtil.showToast(getApplicationContext(),"图片保存失败");
            }
        }

    }
    public void copyFile(File sourceFile, File targetFile) {
        try {
                InputStream is = new FileInputStream(sourceFile); //读入原文件
                FileOutputStream os = new FileOutputStream(targetFile);
                byte[] buffer = new byte[1024];
                int length;
                while ( (length = is.read(buffer)) != -1) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {

        }

    }
}
