package com.lsy.client;

import com.lsy.grpc.api.ResponseModel;
import com.lsy.grpc.api.User;
import com.lsy.grpc.api.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by lsy on 2019/3/12.
 */
public class UserClient {

    private static final Logger logger = Logger.getLogger(UserClient.class.getName());
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 50051;

    private final ManagedChannel channel;

    private UserServiceGrpc.UserServiceBlockingStub userService;

    public UserClient(String host,int port){
        channel= ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();

        userService=UserServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException{
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private ResponseModel invokeWay(String name, String phone, int age) {
        User user=User.newBuilder()
                .setName(name)
                .setPhone(phone)
                .setAge(age)
                .build();
        ResponseModel responseModel=userService.addUser(user);
        return responseModel;
    }

    public static void main(String[] args) throws InterruptedException {
        UserClient client=new UserClient(DEFAULT_HOST,DEFAULT_PORT);
        ResponseModel responseModel=client.invokeWay("lsy","15978723505",20);
        System.out.println(responseModel);
        if(responseModel.getCode()){
            System.out.println("保存成功");
        }else {
            System.out.println("保存失败");
        }
        client.shutdown();
    }
}
