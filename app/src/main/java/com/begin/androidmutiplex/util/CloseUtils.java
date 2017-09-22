package com.begin.androidmutiplex.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @Author zhouy
 * @Date 2017-07-28
 */

public class CloseUtils {

    public static void close(InputStream inputStream){
        try {
            if(null != inputStream){
                inputStream.close();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
