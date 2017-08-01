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

public class StreamUtils {

    public static void closeInputStream(InputStream inputStream){
        try {
            if(null != inputStream){
                inputStream.close();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void closeOutputStream(OutputStream outputStream){
        try {
            if(null != outputStream){
                outputStream.close();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void closeInputStreamReader(InputStreamReader inputStreamReader){
        try {
            if(null != inputStreamReader){
                inputStreamReader.close();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void closeOutputStreamReader(OutputStreamWriter outputStreamWriter){
        try {
            if(null != outputStreamWriter){
                outputStreamWriter.close();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
