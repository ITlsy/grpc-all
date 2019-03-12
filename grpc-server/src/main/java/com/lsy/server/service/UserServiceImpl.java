package com.lsy.server.service;

import com.lsy.grpc.api.ResponseModel;
import com.lsy.grpc.api.User;
import com.lsy.grpc.api.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by lsy on 2019/3/12.
 */
@Service
public class UserServiceImpl implements UserServiceGrpc.UserService {
    public void addUser(User user, StreamObserver<ResponseModel> responseObserver) {

        Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

        ResponseModel responseModel=ResponseModel.newBuilder().setCode(true).setMsg("success").build();
        responseObserver.onNext(responseModel);
        responseObserver.onCompleted();
        logger.info("存入数据库");


    }
}
