package com.begin.androidmutiplex.util;

import android.content.Context;

/**
 * @Author zhouy
 * @Date 2017-07-24
 */

public class ResourceUtils {

    private static final String RES_LAYOUT = "layout";

    private static final String RES_ID = "id";

    private static final String RES_DRAWABLE = "drawable";

    private static final String RES_STRING  = "string";

    private static final String RES_STYLE = "style";

    private static final String RES_COLOR = "color";

    private static final String RES_MIPMAP = "mipmap";

    private static final String RES_ARRAY = "array";

    public static int getLayoutIdf(Context context, String name){
        return getIdentifier(context, name, RES_LAYOUT);
    }

    public static int getIdIdf(Context context, String name){
        return getIdentifier(context, name, RES_ID);
    }

    public static int getDrawableIdf(Context context, String name){
        return getIdentifier(context, name, RES_DRAWABLE);
    }

    public static int getStringIdf(Context context, String name){
        return getIdentifier(context, name, RES_STRING);
    }

    public static int getStyleIdf(Context context, String name){
        return getIdentifier(context, name, RES_STYLE);
    }

    public static int getColorIdf(Context context, String name){
        return getIdentifier(context, name, RES_COLOR);
    }

    public static int getMipmapIdf(Context context, String name){
        return getIdentifier(context, name, RES_MIPMAP);

    }    public static int getArrayIdf(Context context, String name){
        return getIdentifier(context, name, RES_ARRAY);
    }

    private static final int getIdentifier(Context context, String name, String defType){
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }
}
