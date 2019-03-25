package com.lsy.nsqServer.dao;

import com.lsy.grpc.api.NsqRequest;

/**
 * Created by lsy on 2019/3/22.
 */
public interface MessageMapper {
    void add(NsqRequest message);
}
