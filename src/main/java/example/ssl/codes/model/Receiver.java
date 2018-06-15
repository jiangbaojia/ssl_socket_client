package example.ssl.codes.model;

import example.ssl.codes.SocketClient;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLSocket;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by Eric on 2017/11/14.
 */
public class Receiver extends Thread {
    static Logger logger = Logger.getLogger(Receiver.class);
    //private SocketClient client;
    public Receiver() {super();}

    private static final int REC_SPACE_TIME = 10;//接收信息间隔（毫秒）

    @Override
    public void run(){
        SSLSocket s = null;
        try {
            logger.info("Received data buffer");
            s = SocketClient.getSSLSocket();

            InputStream input = s.getInputStream();

            while(null!=s && s.isConnected() && null!=input){
                byte[] headPacket = new byte[Head.Header_Length];
                int len = 0;
                while (len < Head.Header_Length){
                    len += input.read(headPacket, len, Head.Header_Length - len);
                }
                //int len = input.read(headPacket);
                logger.info("Package header length: " + len);
                if (len==Head.Header_Length) {//包头信息完整
                    ByteBuffer headBuf = ByteBuffer.allocate(Head.Header_Length);
                    headBuf.put(headPacket);
                    headBuf.flip();

                    headBuf.getInt();
                    int leng = headBuf.getInt();
                    int bodyDataLen = leng - Head.Header_Length;
                    byte[] bodyPacket = new byte[bodyDataLen];
                    if(bodyDataLen ==input.read(bodyPacket)){//包体信息完整
                        //根据不同命令码进行业务处理
                        Dispatcher handler = new Dispatcher(headPacket,bodyPacket);
                        handler.start();
                    }else if(bodyDataLen ==0){//网络中断，包体信息长度为0
                        SocketClient.Close();
                    }
                }else if(len==0){//网络中断，包头信息长度为0
                    SocketClient.Close();
                    break;
                }
                Thread.sleep(REC_SPACE_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());

            try {
                SocketClient.Close();
//					Client.reConnect();
            } catch (Exception e1) {
                logger.error(e1.getMessage());
            }
            return;
        }
        super.run();
    }
}
