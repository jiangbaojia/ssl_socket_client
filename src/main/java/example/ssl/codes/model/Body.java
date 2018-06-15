package example.ssl.codes.model;

/**
 * Created by Eric on 2017/11/13.
 */

public abstract class Body {

    private byte[] packetBody;

    private byte[] getPacketBody() {
        return packetBody;
    }

    private void setPacketBody(byte[] packetBody) {
        this.packetBody = packetBody;
    }

    protected abstract byte[] buildPackage() throws Exception;


    /**
     * 获取包体
     *
     * @throws Exception
     * @return加密后的包体
     */
    public byte[] buildPackageBody() throws Exception {
        this.setPacketBody(buildPackage());
        return packetBody;
    }
}

