package netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            //向管道加入处理器

            //得到管道
            ChannelPipeline pipeline = ch.pipeline();
            //加入netty提供的httpServerCodec,是netty提供的http编解码器
            pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
            pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
        }
    }
