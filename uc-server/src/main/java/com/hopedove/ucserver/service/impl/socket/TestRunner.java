package com.hopedove.ucserver.service.impl.socket;

import com.hopedove.ucserver.service.ISocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author  wsj
 * @Description
 * @create 2019-04-14 23:40
 */
@Configuration
@Component
public class TestRunner implements CommandLineRunner {

   // @Autowired
   // private SocketServiceImpl socketService = SpringUtil.getBean(SocketServiceImpl.class);

    @Value("${socket.port}")
    private Integer port;

    @Value("${socket.pool-keep}")
    private Integer poolKeep;

    @Value("${socket.pool-core}")
    private Integer poolCore;

    @Value("${socket.pool-max}")
    private Integer poolMax;

    @Value("${socket.pool-queue-init}")
    private Integer poolQueueInit;

    @Override
    public void run(String... args) throws Exception {
       // System.out.println(socketService.getPoolMax());
        ServerSocket server = null;
        Socket socket = null;
        server = new ServerSocket(port);
        System.out.println("设备服务器已经开启, 监听端口:" + port);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                poolCore,
                poolMax,
                poolKeep,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(poolQueueInit),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        while (true) {
            socket = server.accept();
            pool.execute(new ServerConfig(socket));
        }
    }
}
