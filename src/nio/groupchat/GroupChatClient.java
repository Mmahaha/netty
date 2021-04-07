package nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    private static final String HOST = "localhost";
    private static final int PORT = 8899;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws Exception{
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "is ok..");
    }

    public void sendInfo(String info){
        info = username + ":" + info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //读取服务端消息
    public void readInfo(){
        try {
            int read = selector.select();
            if (read > 0){
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while(keyIterator.hasNext()){
                    SelectionKey selectionKey = keyIterator.next();
                    keyIterator.remove();   //防止重复操作
                    if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        System.out.println(new String(buffer.array()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread(){
            public void run(){
                while(true){
                    groupChatClient.readInfo();
                }

            }
        }.start();


        //发送数据
        try {
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                groupChatClient.sendInfo(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
