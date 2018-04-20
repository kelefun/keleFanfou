package com.funstill.kelefun.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.view.Gravity;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.funstill.kelefun.R;
import com.funstill.kelefun.util.ToastUtil;
import com.funstill.lib.image.utils.FileUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * @author liukaiyang
 * @since 2017/4/28 18:46
 */

public class ImagePreview extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URL = "image_url";
    private PhotoView mPhotoView;
    private ImageView mImageViewSave;
    private AppBarLayout mAppBar;
    private Toolbar mToolbar;
    private boolean mIsToolbarHidden = false;
    private String url;

    public static void startPreview(Context context, String imageUrl) {
        Intent intent = new Intent(context, ImagePreview.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_image_preview);
        initView();
        String mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        url = mImageUrl;
        if (mImageUrl.endsWith("gif")) {
            Glide.with(this)
                    .load(mImageUrl)
                    .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.drawable.tab_item_bg)
                    .into(mPhotoView);
        } else {
            Glide.with(this)
                    .load(mImageUrl)
//                    .placeholder(R.drawable.tab_item_bg)
                    .into(mPhotoView);
        }
        mPhotoView.setOnClickListener(v -> {
            hideOrShowToolbar();
        });
        mImageViewSave.setOnClickListener(v -> {
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
        });
    }

    private void initView() {
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.black));
        mAppBar = (AppBarLayout) findViewById(R.id.toolbar_appbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(R.string.image_preview);
//       mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationIcon(R.drawable.ic_action_close);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mToolbar.setBackgroundColor(getResources().getColor(R.color.black));
        mImageViewSave = new ImageView(this);
        //layoutParams 一定要引入imageView的父组件的params(比如mToolbar.addView(mImageViewSave)需引入ToolBar.LayoutParams)
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        lp.setMargins(0, 0, 160, 0);
        mImageViewSave.setLayoutParams(lp);
        mImageViewSave.setImageResource(R.drawable.ic_action_download_white);
//        mImageViewSave.setForegroundGravity(Gravity.END);
        mToolbar.addView(mImageViewSave);
        mPhotoView = (PhotoView) findViewById(R.id.statusImagePreview);
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
            if (sourceFile != null) {
                File targetFile = FileUtils.createCameraFile(ImagePreview.this, "KeleFun");
                //copy file
                copyFile(sourceFile, targetFile);
                Uri uri = Uri.fromFile(targetFile);
                // 通知图库更新
                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                ImagePreview.this.sendBroadcast(scannerIntent);
                ToastUtil.showToast(getApplicationContext(), "图片已保存成功");
            } else {
                ToastUtil.showToast(getApplicationContext(), "图片保存失败");
            }
        }

    }

    public void copyFile(File sourceFile, File targetFile) {
        try {
            InputStream is = new FileInputStream(sourceFile); //读入原文件
            FileOutputStream os = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
    protected void hideOrShowToolbar() {
        mAppBar.animate()
                .translationY(mIsToolbarHidden ? 0 : -mAppBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsToolbarHidden = !mIsToolbarHidden;
    }
}
