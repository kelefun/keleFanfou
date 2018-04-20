package com.funstill.lib.image.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funstill.lib.R;
import com.funstill.lib.image.adapter.ImageFolderAdapter;
import com.funstill.lib.image.adapter.ImageListAdapter;
import com.funstill.lib.image.config.IHandlerCallBack;
import com.funstill.lib.image.config.ImageSelector;
import com.funstill.lib.image.config.SelectorConfig;
import com.funstill.lib.image.model.LocalMedia;
import com.funstill.lib.image.model.LocalMediaFolder;
import com.funstill.lib.image.utils.FileUtils;
import com.funstill.lib.image.utils.GridSpacingItemDecoration;
import com.funstill.lib.image.utils.LocalMediaLoader;
import com.funstill.lib.image.utils.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageSelectorActivity extends AppCompatActivity {
    public final static int REQUEST_CAMERA = 67;

    private int maxSelectNum = 9;
    private boolean showCamera = true;
    private boolean enablePreview = true;
//    private boolean enableCrop = false;

    private int spanCount = 3;

    private Toolbar toolbar;
    private TextView doneText;

    private TextView previewText;

    private RecyclerView recyclerView;
    private ImageListAdapter imageAdapter;

    private LinearLayout folderLayout;
    private TextView folderName;
    private FolderWindow folderWindow;

    private String cameraPath;
//-----------------------

    private SelectorConfig selectorConfig;
    private IHandlerCallBack mHandlerCallBack;
    private ArrayList<String> resultPhoto=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageselector);

        selectorConfig = ImageSelector.getInstance().getSelectorConfig();
        if (selectorConfig == null) {
            exit();
            return;
        }

        maxSelectNum = selectorConfig.getMaxSize();
        showCamera = selectorConfig.showCamera();
        enablePreview = true;// TODO: 2017/4/28
        cameraPath = selectorConfig.getFilePath();

        if (selectorConfig.isMultiSelect()) {
//            enableCrop = false;
        } else {
            enablePreview = false;
        }

        initView();
        registerListener();

        new LocalMediaLoader(this, LocalMediaLoader.TYPE_IMAGE).loadImage(new LocalMediaLoader.LocalMediaLoadListener() {

            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                folderWindow.bindFolder(folders);
                imageAdapter.bindImages(folders.get(0).getImageList());
            }
        });
    }

    public void initView() {
        folderWindow = new FolderWindow(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.picture);
//        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);

        doneText = (TextView) findViewById(R.id.done_text);
        doneText.setVisibility(selectorConfig.isMultiSelect() ? View.VISIBLE : View.GONE);

        previewText = (TextView) findViewById(R.id.preview_text);
        previewText.setVisibility(enablePreview ? View.VISIBLE : View.GONE);

        folderLayout = (LinearLayout) findViewById(R.id.folder_layout);
        folderName = (TextView) findViewById(R.id.folder_name);

        recyclerView = (RecyclerView) findViewById(R.id.folder_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, ScreenUtils.dip2px(this, 2), false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

        imageAdapter = new ImageListAdapter(this, maxSelectNum, selectorConfig.isMultiSelect(), showCamera,enablePreview);
        recyclerView.setAdapter(imageAdapter);
//-------------
        mHandlerCallBack = selectorConfig.getIHandlerCallBack();
        mHandlerCallBack.onStart();
    }

    public void registerListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        folderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderWindow.isShowing()) {
                    folderWindow.dismiss();
                } else {
                    folderWindow.showAsDropDown(toolbar);
                }
            }
        });
        imageAdapter.setOnImageSelectChangedListener(new ImageListAdapter.OnImageSelectChangedListener() {
            @Override
            public void onChange(List<LocalMedia> selectImages) {
                boolean enable = selectImages.size() != 0;
                doneText.setEnabled(enable ? true : false);
                previewText.setEnabled(enable ? true : false);
                if (enable) {
                    doneText.setText(getString(R.string.done_num, selectImages.size(), maxSelectNum));
                    previewText.setText(getString(R.string.preview_num, selectImages.size()));
                } else {
                    doneText.setText(R.string.done);
                    previewText.setText(R.string.preview);
                }
            }

            @Override
            public void onTakePhoto() {
                startCamera();
            }

            @Override
            public void onPictureClick(LocalMedia media, int position) {
                if (enablePreview) {
                    startPreview(imageAdapter.getImages(), position);
                } else {
                    onSelectDone(media.getPath());
                }
            }
        });
        doneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectDone(imageAdapter.getSelectedImages());
            }
        });
        folderWindow.setOnItemClickListener(new ImageFolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, List<LocalMedia> images) {
                folderWindow.dismiss();
                imageAdapter.bindImages(images);
                folderName.setText(name);
            }
        });
        previewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreview(imageAdapter.getSelectedImages(), 0);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // on take photo success
            if (requestCode == REQUEST_CAMERA) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(cameraPath))));
                onSelectDone(cameraPath);
            }
            //on preview select change
            else if (requestCode == ImagePreviewActivity.REQUEST_PREVIEW) {
                boolean isDone = data.getBooleanExtra(ImagePreviewActivity.OUTPUT_ISDONE, false);
                List<LocalMedia> images = (List<LocalMedia>) data.getSerializableExtra(ImagePreviewActivity.OUTPUT_LIST);
                if (isDone) {
                    onSelectDone(images);
                }else{
                    imageAdapter.bindSelectImages(images);
                }
            }
        }
    }


    /**
     * start to camera、preview、crop
     */
    public void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = FileUtils.createCameraFile(this,selectorConfig.getFilePath());
            cameraPath = cameraFile.getAbsolutePath();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(this,"Has No Camera!", Toast.LENGTH_SHORT).show();
            selectorConfig.getIHandlerCallBack().onError();
        }

    }

    public void startPreview(List<LocalMedia> previewImages, int position) {
        ImagePreviewActivity.startPreview(this, previewImages, imageAdapter.getSelectedImages(), maxSelectNum, position);
    }

    /**
     * on select done
     *
     * @param medias
     */
    public void onSelectDone(List<LocalMedia> medias) {
        for (LocalMedia media : medias) {
            resultPhoto.add(media.getPath());
        }
        mHandlerCallBack.onSuccess(resultPhoto);
        exit();
    }

    public void onSelectDone(String path) {
        resultPhoto.add(path);
        mHandlerCallBack.onSuccess(resultPhoto);
        exit();
    }


    private void exit() {
        if (mHandlerCallBack != null) {
            mHandlerCallBack.onFinish();
        }
        finish();
    }
}
