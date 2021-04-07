package nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 8899;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            //循环监听
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("client detected! " + socketChannel.getRemoteAddress().toString().substring(1));
                        }
                        if (selectionKey.isReadable()) {
                            //TODO
                            readData(selectionKey);
                        }
                        keyIterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readData(SelectionKey selectionKey) throws Exception{
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            if (read > 0) {
                String msg = new String(buffer.array());
                //打印消息并转发至其他客户端
                System.out.println(msg);
                sendToOtherClients(msg, selectionKey);
            }
        } catch (IOException e) {
            System.out.println(channel.getRemoteAddress().toString().substring(1) + " quit the chatroom");
            selectionKey.cancel();
            channel.close();
        }
    }

    public void sendToOtherClients(String msg, SelectionKey self){
        Set<SelectionKey> keys = selector.keys();
        try {
            for(SelectionKey key: keys){
                if (key.channel() instanceof SocketChannel && key != self){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                    channel.write(byteBuffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
