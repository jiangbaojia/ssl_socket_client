package example.ssl.codes.model;

import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * Created by Eric on 2017/11/23.
 */
public class HeartTask extends TimerTask {
    static Logger logger = Logger.getLogger(HeartTask.class);
    private HeartReqSender _sender;
    public HeartTask() {
        super();
        HeartReqSender sender = new HeartReqSender();
        this._sender = sender;
    }

    @Override
    public void run() {
        try {
            if(null!=_sender){
                logger.info("Heartbeat sid:" + _sender.get_sid());
                _sender.run();
                //Client.heartSid = sender.getSid();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
