package bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class BIOServer {
    public static void main(String[] args) throws Exception {
    //线程池机制
    //思路
    //1. 创建一个线程池
    //2. 如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true) {
            System.out.println(" 线程信息id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
            //这里始终会有一个主线程用来等待客户端的连接，当有一个新连接时创建一个新的线程用于后续交互，创建后主线程会到这里阻塞等待下一个连接
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            //创建新线程与之通讯
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
        }
    }
    //编写一个handler 方法，和客户端通讯
    public static void handler(Socket socket) {
        try {
            System.out.println(" 线程信息id =" + Thread.currentThread().getId() + " 名字=" +
                    Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
        //通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();
        //循环的读取客户端发送的数据
            while (true) {
                System.out.println(" 线程信息id =" + Thread.currentThread().getId() + " 名字=" +
                        Thread.currentThread().getName());
                System.out.println("read....");
                //读取也会阻塞该线程（用于交互的线程）
                int read = inputStream.read(bytes);
                if(read != -1) {
                    System.out.println(new String(bytes, 0, read
                    )); //输出客户端发送的数据
                } else {
                    break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client 的连接");
            try {
                socket.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}