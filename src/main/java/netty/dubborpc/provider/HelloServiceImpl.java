package netty.dubborpc.provider;

import netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    private int count = 0;
    //当有消费方调用该方法时，返回一个结果
    @Override
    public String hello(String mes) {
        System.out.println("收到客户端消息=" + mes);
        //根据mes返回不同结果
        if (mes!=null) {
            return "你好客户端,我已收到你的消息[" + mes + "] 第" + (++count) + "次";   //结果都是1,说明每次都会产生一个新的实例
        } else {
            return "你好客户端,我已收到你的消息";
        }
    }
}
