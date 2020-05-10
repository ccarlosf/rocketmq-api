package com.ccarlos.rocketmq.quickstart;

import com.ccarlos.rocketmq.constants.Const;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

public class Producer {

    public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {

        DefaultMQProducer producer = new DefaultMQProducer
                ("test_quick_producer_name");

//		producer.setNamesrvAddr(Const.NAMESRV_ADDR_SINGLE);

        producer.setNamesrvAddr(Const.NAMESRV_ADDR_MASTER_SLAVE);

        // 手动设置超时时间 非常重要
        producer.setSendMsgTimeout(10000);

        producer.start();

        for (int i = 0; i < 5; i++) {
            //	1.	创建消息
            Message message = new Message("test_quick_topic",    //	主题
                    "TagA", //	标签
                    "key" + i,    // 	用户自定义的key ,唯一的标识
                    ("Hello RocketMQ" + i).getBytes());    //	消息内容实体（byte[]）

            // 设置延迟时间
		/*	if(i == 1) {
				message.setDelayTimeLevel(3);
			}*/

            SendResult sr = producer.send(message, new MessageQueueSelector() {

                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer queueNumber = (Integer) arg;
                    return mqs.get(queueNumber);
                }
            }, 2);
            System.err.println(sr);

            // 2.1 同步发送消息
//            SendResult sr = producer.send(message);
//            SendStatus status = sr.getSendStatus();
//            System.err.println(status);
//            System.err.println("消息发出: " + sr);

            // 2.2 异步发送消息
         /*   producer.send(message, new SendCallback() {
                //rabbitmq急速入门的实战: 可靠性消息投递
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.err.println("msgId: " + sendResult.getMsgId() + ", status: " + sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                    System.err.println("------发送失败");
                }
            });*/
        }
        producer.shutdown();
    }
}
