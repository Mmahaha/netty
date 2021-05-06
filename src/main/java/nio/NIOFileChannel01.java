package nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{
        String str = "hello world";
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\channel.txt");
        //Channel是基于Stream的
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes(StandardCharsets.UTF_8));
        //注意读写切换需要进行翻转
        buffer.flip();
        channel.write(buffer);
        fileOutputStream.close();
    }

}
