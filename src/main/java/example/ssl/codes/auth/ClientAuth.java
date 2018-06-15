package example.ssl.codes.auth;

import example.ssl.codes.config.Configuration;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.util.Properties;

/**
 * Created by Eric on 2017/9/12.
 */
public class ClientAuth {
    private static SSLContext sslContext;

    public static SSLContext getSSLContext() throws Exception{
        Properties p = Configuration.getConfig();
        String protocol = p.getProperty("protocol");
        String clientTrustCerFile = p.getProperty("clientTrustCer");
        String clientTrustCerPwd = p.getProperty("clientTrustCerPwd");

        //Trust Key Store
        KeyStore keyStore = KeyStore.getInstance("JKS");

        String path = URLDecoder.decode(Configuration.class.getClassLoader().getResource(clientTrustCerFile).getPath(), "utf-8");
        keyStore.load(new FileInputStream(path),
                clientTrustCerPwd.toCharArray());


        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        TrustManager[] tms = trustManagerFactory.getTrustManagers();

        KeyManager[] kms = null;
        if(Configuration.getConfig().getProperty("authority").equals("2")){
            String clientKeyPwd = p.getProperty("clientKeyPwd");

            /* 此处不适用
            String clientCerFile = p.getProperty("clientCer");
            String clientCerPwd = p.getProperty("clientCerPwd");

            //Key Store
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(clientCerFile),
                    clientCerPwd.toCharArray());
                    */

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, clientKeyPwd.toCharArray());
            kms = keyManagerFactory.getKeyManagers();
        }
        sslContext = SSLContext.getInstance(protocol);
        sslContext.init(kms, tms, null);

        return sslContext;
    }
}
