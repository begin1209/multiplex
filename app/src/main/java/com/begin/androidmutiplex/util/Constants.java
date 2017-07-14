package com.begin.androidmutiplex.util;

/**
 * @Author zhouy
 * @Date 2017-04-11
 */

public class Constants {

    //assets文件夹中文件拷贝结果文件名
    public static final String COPY_ASSETS_PREFERENCE = "copy_assets_preference";
    public static final String COPY_ASSET_RESULT = "copy_result";
    public static final String LOCAL_WEBVR_PATH = "file_path";

    //资源URL相关接口
    public static final String SOURCE_VERSION_INFO = "source_version_info";
    /**
     * 资源更新检查接口
     */
    public static final String SOURCE_CHECK_UPDATE = "http://fintech.ucsmy.com:8090/api/CheckUpdate/check";
    public static final String SOURCE_UPDATE_URL = "http://172.17.2.74:8080/work/grcbank.zip";
    public static final String SOURCE_DRC_NAME = "grcbank-taiyangjishi";
    public static final String SOURCE_DRC_TEMP = "temp";
    public static final String SOURCE_RELATIVE = "/gvrbank/vtour/module.json";


    /**
     * 用户行为统计接口
     */
//    public static final String PAGE_EVENT_STATISTICS = "http://172.17.2.131:8092/v1.0/upload/file/uploadFileAction";
    public static final String PAGE_EVENT_STATISTICS = "https://fintech.ucsmy.com:81/v1.0/upload/file/uploadDataAction";
//    public static final String PAGE_EVENT_STATISTICS = "http://172.17.2.161/test.php";

    public static final String UPLOAD_FILE_NAME_TEMP = "statistic.temp";
    public static final String UPLOAD_FILE_NAME_LOCK = "statistic.lock";
    public static final long UPLOAD_TIME_GAP = 2 * 3600 * 1000; //上报时间间隔
//    public static final long UPLOAD_TIME_GAP = 2 * 30000; //上报时间间隔

    //网络请求设置相关常量
    public static final int CONNECT_TIME_OUT = 25000;
    public static final int READ_TIME_OUT = 25000;
    public static final String POST_REQUEST = "POST";
    public static final String GET_REQUEST = "GET";

    //数据包更新相关
    public static final int DATA_UPDATE = 100;
    public static final int DATA_UPDATE_FAILED = 101;

    //数据压缩相关
    public static final int DATA_UNZIP_FINISHED = 200;
    public static final int DATA_UNZIP_FAILED = 201;

    //两次back键间隔退出
    public static final int BACK_CLICK_MESSAGE = 301;
    public static final int BACK_CLICK_INTERVAL = 2000;


    public static final int QUEST_CODE_LOCATION =1;   // 位置信息权限请求标志
    public static final int QUEST_CODE_CAMERA = 2;    // 摄像头权限标志
    public static final int QUEST_CODE_ALL = 3;      // 批量请求权限
    public static final int QUEST_CODE_STORAGE = 4;  // 文件存储权限
    public static final int QUEST_ALERT_WINDOW = 5;  // 悬浮框权限

    public static final String UTF_8 = "utf-8";


}
