package example.ssl.codes.model;

/**
 * Created by Eric on 2017/11/23.
 */
public class HeartReqSender extends Sender {
    private String _sid;

    public String get_sid(){return _sid;}
    @Override
    public byte[] getPackageByteArray() throws Exception{
        this._sid = "01234";
        HeartReqBody body = new HeartReqBody(_sid, 123);
        byte[] packetBody = body.buildPackageBody();

        byte[] packetByteArray = initPacket(packetBody, Command.HeartBeatRea,10);

        return packetByteArray;
    }
}
