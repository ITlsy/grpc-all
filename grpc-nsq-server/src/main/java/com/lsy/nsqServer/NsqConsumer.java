package com.lsy.nsqServer;

import com.github.brainlag.nsq.NSQConsumer;
import com.github.brainlag.nsq.NSQMessage;
import com.github.brainlag.nsq.callbacks.NSQMessageCallback;
import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
import com.github.brainlag.nsq.lookup.NSQLookup;


/**
 * Created by lsy on 2019/3/22.
 */
public class NsqConsumer {

    public static void main(String[] args) {

        nsqConsumer();
    }

    public static void nsqConsumer(){
        NSQLookup lookup = new DefaultNSQLookup();
        lookup.addLookupAddress("127.0.0.1", 4161);
        NSQConsumer consumer = new NSQConsumer(lookup, "test-grpc", "nsq_to_file", new NSQMessageCallback(){
            @Override
            public void message(NSQMessage message) {
                byte b[] = message.getMessage();
                String s = new String(b);
                System.out.println(s);
                message.finished();
            }
        });
        consumer.start();
        //线程睡眠，让程序执行完
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
