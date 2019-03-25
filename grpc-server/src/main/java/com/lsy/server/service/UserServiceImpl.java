package com.lsy.server.service;

import com.github.brainlag.nsq.NSQProducer;
import com.github.brainlag.nsq.exceptions.NSQException;
import com.lsy.grpc.api.*;
import com.lsy.server.dao.UserMapper;
import com.lsy.server.util.MybatisUtil;
import io.grpc.stub.StreamObserver;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * Created by lsy on 2019/3/12.
 */

public class UserServiceImpl implements UserServiceGrpc.UserService {

    SqlSession session = MybatisUtil.getSqlSession(true);
    Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    public void addUser(User user, StreamObserver<ResponseModel> responseObserver) {

        try {
            UserMapper userMapper=session.getMapper(UserMapper.class);
            userMapper.save(user);
            ResponseModel responseModel=ResponseModel.newBuilder().setCode(true).setMsg("success").build();
            responseObserver.onNext(responseModel);
            responseObserver.onCompleted();
            logger.info("存入数据库");
        }catch (Exception e){
            ResponseModel responseModel=ResponseModel.newBuilder().setCode(false).setMsg("error").build();
            responseObserver.onNext(responseModel);
            responseObserver.onCompleted();
        }finally {
            session.close();
        }
    }

    public void getByName(NameRequest name, StreamObserver<Result> responseObserver) {
        try {
            UserMapper userMapper=session.getMapper(UserMapper.class);
            String phone=userMapper.getByName(name);
            Result result=Result.newBuilder().setCode(true).setPhone(phone).build();

            responseObserver.onNext(result);
            responseObserver.onCompleted();

            Map<String,Object> map=new HashMap<String, Object>();
            map.put("message",phone);
            NSQProducer producer = new NSQProducer();
            producer.addAddress("127.0.0.1",4150).start();
            try {
                String aa=map.get("message").toString();
                producer.produce("test", aa.getBytes());

            } catch (NSQException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            Result result=Result.newBuilder().setCode(false).build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();
        }finally {
            session.close();
        }
    }
}
