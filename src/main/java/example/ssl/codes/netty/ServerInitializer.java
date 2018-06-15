package example.ssl.codes.netty;

import example.ssl.codes.protobuf.Employee;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Created by Raymond on 2/9/2016.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SSLContext sslContext;
    public ServerInitializer(final  SSLContext sslContext){
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        final SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);

        ChannelPipeline p = socketChannel.pipeline();

        p.addLast("sslHandler", new SslHandler(sslEngine));
        //p.addLast(new ProtobufVarint32FrameDecoder());
        p.addLast(new ProtobufDecoder(Employee.EmployeeReq.getDefaultInstance()));
        //p.addLast(new ProtobufVarint32LengthFieldPrepender());
        p.addLast(new ProtobufEncoder());
        p.addLast(new ServerHandler());
    }
}
