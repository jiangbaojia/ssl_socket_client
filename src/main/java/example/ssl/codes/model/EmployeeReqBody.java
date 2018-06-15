package example.ssl.codes.model;

import example.ssl.codes.protobuf.Employee;

/**
 * Created by Eric on 2017/11/13.
 */
public class EmployeeReqBody extends Body {
    private int loginID;
    private String loginAccount;
    private String sid;

    public EmployeeReqBody(String sid, int loginid, String loginaccount){
        this.loginID = loginid;
        this.sid = sid;
        this.loginAccount = loginaccount;
    }

    @Override
    public byte[]buildPackage() throws Exception{
        Employee.EmployeeReq.Builder req = Employee.EmployeeReq.newBuilder();
        req.setSID(this.sid);
        req.setLoginId(this.loginID);
        req.setLoginAccount(this.loginAccount);

        byte[] data = req.build().toByteArray();
        return data;
    }
}
