package example.ssl.codes.model;

/**
 * Created by Eric on 2017/11/14.
 */
public class EmployeeReqSender extends Sender {
    @Override
    public byte[] getPackageByteArray() throws Exception{
        EmployeeReqBody body = new EmployeeReqBody("01234", 123, "erickong");
        byte[] packetBody = body.buildPackageBody();

        byte[] packetByteArray = initPacket(packetBody, Command.EmployeeReq,10);

        return packetByteArray;
    }
}
