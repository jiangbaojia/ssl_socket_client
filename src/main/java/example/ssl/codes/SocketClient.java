package example.ssl.codes;

import example.ssl.codes.auth.ClientAuth;
import example.ssl.codes.config.Configuration;
import example.ssl.codes.model.Constant;
import example.ssl.codes.model.LoginReqSender;
import example.ssl.codes.model.Receiver;
import example.ssl.codes.model.StateCheckTask;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.util.Properties;
import java.util.Timer;

/**
 * Created by Eric on 2017/11/13.
 */
public class SocketClient {
    static Logger logger = Logger.getLogger(SocketClient.class);
    private SSLContext sslContext;
    private int port = 8888;
    private String host = "localhost";
    private Properties p;

    public static int state = 0;//0-未连接 1-正在重连 2-连接正常

    private static SocketClient client = null;
    private static SSLSocket socket = null;
    //创建链路
    public static SocketClient createClient() throws Exception{
        if (null == client){
            client = new SocketClient();
        }
        return client;
    }

    public static SSLSocket getSSLSocket() throws Exception{
        if (null == socket || socket.isClosed()) {
            if (null == client)
                client = new SocketClient();
            socket = client.createSocket();
        }
        return socket;
    }

    private SSLSocket createSocket() throws Exception{
        try {
            p = Configuration.getConfig();

            sslContext = ClientAuth.getSSLContext();
            SSLSocketFactory factory = (SSLSocketFactory) sslContext.getSocketFactory();

            SSLSocket s = (SSLSocket) factory.createSocket(host, port);
            s.setUseClientMode(true);
            s.setReceiveBufferSize(1024 * 1024);

            s.startHandshake();
            return s;

/*
            socket = (SSLSocket)factory.createSocket();
            String[] pwdsuits = socket.getSupportedCipherSuites();
            //socket可以使用所有支持的加密套件
            socket.setEnabledCipherSuites(pwdsuits);
            //默认就是true
            socket.setUseClientMode(true);
            SocketAddress address = new InetSocketAddress(host, port);
            socket.connect(address, 0);

            MyHandshakeCompletedListener listener = new MyHandshakeCompletedListener();
            socket.addHandshakeCompletedListener(listener);
            socket.setSendBufferSize(256 * 1024);
            socket.setReceiveBufferSize(1024 * 1024);*/
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("socket establish failed!");
            return null;
        }
    }

    public static void Start() throws Exception {
        createClient();
        getSSLSocket();
        if (null != socket) {

            Receiver r = new Receiver();
            r.start();

            Thread.sleep(3000);

            //发送登录请求
            LoginReqSender s = new LoginReqSender();
            s.start();

            Timer timer = new Timer();
            StateCheckTask cht = new StateCheckTask();
            timer.schedule(cht, 60 * 1000, 5 * 1000);// 1分钟后每5秒检查一次
        }else{
            //logger.info("Socket created failed");
            System.out.println("Socket created failed! error Remote server address");
        }
    }

    public static void Close() throws Exception{
        SocketClient.state = 0;
        if (null != socket && socket.isConnected()){
            socket.close();
        }
    }

    public static void main(String[] args) throws Exception {
        Start();

        Thread.sleep(1000);
    }

    //断线重连
    public static void Reconnect() throws Exception{
        try {
            if (null != Constant.HeartBeatTimer) {
                Constant.HeartBeatTimer.cancel();
            }
            SocketClient.socket = null;
            SocketClient.state = 1;
            SocketClient.Start();
        }catch (Exception e){
            SocketClient.Close();
        }
    }

}
