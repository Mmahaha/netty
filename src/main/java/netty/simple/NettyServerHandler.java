package netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/*
自定义一个Handler需要继承netty规定好的某个HandlerAdapter
此时我们自定义的Handler才能称之为一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据事件（可以读取客户端发送的消息）
    /*
    ChannelHandlerContext：上下文对象，含有管道pipeline， 通道， 地址
    Object msg：就是客户端发送的数据 默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //使用taskQueue异步执行耗时任务
        //1.提交到该channel对应的NIOEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("heihei",CharsetUtil.UTF_8));
                }catch (Exception ex){
                    System.out.println("发生异常：" + ex.getMessage());
                }
            }
        });

        /*
        System.out.println("server ctx = " + ctx);
        //将msg转成ByteBuffer
        //此处的ByteBuf是Netty提供的，
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是" + buf.toString(StandardCharsets.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
         */
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是write+flush，将数据写入缓冲并刷新
        //一般将，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client~", CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
