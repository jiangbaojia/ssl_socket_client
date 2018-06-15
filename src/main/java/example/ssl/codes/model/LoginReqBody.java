package example.ssl.codes.model;

import example.ssl.codes.protobuf.Employee;

/**
 * Created by Eric on 2017/11/21.
 */
public class LoginReqBody extends Body {
    private String SID;
    private String LoginAccount;
    private String pwd;

    public LoginReqBody(String sid, String loginAccount, String pwd){
        this.SID = sid;
        this.LoginAccount = loginAccount;
        this.pwd = pwd;
    }

    @Override
    public byte[]buildPackage() throws Exception{
        Employee.LoginReq.Builder req = Employee.LoginReq.newBuilder();
        req.setSID(this.SID);
        req.setLoginAccount(this.LoginAccount);
        req.setPwd(this.pwd);

        byte[] data = req.build().toByteArray();
        return data;
    }
}
