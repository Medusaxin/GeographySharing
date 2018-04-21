package com.example.lenovo.geographysharing.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shdeng on 2018/4/20.
 */

public class CompressImageUtil {

    public static CompressImageUtil compressImageUtil = new CompressImageUtil();

    public static File currentImageFile = null;//图片压缩后储存的位置

    public static String filename = null;//压缩后的文件名，用于上传直接调用。
    /*
压缩

 */
//采样率压缩（根据路径获取图片并压缩）：
    public Bitmap getSmallBitmap_File(File file, int reqWidth, int reqHeight) {//针对相机
        try {
            String filePath = file.getAbsolutePath();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//开始读入图片，此时把options.inJustDecodeBounds 设回true了
            BitmapFactory.decodeFile(filePath, options);//此时返回bm为空
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//设置缩放比例 数值越高，图片像素越低
            options.inJustDecodeBounds = false;//重新读入图片，注意此时把options.inJustDecodeBounds 设回false了
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            compressImage(bitmap);
            return bitmap;
        } catch (Exception e) {
            Log.d("wzc", "类:" + this.getClass().getName() + " 方法：" + Thread.currentThread()
                    .getStackTrace()[0].getMethodName() + " 异常 " + e);
            return null;
        }
    }
    public Bitmap getSmallBitmap_String(String filePath, int reqWidth, int reqHeight) {//针对相册
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//开始读入图片，此时把options.inJustDecodeBounds 设回true了
            BitmapFactory.decodeFile(filePath, options);//此时返回bm为空
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//设置缩放比例 数值越高，图片像素越低
            options.inJustDecodeBounds = false;//重新读入图片，注意此时把options.inJustDecodeBounds 设回false了
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            compressImage(bitmap);
            return bitmap;
        } catch (Exception e) {
            Log.d("wzc", "类:" + this.getClass().getName() + " 方法：" + Thread.currentThread()
                    .getStackTrace()[0].getMethodName() + " 异常 " + e);
            return null;
        }
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        try {
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 1;  //1表示不缩放
            if (height > reqHeight || width > reqWidth) {
                int heightRatio = Math.round((float) height / (float) reqHeight);
                int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            return inSampleSize;
        } catch (Exception e) {
            Log.d("wzc", "类:" + this.getClass().getName() + " 方法：" + Thread.currentThread()
                    .getStackTrace()[0].getMethodName() + " 异常 " + e);
            return 1;
        }
    }

    // 质量压缩法：
    private Bitmap compressImage(Bitmap image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 200) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

            }
            //压缩好后写入文件中
            FileOutputStream fos = new FileOutputStream(currentImageFile.getAbsolutePath());
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public void CreateFiles() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(), "GeoShare");//在sd下创建文件夹myimage；Environment.getExternalStorageDirectory()得到SD卡路径文件
        if (!dir.exists()) {    //exists()判断文件是否存在，不存在则创建文件
            dir.mkdirs();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
        filename = df.format(new Date());
        currentImageFile = new File(dir, filename + ".jpg");
        if (!currentImageFile.exists()) {
            currentImageFile.createNewFile();
        }
    }
}
