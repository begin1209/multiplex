package com.begin.androidmutiplex.util;

/**
 * @Author zhouy
 * @Date 2017-05-08
 */

public class ConvertUtils {

    private static final String HEX = "0123456789ABCDEF";

    /**
     * 字节数组转换成16进制
     * @param bytes
     * @return
     */
    public static String bytes2HexStr(byte[] bytes){
        StringBuilder builder = new StringBuilder();
        if(bytes == null || bytes.length == 0){
            return null;
        }
        for (byte b: bytes){
            LogUtils.e("转换字符串", b+"|");
            builder.append(HEX.charAt((b>>4)&0x0f));
            builder.append(HEX.charAt(b&0x0f));
        }
        return builder.toString();
    }

    public static byte[] hexStr2Bytes(String hexStr){
        if(null == hexStr || "".equals(hexStr)){
            return null;
        }
        hexStr = hexStr.toLowerCase();
        final byte[] byteArray = new byte[hexStr.length() / 2];
        int k = 0;
        for(int i = 0; i < byteArray.length; i++){
            byte high = (byte)(Character.digit(hexStr.charAt(k), 16) & 0xff);
            byte low = (byte)(Character.digit(hexStr.charAt(k + 1),16) & 0xff);
            byteArray[i] = (byte)(high << 4 | low);
        }
        return byteArray;
    }

}
