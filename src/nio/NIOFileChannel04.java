package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    //使用transferFrom完成快速拷贝
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("D:\\channel.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\copy2.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();
        FileChannel fileChannel02 = fileOutputStream.getChannel();
        fileChannel02.transferFrom(fileChannel01,0,fileChannel01.size());
        //关闭各个流
        fileChannel01.close();
        fileChannel02.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
