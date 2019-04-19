import sun.nio.ch.Net;

public class WSManagerMain {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start("localhost",8080);
        System.out.println("netty 启动成功");
    }
}
