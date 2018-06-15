package example.ssl.codes.tools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Eric on 2017/9/12.
 */
public class SocketIO{
    public static DataInputStream getDataInput(Socket socket) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        return input;
    }

    public static DataOutputStream getDataOutput(Socket socket) throws IOException{
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        return out;
    }
}
