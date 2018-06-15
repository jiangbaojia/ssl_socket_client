package example.ssl.codes.netty;

import example.ssl.codes.auth.ServerAuth;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;

/**
 * Created by Raymond on 2/9/2016.
 */
public class Server {
    final static int PORT = 8888;
    static Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        SSLContext sslContext = ServerAuth.getSSLContext();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{

            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.TCP_NODELAY, true);
            b.option(ChannelOption.SO_KEEPALIVE,true);

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializerByteBuf(sslContext));

            b.bind(PORT).sync().channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
