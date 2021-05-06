package netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyBuf02 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
        //使用相关的api
        if (byteBuf.hasArray()){
            byte[] content = byteBuf.array();


        }
    }
}
