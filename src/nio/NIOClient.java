package nio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress socketAddress = new InetSocketAddress("localhost",6666);
        if(!socketChannel.connect(socketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("由于channel设置为非阻塞，所以在连接完成前可以做其他事情");
            }
        }
        String str = "hello world";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer);
        System.in.read();

    }
}
