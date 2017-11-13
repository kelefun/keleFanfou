package com.funstill.library.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dee on 15/11/20.
 */
public class FileUtils {
    public static final String POSTFIX = ".JPEG";
    public static final String APP_NAME = "KeleFun";

    public static File createCameraFile(Context context,String filePath) {
        return createMediaFile(context,filePath);
    }
    public static File createCropFile(Context context,String filePath) {
        return createMediaFile(context,filePath);
    }

    private static File createMediaFile(Context context,String parentPath){
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED)?Environment.getExternalStorageDirectory():context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath(), parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()){

        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = APP_NAME + "_" + timeStamp + "";
        File tmpFile = new File(folderDir, fileName + POSTFIX);
        return tmpFile;
    }
    /**
     * 创建初始文件夹。保存拍摄图片和裁剪后的图片
     *
     * @param filePath 文件夹路径
     */
    public static void createFile(String filePath) {
        String externalStorageState = Environment.getExternalStorageState();

        File dir = new File(Environment.getExternalStorageDirectory() ,filePath);

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdirs();
            }

//            File file = new File(cropFile, ".nomedia");    // 创建忽视文件。   有该文件，系统将检索不到此文件夹下的图片。
//            if (!file.exists()) {
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    }

}
