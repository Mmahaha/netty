package netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    /*
    ByteBuf对象包含一个数组arr,是byte[10]
    ByteBuf读写切换时不需要进行flip操作
    ByteBuf底层维护了readerIndex和writerIndex,和capacity一起将buffer分成三个区域
        0--readerIndex  表示已读的区域
        readerIndex--writerIndex    表示可读的区域
        writerIndex--capacity   表示可写的区域
     */
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);   //每执行一次,writerIndex会++
        }

        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.readByte()); //每执行一次,readerIndex会++
        }
    }

}
