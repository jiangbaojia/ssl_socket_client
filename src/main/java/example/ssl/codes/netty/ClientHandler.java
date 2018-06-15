package example.ssl.codes.netty;

import example.ssl.codes.protobuf.Employee;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * Created by Raymond on 2/9/2016.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Employee.EmployeeRes> {
    private volatile Channel channel = null;
    private Employee.EmployeeRes svrResponse = null;

    public void send(int id, String msg){
        Employee.EmployeeReq.Builder builder = Employee.EmployeeReq.newBuilder();
        builder.setSID("01234").setLoginId(id).setLoginAccount(msg);

        channel.writeAndFlush(builder.build());
    }

    public String getMessage(){
        if(svrResponse != null){
            return svrResponse.getSID();
        }
        return null;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx){
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Employee.EmployeeRes response) throws Exception {
        svrResponse = response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
