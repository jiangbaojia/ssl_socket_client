package example.ssl.codes.model;

import example.ssl.codes.protobuf.Employee;

/**
 * Created by Eric on 2017/11/23.
 */
public class HeartReqBody extends Body {
    private String SID;
    private int LoginId;

    public HeartReqBody(String sid, int loginId){
        this.SID = sid;
        this.LoginId = loginId;
    }

    @Override
    public byte[]buildPackage() throws Exception{
        Employee.HeartBeatReq.Builder req = Employee.HeartBeatReq.newBuilder();
        req.setSID(this.SID);
        req.setLoginId(this.LoginId);

        byte[] data = req.build().toByteArray();
        return data;
    }
}
