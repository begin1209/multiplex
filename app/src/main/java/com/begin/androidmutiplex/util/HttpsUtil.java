package com.begin.androidmutiplex.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @Author zhouy
 * @Date 2017-09-22
 */

public class HttpsUtil {

    private static final String[] SERVER_CERT = new String[]{};

    /**
     * 单向验证只需传入服务端证书，后两个参数为null
     * 一般情况下 单向验证已经足够，
     * 对安全需要更高要求的业务，如银行、金融类等才用双向验证
     * @param context
     * @param bksFile
     * @param password
     */
    public static void initHttps(Context context, String bksFile, String password) {
        InputStream[] certificates = getCertificates(context, SERVER_CERT);
        InputStream bksInput = null;
        try {
             bksInput = new FileInputStream(bksFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            CloseUtils.close(bksInput);
        }
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(certificates, bksInput, password);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
        if(null == certificates){
            HttpsURLConnection.setDefaultHostnameVerifier(getUnSafeHostnameVerifier());
        }

    }


    /**
     * 获取服务端证书（客户端信任的），默认放在assets文件夹下
     * @param context
     * @param fileNames
     * @return
     */
    public static InputStream[] getCertificates(Context context, String... fileNames) {
        if (null == context || null == fileNames || fileNames.length <= 0) {
            return null;
        }
        try {
            InputStream[] certificates = new InputStream[fileNames.length];
            for (int i = 0; i < certificates.length; i++) {
                certificates[i] = context.getAssets().open(fileNames[i]);
            }
            return certificates;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取SSLSocketFactory,
     * 单向验证只需要传入服务端证书，后两个为null，
     * 双向验证需要传入bksFile,password
     * 不验证，即信任所有证书时全部传null，同时配合getUnSafeHostnameVerifier(),有安全隐患，慎用！！！
     * @param certificates
     * @param bksFile  客户端证书（服务端需要验证）
     * @param password
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(InputStream[] certificates, InputStream bksFile, String password){
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            //利用TrustManager创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            if(null == trustManagers || trustManagers.length <= 0){
                trustManagers = new TrustManager[]{new UnSafeTrustManager()};
            }
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException kme){
            kme.printStackTrace();
        }
        return null;
    }


    /**
     * 利用客户端需要验证的服务端CA证书获取受信任的TrustManager
     * @param certificates
     * @return
     */
    public static TrustManager[] prepareTrustManager(InputStream... certificates){
        if(null == certificates || certificates.length <= 0){
            return null;
        }
        try {
            //创建一个keystore 存储所有的CA证书
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);  //使用默认证书
            keyStore.load(null);  //去掉默认证书

            int index = 0;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            for (int i = 0; i < certificates.length; i++){
                //通过inputStream获取每个证书Certificate
                String caAlias = "caAlias"+index++;     //为证书设置别名
                Certificate ca = cf.generateCertificate(certificates[i]);
                keyStore.setCertificateEntry(caAlias, ca);  //设置自己的证书
                //关闭流
                CloseUtils.close(certificates[i]);
            }
            //创建TrustManager信任KeyStore中的所有CA证书,并返回TrustManager
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            return tmf.getTrustManagers();
            // TODO: 2016/11/11 针对有效期异常导致校验失败的情况，目前没有完美的解决方案
//            TrustManager[] keyStoreTrustManagers = tmf.getTrustManagers();
//            return getNotValidateTimeTrustManagers((X509TrustManager[]) keyStoreTrustManagers);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        } catch (NoSuchAlgorithmException nae){
            nae.printStackTrace();
        } catch (CertificateException ce){
            ce.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param bksFile 服务端需要验证的客户端证书
     * @param password
     * @return
     */
    public static KeyManager[] prepareKeyManager(InputStream bksFile, String password){
        if(null == bksFile || null == password){
            return null;
        }
        try {
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            String kmfAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(kmfAlgorithm);
            kmf.init(clientKeyStore, password.toCharArray());
            return kmf.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        } catch (NoSuchAlgorithmException nae){
            nae.printStackTrace();
        } catch (CertificateException cfe){
            cfe.printStackTrace();
        } catch (UnrecoverableKeyException uke){
            uke.printStackTrace();
        }
        return null;
    }

    /**
     * 不校验证书有效期的TrustManager
     * <p>
     * 防止用户乱改手机时间导致校验失败
     * 注意：由于校验证书时，对有效期的校验并不是最后一项，所以该TrustManager仍然存在安全隐患，并不推荐使用
     */
    private static class NotValidateTimeTrustManager implements X509TrustManager {

        private X509TrustManager defaultTrustManager;

        public NotValidateTimeTrustManager(X509TrustManager defaultTrustManager) {
            this.defaultTrustManager = defaultTrustManager;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            defaultTrustManager.checkClientTrusted(chain, authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException e) {
                e.printStackTrace();
                Throwable t = e;
                while (t != null) {
                    if (t instanceof CertificateExpiredException
                            || t instanceof CertificateNotYetValidException)
                        return;
                    t = t.getCause();
                }
                throw e;
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return defaultTrustManager.getAcceptedIssuers();
        }
    }

    private static NotValidateTimeTrustManager[] getNotValidateTimeTrustManagers(X509TrustManager[] trustManagers) {
        NotValidateTimeTrustManager[] notValidateTimeTrustManagers = new NotValidateTimeTrustManager[trustManagers.length];
        for (int i = 0; i< trustManagers.length; i++) {
            notValidateTimeTrustManagers[i] = new NotValidateTimeTrustManager(trustManagers[i]);
        }
        return notValidateTimeTrustManagers;
    }

    private static class UnSafeTrustManager implements X509TrustManager {
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

    private static class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * 不验证，即信任所有证书时使用
     * 有安全隐患，慎用！！！
     *
     * @return
     */
    public static UnSafeHostnameVerifier getUnSafeHostnameVerifier() {
        return new UnSafeHostnameVerifier();
    }
}
