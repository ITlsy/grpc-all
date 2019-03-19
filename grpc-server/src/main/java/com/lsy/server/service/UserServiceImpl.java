package com.lsy.server.service;

import com.lsy.grpc.api.*;
import com.lsy.server.dao.UserMapper;
import com.lsy.server.util.MybatisUtil;
import io.grpc.stub.StreamObserver;
import org.apache.ibatis.session.SqlSession;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by lsy on 2019/3/12.
 */
@Service
public class UserServiceImpl implements UserServiceGrpc.UserService {

    SqlSession session = MybatisUtil.getSqlSession(true);

    public void addUser(User user, StreamObserver<ResponseModel> responseObserver) {

        Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

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
        }catch (Exception e){
            Result result=Result.newBuilder().setCode(false).build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();
        }finally {
            session.close();
        }
    }
}
