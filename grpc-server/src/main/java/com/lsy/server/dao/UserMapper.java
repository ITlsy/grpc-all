package com.lsy.server.dao;

import com.lsy.grpc.api.NameRequest;
import com.lsy.grpc.api.User;

/**
 * Created by lsy on 2019/3/13.
 */
public interface UserMapper {
    void save(User user);

    String getByName(NameRequest name);

}
