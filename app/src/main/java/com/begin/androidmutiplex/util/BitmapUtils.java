package com.begin.androidmutiplex.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author zhouy
 * @Date 2017-06-09
 */

public class BitmapUtils {


    /**
     * 获取Base64字符串
     * @param bitmap
     * @return
     */
    public static String getBase64String(Bitmap bitmap){
        ByteArrayOutputStream bos = null;
        String bmpStr;
        if (null != bitmap){
            try {
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bytes = bos.toByteArray();
                bmpStr = Base64.encodeToString(bytes, Base64.DEFAULT);
                return bmpStr;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if(null != bos){
                        bos.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从路径获取bitmap对象
     * @param uri
     * @return
     */
    public static Bitmap getBitmapByUri(Context context, Uri uri){
        InputStream input = null;
        Bitmap bitmap = null;
        try {
            input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 16;
            bitmap = BitmapFactory.decodeStream(input, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 通过输入流获取Bitmap
     * @param inputStream
     * @param rect
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapByStream(InputStream inputStream, Rect rect,
                                              int reqWidth, int reqHeight){
        //decodeSteam是无法重新解析一个流的 所以当第一次decodeSteam之后，第二次返回为null
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(inputStream, rect, options);
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream);

    }

    /**
     * 从文件中加载图片
     * @param pathName  文件路径
     * @param reqWidth  请求宽度 负数或者0:不采样 使用原始大小
     * @param reqHeight 请求高度
     * @return
     */
    public static Bitmap decodeBitmapByFile(String pathName, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //此时图片实际上未被加载至内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        //计算采样率
        options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);

    }


    /**
     * 通过字节流采样bitmap
     * @param bytes
     * @param offset
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapByByteArray(byte[] bytes, int offset, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //此时图片实际上未被加载至内存中
        options.inJustDecodeBounds = true;
        int length = bytes.length;
        BitmapFactory.decodeByteArray(bytes,offset, length, options);
        //计算采样率
        options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes,offset, length, options);
    }

    /**
     * 通过资源文件采样Bitmap大小
     * @param resources
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapByResource(Resources resources, int resId,
                                                int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //此时图片实际上未被加载至内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        //计算采样率
        options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }
    /**
     * 计算图片采样率
     * @param options
     * @param reqWidth ImageView请求的宽度
     * @param reqHeight ImageView请求高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        //raw width and height of image
        if(reqWidth == 0 || reqHeight == 0){
            return 1;
        }
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth){
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while ((halfWidth / inSampleSize) >= reqWidth &&
                    (halfHeight /inSampleSize) >= reqHeight){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
