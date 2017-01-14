package com.android.my.lab.lan;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final String PACKAGE_NAME = "com.jenwis.lab";
    private static final String FILE_DIR_NAME = "files";
    private static final String XMART_USB_PATH = "/mnt/usbhost";

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
        }
    }

    public static String getSaveFilePath() {
        if (hasSDCard()) {
            return getRootFilePath() + PACKAGE_NAME + "/" + FILE_DIR_NAME + "/";
        } else {
            return getRootFilePath() + PACKAGE_NAME + "/" + FILE_DIR_NAME;
        }
    }

    /**
     * 将图片缓存在本地，并返回图片的绝对路径
     * quality:0-100
     */
    public static String storePicture(Bitmap bitmap, String storePath, int quality) {
        File file = new File(storePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File imageFile = new File(file, System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = null;
        try {
            imageFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageFile.getAbsolutePath();
    }

    /*
    * 将图片保存到系统相册中
    * */
    public static void saveImageToGallery(Context context, Bitmap bmp, String subDir) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), subDir);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    public static String getFileStorePath(String subDir) {
        String storePath = getSaveFilePath();
        if (storePath.endsWith("/")) {
            storePath += subDir;
        } else {
            storePath += "/" + subDir;
        }

        return storePath;
    }

    /*
    * 获取usb挂载路径
    * */
    public static String[] getUsbMountDir() {
        String dirs = SystemProperties.get("sys.usb.label", null);
        if (!TextUtils.isEmpty(dirs)) {
            if (dirs.contains(",")) {
                return dirs.split(",");
            } else {
                return new String[]{dirs};
            }
        }

        return new String[]{XMART_USB_PATH};
    }

    public static boolean isUsbAvailable() {
        String[] usbMountDirs = getUsbMountDir();
        if (usbMountDirs != null) {
            for (String usbMountDir : usbMountDirs) {
                File usbDir = new File(usbMountDir);
                if (usbDir != null && usbDir.isDirectory()) {
                    File[] usbFiles = usbDir.listFiles();
                    if (usbFiles != null && usbFiles.length > 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void copyFile(String oldPath, String newPath, OnUpdateFileCopyFromUsbListener listener) {
        try {
            int byteSum = 0;
            int byteRead = 0;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead; //字节数 文件大小
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
                if (listener != null) {
                    listener.onCopySuccess();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int byteSum = 0;
            int byteRead = 0;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead; //字节数 文件大小
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }
    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File fileDir = null;
        File file = null;

        try {
            fileDir = new File(filePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
        } catch (Exception e) {
        }

        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 生成文件夹
     * @param filePath 文件夹路径
     */
    public static void makeDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String content, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = content + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {

        }
    }

    public interface OnUpdateFileCopyFromUsbListener {
        void onCopySuccess();
    }
}
