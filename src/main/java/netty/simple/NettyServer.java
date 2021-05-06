package netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws Exception{
        /*
         1.创建bossGroup和workerGroup
         2.bossGroup只处理连接请求，workerGroup负责客户端业务处理
         3.两个都是无线循环（轮询）
         4.bossGroup和workerGroup含有的子线程（NioEventLoop）的个数默认为cpu总线程数*2
         */

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建服务器端的启动对象，配置参数
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程进行设置
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                .channel(NioServerSocketChannel.class)    //使用NioSocketChannel作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG, 128)  //设置线程队列等待连接的个数
                .childOption(ChannelOption.SO_KEEPALIVE, true)  //设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道初始化对象（匿名对象）
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                }); //给workerGroup的EventLoop对应的管道设置处理器

            System.out.println("...server is ready...");

            //绑定一个端口并且同步，生成了一个ChannelFuture对象
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            //对关闭通道进行监听（不是马上关闭）
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
