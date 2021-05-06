package netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    public int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        //服务端接收到信息如下
        System.out.println("长度=" + len);
        System.out.println("内容=" + new String(content, StandardCharsets.UTF_8));
        System.out.println("服务器接收消息的数量=" + (++count));

        //回复消息
        String message = "客户端你真笨";
        byte[] response = message.getBytes(StandardCharsets.UTF_8);
        int length = response.length;
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(response);
        ctx.writeAndFlush(messageProtocol);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
