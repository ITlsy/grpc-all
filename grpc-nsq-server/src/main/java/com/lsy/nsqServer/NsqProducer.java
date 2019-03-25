package com.lsy.nsqServer;

import com.github.brainlag.nsq.NSQProducer;
import com.github.brainlag.nsq.exceptions.NSQException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by lsy on 2019/3/22.
 */
public class NsqProducer {
    public static void main(String[] args) throws UnsupportedEncodingException {
        nsqProducer();
    }

    public static void nsqProducer() throws UnsupportedEncodingException {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("message","你好啊");
        NSQProducer producer = new NSQProducer();
        producer.addAddress("127.0.0.1",4150).start();
        try {
            String aa=map.get("message").toString();
            producer.produce("test-grpc", aa.getBytes());
        } catch (NSQException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
