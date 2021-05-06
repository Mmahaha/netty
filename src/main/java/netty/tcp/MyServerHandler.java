package netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public int count = 0;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        //buffer转字符串
        String s = new String(buffer, StandardCharsets.UTF_8);
        System.out.println("服务器收到消息:" + s);
        System.out.println("服务器收到的消息量:" + (++this.count));

        //服务器回送数据给客户端,回送一个随机id
        ByteBuf response = Unpooled.copiedBuffer(UUID.randomUUID().toString(), StandardCharsets.UTF_8);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
