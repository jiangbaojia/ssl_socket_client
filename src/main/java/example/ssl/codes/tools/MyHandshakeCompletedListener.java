package example.ssl.codes.tools;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;

/**
 * Created by Eric on 2017/9/12.
 */
public class MyHandshakeCompletedListener implements HandshakeCompletedListener {
    public void handshakeCompleted(HandshakeCompletedEvent arg0) {
        System.out.println("Handshake finished successfully");
    }
}
