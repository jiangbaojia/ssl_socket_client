package example.ssl.codes.model;

import example.ssl.codes.SocketClient;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2017/11/14.
 */
public class Sender extends Thread {
    static Logger logger = Logger.getLogger(Sender.class);
    @Override
    public void run() {
        try {
            send();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            try {
                SocketClient.Close();
//				Client.reConnect();
            } catch (Exception e1) {
                e1.printStackTrace();
                logger.error(e1.getMessage());
            }
            return;
        }
        super.run();
    }

    /**
     * 发送
     * @throws Exception
     */
    private void send()throws Exception{
        OutputStream output = getOutputStream();

        byte[] packetByteArray = getPackageByteArray();

        sendPacket(output, packetByteArray);
    }

    /**
     * 获取输出字节流
     * @return
     * @throws Exception
     */
    private OutputStream getOutputStream()throws Exception{
        SocketClient client = SocketClient.createClient();
        SSLSocket s = client.getSSLSocket();
        OutputStream output = s.getOutputStream();

        return output;
    }

    //合并协议头与协议体
    protected byte[] initPacket(byte[] packetBody, int command, int market) throws Exception {
        Head head = new Head(packetBody.length,command,market);
        byte[] packetHead = head.bulidPackage();

        List<byte[]> packet = new ArrayList<byte[]>();
        packet.add(packetHead);
        packet.add(packetBody);

        byte[] packetByteArray = ByteArrayUtils.streamCopy(packet);
        return packetByteArray;
    }

    /**
     * 获取输出内容
     * @return
     * @throws Exception
     */
    protected byte[] getPackageByteArray()throws Exception{
        return null;
    }

    //发送包
    private void sendPacket(OutputStream output, byte[] packetByteArray)
            throws IOException {
        if(null!=packetByteArray && packetByteArray.length>0)
            output.write(packetByteArray);
        logger.error("sent: packet");
        output.flush();
    }
}
