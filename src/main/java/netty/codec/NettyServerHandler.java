package netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

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
        //读取客户端发送的StudentPOJO.Student
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        System.out.println("客户端发送的数据: id=" + student.getId() + "name=" + student.getName());
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
