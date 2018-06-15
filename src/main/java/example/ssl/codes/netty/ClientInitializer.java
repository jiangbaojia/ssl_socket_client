package example.ssl.codes.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Created by Raymond on 2/9/2016.
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SSLContext sslContext;

    public ClientInitializer(final SSLContext sslContext){
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        final SSLEngine sslEngine = sslContext.createSSLEngine("localhost", 8888);

        ChannelPipeline p = socketChannel.pipeline();
        sslEngine.setUseClientMode(true);
        p.addLast("sslHandler", new SslHandler(sslEngine));
        //p.addLast(new ProtobufVarint32FrameDecoder());
        //p.addLast(new ProtobufDecoder(Employee.EmployeeRes.getDefaultInstance()));
        //p.addLast(new ProtobufVarint32LengthFieldPrepender());
        //p.addLast(new ProtobufEncoder());
        p.addLast("decoder", new HeaderDecoder());
        p.addLast("encoder", new HeaderEncoder());


        p.addLast(new ClientHandler());
    }
}
