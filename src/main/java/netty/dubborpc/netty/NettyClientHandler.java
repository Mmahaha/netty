package netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;  //上下文
    private String result;  //返回的结果
    private String para;    //客户端调用方法时传入的参数 Hello##

    //被代理对象调用,发送数据给服务器,再wait()等待被唤醒
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("cal11 被调用");
        context.writeAndFlush(para);
        wait(); //在channelRead()获取到服务器返回的结果后被唤醒
        System.out.println("call2 被调用");
        return result;  //服务方返回的结果
    }

    //最先被调用的方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive 被调用");
        context = ctx;  //其他方法中会使用到该上下文
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 被调用");
        result = msg.toString();
        notify();   //唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    void setPara(String para){
        System.out.println("setPara 被调用");
        this.para = para;
    }
}
