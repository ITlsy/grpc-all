package com.lsy.server;

import com.lsy.grpc.api.UserServiceGrpc;
import com.lsy.server.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by lsy on 2019/3/12.
 */
public class UserServer {

    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    private static final int DEFAULT_PROT=50051;
    private int port;
    private Server server;

    public UserServer(int port){
        this(port,ServerBuilder.forPort(port));
    }
    public UserServer(int port, ServerBuilder<?> serverBuilder){
        this.port=port;
        server=serverBuilder.addService(UserServiceGrpc.bindService(new UserServiceImpl())).build();
    }

    private void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                UserServer.this.stop();
            }
        });
    }

    private void stop() {
        if(server!=null){
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException{
        if(server!=null){
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        UserServer server=new UserServer(DEFAULT_PROT);
        server.start();
        server.blockUntilShutdown();
    }
}
