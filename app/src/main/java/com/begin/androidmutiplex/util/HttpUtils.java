package com.begin.androidmutiplex.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * http/https网络请求封装类
 * @Author zhouy
 * @Date 2017-06-21
 */

public class HttpUtils {

    private static final String KEY_STORE_TYPE_BKS = "bks";//证书类型 固定值
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型 固定值

    private static final String KEY_STORE_CLIENT_PATH = "client.p12";//客户端要给服务器端认证的证书
    private static final String KEY_STORE_TRUST_PATH = "client.truststore";//客户端验证服务器端的证书库
    private static final String KEY_STORE_PASSWORD = "123456";// 客户端证书密码
    private static final String KEY_STORE_TRUST_PASSWORD = "123456";//客户端证书库密码

    private static final TrustManager[] TRUST_MANAGER = { new CustomTrustManager() };

    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Http网络请求相关属性配置
     * @param urlStr
     * @param method
     * @return
     * @throws IOException
     */
    public static HttpURLConnection initHttp(String urlStr, String method)
            throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(Constants.CONNECT_TIME_OUT);
        connection.setReadTimeout(Constants.READ_TIME_OUT);
        connection.setDoInput(true);
        //若设置了setDoOutput(true),那么即使设置请求方法为GET，最终请求方式还是POST
        if(Constants.POST_REQUEST.equals(method)){
            connection.setDoOutput(true);
        }
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
        connection.setRequestProperty("Charset", "UTF-8");
        return connection;
    }

    /**
     * Https网络请求相关属性配置
     * 可实现客户端与服务端的双向验证
     *可选择信任所有连接
     * @param context
     * @param url
     * @param method
     * @param headers
     * @param trustAllHost
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     */
    public static HttpsURLConnection initHttps(Context context, String url, String method, Map<String, String> headers, boolean trustAllHost)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
            KeyManagementException, KeyStoreException, UnrecoverableKeyException {

        SSLContext sslContext = getSSLContext(context,trustAllHost);
        URL _url = new URL(url);
        HttpsURLConnection https = (HttpsURLConnection) _url.openConnection();
        // 连接超时
        https.setConnectTimeout(Constants.CONNECT_TIME_OUT);
        // 读取超时 --服务器响应比较慢，增大时间
        https.setReadTimeout(Constants.READ_TIME_OUT);
        https.setRequestMethod(method);
        if(Constants.POST_REQUEST.equals(method)){
            https.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            https.setDoOutput(true);
        }
        if (null != headers && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                https.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if(trustAllHost){
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
        }else {
            if(null != sslContext){
                https.setSSLSocketFactory(sslContext.getSocketFactory());
            }
        }
        https.setDoInput(true);
        https.connect();
        return https;
    }


    public static SSLContext getSSLContext(Context context, boolean trustAllHost){
        if(trustAllHost){
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, TRUST_MANAGER, null);
                return sc;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                // 服务器端需要验证的客户端证书
                KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
                // 客户端信任的服务器端证书
                KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);

                InputStream ksIn = context.getResources().getAssets().open(KEY_STORE_CLIENT_PATH);
                InputStream tsIn = context.getResources().getAssets().open(KEY_STORE_TRUST_PATH);
                try {
                    keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
                    trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ksIn.close();
                    } catch (Exception ignore) {
                    }
                    try {
                        tsIn.close();
                    } catch (Exception ignore) {
                    }
                }
                SSLContext sslContext = SSLContext.getInstance("TLS");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
                keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
                return sslContext;
            } catch (Exception e) {
                LogUtils.e("tag", e.getMessage());
            }
        }
        return null;
    }


    private static class CustomTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
}


