package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("D:\\channel.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\copy.txt");
        FileChannel channel01 = fileInputStream.getChannel();
        FileChannel channel02 = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            //注意一定要重置各标志位，否则limit=position的情况下无法正常读取
            byteBuffer.clear();
            int read = channel01.read(byteBuffer);
            if (read == -1) break;
            byteBuffer.flip();
            channel02.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
