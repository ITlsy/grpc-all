package com.lsy.nsqServer.service;

import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.NSQMessage;
import com.github.brainlag.nsq.callbacks.NSQMessageCallback;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;
import com.lsy.grpc.api.NsqRequest;
import com.lsy.grpc.api.NsqResponse;
import com.lsy.grpc.api.NsqServiceGrpc;
import com.lsy.nsqServer.dao.MessageMapper;
import com.lsy.server.service.UserServiceImpl;
import com.lsy.server.util.MybatisUtil;
import io.grpc.stub.StreamObserver;
import org.apache.ibatis.session.SqlSession;

import java.util.logging.Logger;

/**
 * Created by lsy on 2019/3/22.
 */
public class NsqServiceImpl implements NsqServiceGrpc.NsqService {

    Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    SqlSession session = MybatisUtil.getSqlSession(true);
    @Override
    public void sendMessage(NsqRequest request, StreamObserver<NsqResponse> responseObserver) {
        System.out.println("哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈");
        NSQLookup lookup = new DefaultNSQLookup();
        lookup.addLookupAddress("127.0.0.1", 4161);
        NSQConsumer consumer = new NSQConsumer(lookup, "test", "nsq_to_file", new NSQMessageCallback() {
            StreamObserver<NsqResponse> responseObserver;
            @Override
            public void message(NSQMessage message) {
                byte b[] = message.getMessage();
                String content = new String(b);
                System.out.println(content);
                MessageMapper messageMapper=session.getMapper(MessageMapper.class);
                NsqRequest nsqRequest=NsqRequest.newBuilder()
                        .setContent(content).build();
                messageMapper.add(nsqRequest);
                NsqResponse response=NsqResponse.newBuilder().setCode(true).setMsg("success").build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                logger.info("存入数据库啦");
                message.finished();
            }
        });
        consumer.start();
        //线程睡眠，让程序执行完
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
