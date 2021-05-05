package netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {

    private int port;
    public GroupChatServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //获取pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        //向pipeline加入解码器
                        pipeline.addLast("decoder", new StringDecoder());
                        //向pipeline加入编码器
                        pipeline.addLast("encoder", new StringEncoder());
                        //自定义业务处理handler
                        pipeline.addLast(null);
                    }
                });
        System.out.println("Netty服务器启动");
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        //监听关闭
        channelFuture.channel().closeFuture().sync();
    }

    public static void main(String[] args) {
        new GroupChatServer(8989).run();
    }
}
