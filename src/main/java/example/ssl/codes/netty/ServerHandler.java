package example.ssl.codes.netty;

import example.ssl.codes.model.Command;
import example.ssl.codes.protobuf.Employee;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Raymond on 2/9/2016.
 */
public class ServerHandler extends SimpleChannelInboundHandler<BaseMsg> {
    private static final int MAX_UN_REC_HEARTBEAT_TIME = 3;
/*    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Employee.EmployeeReq request) throws Exception {
        int id = request.getLoginId();

        Employee.EmployeeRes.Builder respBuilder = Employee.EmployeeRes.newBuilder();
        respBuilder.setLoginId(id + 1);
        respBuilder.setSID(request.getSID());

        Employee.EmployeeInfo.Builder info = Employee.EmployeeInfo.newBuilder();
        info.setEmployeeId(1);
        info.setEmployeeName("eric");

        respBuilder.setEmployeelist(info);

        channelHandlerContext.writeAndFlush(respBuilder.build());
        Server.logger.info("Server Response client command:");
    }*/

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //channel失效，从Map中移除
        NettyChannelMap.remove((SocketChannel)ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
        switch (baseMsg.getCommand()) {
            case Command.EmployeeReq:
                Employee.EmployeeReq data = Employee.EmployeeReq.parseFrom(baseMsg.getBodyData());
                int id = data.getLoginId();

                Employee.EmployeeRes.Builder respBuilder = Employee.EmployeeRes.newBuilder();
                respBuilder.setLoginId(id + 1);
                respBuilder.setSID(data.getSID());

                Employee.EmployeeInfo.Builder info = Employee.EmployeeInfo.newBuilder();
                info.setEmployeeId(1);
                info.setEmployeeName("eric");

                respBuilder.setEmployeelist(info);

                //channelHandlerContext.writeAndFlush(respBuilder.build());
                Server.logger.info("Server Response client command:");

                NettyChannelMap.getChannel(data.getLoginAccount()).writeAndFlush(respBuilder.build());
                break;
            case Command.LoginReq:
                Employee.LoginReq req = Employee.LoginReq.parseFrom(baseMsg.getBodyData());
                //新增到信道映射表
                ChannelMapObject channelMapObject = new ChannelMapObject((SocketChannel) channelHandlerContext.channel());
                //NettyChannelMap.add(req.getLoginAccount(), (SocketChannel) channelHandlerContext.channel());
                NettyChannelMap.add(req.getLoginAccount(), channelMapObject);


                Employee.LoginRes.Builder res = Employee.LoginRes.newBuilder();
                res.setLoginId(123);
                res.setSID(req.getSID());
                res.setLoginType(3);
                NettyChannelMap.getChannel(req.getLoginAccount()).writeAndFlush(res.build());

                System.out.println("Client " + req.getLoginAccount() + " login success");
                break;
            case Command.HeartBeatRea:

                Employee.HeartBeatRes.Builder heartRes = Employee.HeartBeatRes.newBuilder();
                heartRes.setSID("01234");
                heartRes.setLoginId(1234);

                Channel sc = NettyChannelMap.getChannel("erickong");
                sc.writeAndFlush(heartRes.build());
                System.out.println("Response heartbeat to " + sc.remoteAddress());

                break;
        }


        ReferenceCountUtil.release(baseMsg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        //cause.printStackTrace();//这行会打印客户端断开连接时的异常
        SocketChannel sc = (SocketChannel)ctx.channel();
        Server.logger.error("Client socket cut down,IP:" + sc.remoteAddress());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }


    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        ChannelMapObject object = NettyChannelMap.getMapObject((SocketChannel)ctx.channel());
        if (null != object){
            if (object.getLostHeartBeatCount() >= MAX_UN_REC_HEARTBEAT_TIME){
                NettyChannelMap.remove((SocketChannel)ctx.channel());
                System.out.println("Close channel couse by heart beat, IP:" + ctx.channel().remoteAddress());
            }
            else {
                //NettyChannelMap.remove((SocketChannel)ctx.channel());
                object.plusLostCount();
                System.out.println("Un receiv heartbeat request, count:" + object.getLostHeartBeatCount());
            }
        }

    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.err.println("---ALL_IDLE---");
    }
}
