import com.github.brainlag.nsq.NSQProducer;
import com.github.brainlag.nsq.exceptions.NSQException;
import com.lsy.grpc.api.*;
import com.lsy.nsqServer.service.NsqServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * Created by lsy on 2019/3/22.
 */
public class NsqServer {

    private static final Logger logger = Logger.getLogger(NsqServer.class.getName());

    private static final int DEFAULT_PROT=50052;
    private int port;
    private Server server;

    public NsqServer(int port){
        this(port, ServerBuilder.forPort(port));
    }
    public NsqServer(int port, ServerBuilder<?> serverBuilder){
        this.port=port;
        server=serverBuilder.addService(NsqServiceGrpc.bindService(new NsqServiceImpl())).build();
    }

    private void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                NsqServer.this.stop();
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
        NsqServer server=new NsqServer(DEFAULT_PROT);
        server.start();
        server.blockUntilShutdown();
    }

}
