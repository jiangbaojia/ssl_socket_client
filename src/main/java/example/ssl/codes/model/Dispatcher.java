package example.ssl.codes.model;

import example.ssl.codes.SocketClient;
import example.ssl.codes.protobuf.Employee;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Timer;

/**
 * Created by Eric on 2017/11/14.
 */
public class Dispatcher extends Thread {
    static Logger logger = Logger.getLogger(Dispatcher.class);
    private byte[] headerByte;
    private byte[] bodyByte;

    public Dispatcher(byte[] headerbyte, byte[] bodybyte){
        this.headerByte = headerbyte;
        this.bodyByte = bodybyte;
    }

    public Dispatcher(){super();}

    @Override
    public void run(){
        ByteBuffer headBuf = ByteBuffer.allocate(Head.Header_Length);
        headBuf.put(headerByte);
        headBuf.flip();

        headBuf.getInt();
        headBuf.getInt();
        int command = headBuf.getInt();
        try{
            switch (command) {
                case Command.EmployeeReq:
                    //loginResHandler();
                    Employee.EmployeeRes result = Employee.EmployeeRes.parseFrom(this.bodyByte);
                    logger.info(result.getSID());
                    break;
                case Command.EmployeeRes:
                    Employee.EmployeeRes result0 = Employee.EmployeeRes.parseFrom(this.bodyByte);
                    logger.info("EmployeeRes Response sid:" + result0.getSID());
                    break;
                case Command.LoginRes:
                    logger.info("Received server login response");
                    Employee.LoginRes loginRes = Employee.LoginRes.parseFrom(this.bodyByte);

                    //定时发送心跳报文
                    Timer timer = new Timer();
                    HeartTask ht = new HeartTask();
                    timer.schedule(ht, Constant.LOGIN_SPACE_TIME, Constant.HEART_SPACE_TIME );
                    Constant.HeartBeatTimer = timer;

                    SocketClient.state = 2;//连接正常

                    EmployeeReqSender s = new EmployeeReqSender();
                    s.start();
                    break;

                default://其余抛弃
                    break;
            }
        }catch (Exception e) {
            // TODO: handle exception
            logger.info(e.getMessage());
        }
        super.run();
    }
}
