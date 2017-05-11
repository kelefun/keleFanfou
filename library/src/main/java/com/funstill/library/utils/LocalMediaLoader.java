package com.funstill.library.utils;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.funstill.library.R;
import com.funstill.library.model.LocalMedia;
import com.funstill.library.model.LocalMediaFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 * update by kelefun 17/05/10
 */
public class LocalMediaLoader {
    // load type
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    private final static String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID};

//    private final static String[] VIDEO_PROJECTION = {
//            MediaStore.Video.Media.DATA,
//            MediaStore.Video.Media.DISPLAY_NAME,
//            MediaStore.Video.Media.DATE_ADDED,
//            MediaStore.Video.Media._ID,
//            MediaStore.Video.Media.DURATION};

    private int type = TYPE_IMAGE;
    private FragmentActivity activity;

    public LocalMediaLoader(FragmentActivity activity, int type) {
        this.activity = activity;
        this.type = type;
    }

    HashSet<String> mDirPaths = new HashSet<>();

    public void loadAllImage(final LocalMediaLoadListener imageLoadListener) {
        activity.getSupportLoaderManager().initLoader(type, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                if (id == TYPE_IMAGE) {
                   return new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");
                }
//                else if (id == TYPE_VIDEO) {
//                    cursorLoader = new CursorLoader( activity, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,VIDEO_PROJECTION, null, null, VIDEO_PROJECTION[2] + " DESC");
//                }
                return null;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null && data.getCount() > 0) {
                    ArrayList<LocalMediaFolder> imageFolderList = new ArrayList<>();

                    List<LocalMedia> allImages = new ArrayList<>();//所有图片

                    while (data.moveToNext()) {
                        // 获取图片的路径
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
//                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
//                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        boolean showFlag = size > 1024 * 10;//图片大于10KB

                        if (showFlag) {
                            File imageFile = new File(path);                  // 获取图片文件
                            File parentFile = imageFile.getParentFile();      // 获取图片上一级文件夹
                            String parentFilePath = parentFile.getAbsolutePath();
                            if (mDirPaths.contains(parentFilePath)) {
                                continue;
                            } else {
                                mDirPaths.add(parentFilePath);
                            }
                            LocalMediaFolder localMediaFolder = getImageFolder(imageFile, imageFolderList);
                            File[] files = parentFile.listFiles();//过滤video
                            ArrayList<LocalMedia> images = new ArrayList<>();
                            for (int i = files.length-1; i >=0; i--) {
                                File f = files[i];
                                LocalMedia localMedia = new LocalMedia(f.getAbsolutePath());
                                allImages.add(localMedia);
                                images.add(localMedia);
                            }
                            if (images.size() > 0) {
                                localMediaFolder.setImages(images);
                                localMediaFolder.setImageNum(localMediaFolder.getImages().size());
                                imageFolderList.add(localMediaFolder);
                            }
                        }
                    }//while结束
                    //TODO 改成最近图片
                    LocalMediaFolder imageFolder = new LocalMediaFolder();//所有图片文件夹
                    imageFolder.setImages(allImages);
                    imageFolder.setImageNum(allImages.size());
                    imageFolder.setFirstImagePath(allImages.get(0).getPath());
                    imageFolder.setName(activity.getString(R.string.all_image));
                    imageFolderList.add(0,imageFolder);
                    imageLoadListener.loadComplete(imageFolderList);
                    if (data != null) data.close();
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }


    private LocalMediaFolder getImageFolder(File imageFile, List<LocalMediaFolder> imageFolders) {
        File folderFile = imageFile.getParentFile();
//        Log.i("测试",imageFile.getPath()+"###"+imageFile.getAbsolutePath());
        for (LocalMediaFolder folder : imageFolders) {
            if (folder.getPath().equals(folderFile.getAbsolutePath())) {
                return folder;
            }
        }
        LocalMediaFolder newFolder = new LocalMediaFolder();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        newFolder.setFirstImagePath(imageFile.getPath());
        return newFolder;
    }

    public interface LocalMediaLoadListener {
        void loadComplete(List<LocalMediaFolder> folders);
    }

}
