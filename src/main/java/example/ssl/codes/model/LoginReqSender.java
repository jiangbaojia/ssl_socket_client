package example.ssl.codes.model;

/**
 * Created by Eric on 2017/11/21.
 */
public class LoginReqSender extends Sender {
    @Override
    public byte[] getPackageByteArray() throws Exception{
        LoginReqBody body = new LoginReqBody("01234", "erickong", "erickong");
        byte[] packetBody = body.buildPackageBody();

        byte[] packetByteArray = initPacket(packetBody, Command.LoginReq,10);

        return packetByteArray;
    }
}
