package netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    public int count = 0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //连续发送10条数据
        for (int i = 0; i < 5; i++) {
            String message = "做毕设好开心啦啦啦" + i;
            byte[] content = message.getBytes(StandardCharsets.UTF_8);
            int length = message.getBytes(StandardCharsets.UTF_8).length;
            //创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        byte[] content = msg.getContent();
        int len = msg.getLen();
        System.out.println("服务端发来消息="+ new String(content, StandardCharsets.UTF_8));
        System.out.println("消息长度="+len);
        System.out.println("客户端接收消息的数量"+(++count)+"\n");
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常信息:"+cause.getMessage());
        ctx.close();
    }
}
