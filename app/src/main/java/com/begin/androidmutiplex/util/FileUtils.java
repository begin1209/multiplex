package com.begin.androidmutiplex.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件操作工具类
 * @Author zhouy
 * @Date 2017-05-10
 */

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 拷贝assets下的文件
     * @param context
     * @param src
     * @param des
     * @return
     */
    public static boolean copyAssetFiles(Context context, String src, String des){
        if(null == context || TextUtils.isEmpty(src) || TextUtils.isEmpty(des)){
            return false;
        }
        AssetManager manager = context.getAssets();
        try{
            String[] assetFiles = manager.list(src);
            if(null != assetFiles && assetFiles.length > 0){
                //如果是目录
                File file = new File(des);
                if(!file.exists()){
                    file.mkdirs();
                }
                //文件不存在 递归调用
                for (String fileName: assetFiles){
                    copyAssetFiles(context, src+"/"+fileName, des+"/"+fileName);
                }
            }else{
                copyFile(manager.open(src), src,des);
            }
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 拷贝SD卡上的文件
     * @param src
     * @param des
     * @return
     */
    public static boolean copySDFiles(String src, String des){
        if(TextUtils.isEmpty(src) || TextUtils.isEmpty(des)){
            return false;
        }
        try{
            File srcFile = new File(src);
            if(srcFile.exists()){
                String[] srcFiles = srcFile.list();
                if(null != srcFiles && srcFiles.length > 0){
                    //如果是目录
                    File file = new File(des);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    //文件不存在 递归调用
                    for (String fileName: srcFiles){
                        copySDFiles(src+"/"+fileName, des+"/"+fileName);
                    }
                }else{

                    copyFile(new FileInputStream(srcFile), src,des);
                }
                return true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 文件拷贝
     * @param inputStream
     * @param fileName
     * @param des
     * @return
     */
    public static boolean copyFile(InputStream inputStream, String fileName, String des){
        String newFileName = null;
        if(null == inputStream || TextUtils.isEmpty(fileName) || TextUtils.isEmpty(des)){
            return false;
        }
        OutputStream outputStream = null;
        try {
            newFileName = des;
            File file = new File(newFileName);
            if(!file.exists()){
                file.createNewFile();
            }
            outputStream = new FileOutputStream(newFileName);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0, length);
            }
            outputStream.flush();
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(null != outputStream){
                    outputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 读取文件夹下的所有内容，转化成字符串
     * @param filePath
     * @return
     */
    public static String readFileAtDir(String filePath){
        if(TextUtils.isEmpty(filePath)){
            return null;
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader bf = null;
        InputStream inputStream = null;
        try {
            File file = new File(filePath);
            if(file.exists()){
                inputStream = new FileInputStream(file);
////                inputStream = context.getAssets().open("test/index.html");
                bf = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String line = "";
                while ((line = bf.readLine()) != null){
                    builder.append(line);
                }
                return builder.toString();

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(null != inputStream){
                    inputStream.close();
                }
                if(null != bf){
                    bf.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 读取文件，转换成字节数组
     * @param filePath
     * @return
     */
    public static byte[] readFileBytes(String filePath){
        if(TextUtils.isEmpty(filePath)){
            return null;
        }

        ByteArrayOutputStream bos = null;
        InputStream fileInput = null;
        try {
            File file = new File(filePath);
            if(file.exists()) {
                fileInput = new FileInputStream(file);
                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 *1024];
                int len = -1;
                while ((len = fileInput.read(buffer)) != -1){
                    bos.write(buffer, 0 , len);
                }
                bos.flush();
                return bos.toByteArray();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(null != bos){
                    bos.close();
                }
                if(null != fileInput){
                    fileInput.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 解压文件
     * @param fileName 待解压文件名(包含路径)
     * @param outputDir 解压输出路径
     * @param reWrite 是否覆盖
     * @return true解压成功， false解压遇到错误或者异常
     */
    public static boolean unZip(String fileName,
                                String outputDir, boolean reWrite) throws Exception {
        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(outputDir)) {
            throw new Exception("fileName is null or outputDir is null");
        }
        //创建压缩文件目录
        File outFile = new File(outputDir);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        //打开压缩文件
        File file = new File(fileName);
        InputStream inputStream = null;
        ZipInputStream zipInputStream = null;
        try {
            inputStream = new FileInputStream(file);
            zipInputStream = new ZipInputStream(inputStream);
            ZipEntry entry = zipInputStream.getNextEntry();
            //创建1M缓冲区
            byte[] buffer = new byte[1024 * 1024];
            int count = 0;
            //遍历所有文件和目录
            while (null != entry) {
                if (entry.isDirectory()) {
                    outFile = new File(outputDir + File.separator + entry.getName());
                    if (reWrite || outFile.exists()) {
                        outFile.mkdirs();
                    }
                } else {
                    outFile = new File(outputDir + File.separator + entry.getName());
                    if(!outFile.getParentFile().exists()){
                        outFile.getParentFile().mkdirs();
                    }
                    if (reWrite || !outFile.exists()) {
                        outFile.createNewFile();
                        FileOutputStream outputStream = new FileOutputStream(outFile);
                        while ((count = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, count);
                        }
                        outputStream.close();
                    }
                }
                entry = zipInputStream.getNextEntry();
            }
            LogUtils.v(FileUtils.class.getSimpleName(), "解压完成");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(null != zipInputStream){
                    zipInputStream.close();
                }
                if(null != inputStream){
                    inputStream.close();
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除文件
     * @param fileName
     */
    public static void deleteFile(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
    }

    /**
     * 删除非null文件夹
     * @param fileDir
     */
    public static void deleteDir(String fileDir){
        //递归删除文件
        File file = new File(fileDir);
        if(null == file || !file.exists() || !file.isDirectory()){
            return;
        }
        File[] files = file.listFiles();
        for(File temp: files){
            if(temp.isFile()){
                temp.delete();
            }else if(temp.isDirectory()) {
                deleteDir(temp.getAbsolutePath());
            }
        }
        //删除自身
        file.delete();
    }


    /**
     * 写字符串到文件
     * @param fileName
     * @param isAppend
     * @return
     */
    public static boolean write2File(String fileName, String data, boolean isAppend){
        File file = new File(fileName);
        FileOutputStream outputStream = null;
        PrintWriter writer = null;
        FileChannel fileChannel = null;
        FileLock fileLock = null;
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            //添加文件锁，直到文件获得锁，才开始往文件中添加数据
//            fileChannel = outputStream.getChannel();
//            do{
//                fileLock = fileChannel.tryLock();
//            }while (null == fileLock);
//            ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
//            fileChannel.write(buffer);
//            LogUtils.e("读写文件", buffer.toString());
            FileWriter fileWriter = new FileWriter(file, true);
            writer = new PrintWriter(fileWriter);
            writer.println(data);
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
//                if(null != fileLock){
//                    fileLock.release();
//                    fileLock = null;
//                }
//                if(null != fileChannel){
//                    fileChannel.close();
//                    fileChannel = null;
//                }

                if(null != outputStream){
                    outputStream.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return false;
    }
}
