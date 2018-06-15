package example.ssl.codes.model;

import example.ssl.codes.SocketClient;
import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * Created by Eric on 2017/11/23.
 */
public class StateCheckTask extends TimerTask {
    static Logger logger = Logger.getLogger(StateCheckTask.class);
    private static int i = 0;//重连计数器

    @Override
    public void run() {
        // TODO Auto-generated method stub
        logger.info("检查连接状态");
        try {
            if (SocketClient.state == 1) {
                i++;
                logger.info("第"+i+"次重连中");
                if(i>1){
                    SocketClient.Close();
                    i = 0;
                }
            }
            if (SocketClient.state == 0) {
                logger.info("进入重连");
                SocketClient.Reconnect();
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
