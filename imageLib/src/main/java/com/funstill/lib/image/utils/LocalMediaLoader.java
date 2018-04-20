package com.funstill.lib.image.utils;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.funstill.lib.R;
import com.funstill.lib.image.model.LocalMedia;
import com.funstill.lib.image.model.LocalMediaFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 * update by kelefun 17/05/10
 */
public class LocalMediaLoader {
    //    public static final int TYPE_CATEGORY= 0;
    public static final int TYPE_IMAGE = 1;
    //    public static final int TYPE_VIDEO = 2;
    private final static String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.SIZE,
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

    private List<LocalMediaFolder> imageFolderList = new ArrayList<>();//文件夹集合

    public void loadImage(final LocalMediaLoadListener imageLoadListener) {
        activity.getSupportLoaderManager().initLoader(type, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                if (id == TYPE_IMAGE) {
                    return new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, MediaStore.Images.Media.MIME_TYPE + " like '%image/%'", null, IMAGE_PROJECTION[2] + " DESC");
                }
//                else if (id == TYPE_VIDEO) {
//                    cursorLoader = new CursorLoader( activity, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,VIDEO_PROJECTION, null, null, VIDEO_PROJECTION[2] + " DESC");
//              }
                return null;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null && data.getCount() > 0) {
                    List<LocalMedia> recentImages = new ArrayList<>();//最近图片
                    while (data.moveToNext()) {
                        // 获取图片的路径
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long addDate = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        boolean showFlag = size > 1024 * 5;//图片大于5KB
                        if (showFlag) {
                            File imageFile = new File(path);                  // 获取图片文件
                            LocalMedia localMedia = new LocalMedia(imageFile.getAbsolutePath(), name, addDate);
                            if (recentImages.size() < 30) {//最近文件暂定为最多30张
                                recentImages.add(localMedia);
                            }

                            LocalMediaFolder localMediaFolder = getImageFolder(imageFile, imageFolderList);
                            localMediaFolder.getImageList().add(localMedia);
                            localMediaFolder.setImageNum(localMediaFolder.getImageList().size());
                        }
                    }//while结束
                    LocalMediaFolder recentImageFolder = new LocalMediaFolder();//最近图片文件夹
                    recentImageFolder.setImageList(recentImages);
                    recentImageFolder.setImageNum(recentImages.size());
                    recentImageFolder.setFirstImagePath(recentImages.get(0).getPath());
                    recentImageFolder.setName(activity.getString(R.string.recent_image));
                    imageFolderList.add(0, recentImageFolder);
                    imageLoadListener.loadComplete(imageFolderList);
                    if (data != null) data.close();
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }


    /**
     * 如果imgageFile 所属的文件夹不存在则new一个文件夹对象LocalMediaFolder
     * 存在则返回此文件夹对象
     *
     * @param imageFile
     * @param imageFolderList
     * @return
     */
    private LocalMediaFolder getImageFolder(File imageFile, List<LocalMediaFolder> imageFolderList) {
        File folderFile = imageFile.getParentFile();
        for (LocalMediaFolder folder : imageFolderList) {
            if (folder.getPath().equals(folderFile.getAbsolutePath())) {
                return folder;
            }
        }
        LocalMediaFolder newFolder = new LocalMediaFolder();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        newFolder.setFirstImagePath(imageFile.getPath());
        imageFolderList.add(newFolder);
        return newFolder;
    }

    public interface LocalMediaLoadListener {
        void loadComplete(List<LocalMediaFolder> folders);
    }

}
