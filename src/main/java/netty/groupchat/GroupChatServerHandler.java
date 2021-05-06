package netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义channel组,管理所有channel
    //GlobalEventExecutor.INSTANCE是全局事件执行器,是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    //表示channel处于非活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
    }

    //handlerAdded()表示连接建立时,立马会执行的方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //channelGroup的该方法会将所有的channel遍历并发送该消息,无需自己遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天\n");
        channelGroup.add(channel);
    }

    //该方法会自动从channelGroup中移除该channel
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        //遍历channelGroup发送,需要排除自己所以不要用上面的方法
        channelGroup.forEach(ch -> {
            if (channel!=ch){
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送了消息:"+msg + "\n" );
            }
        });
    }

    //发生异常,关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
