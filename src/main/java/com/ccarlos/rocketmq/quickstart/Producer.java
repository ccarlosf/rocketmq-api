package com.ccarlos.rocketmq.quickstart;

import com.ccarlos.rocketmq.constants.Const;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

public class Producer {

	public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {

		DefaultMQProducer producer = new DefaultMQProducer("test_quick_producer_name");

		producer.setNamesrvAddr(Const.NAMESRV_ADDR_SINGLE);

		// 手动设置超时时间
		producer.setSendMsgTimeout(10000);

		producer.start();

		for(int i = 0 ; i <5000; i ++) {
			//	1.	创建消息
			Message message = new Message("test_quick_topic",    //	主题
					"TagA", //	标签
					"key" + i,    // 	用户自定义的key ,唯一的标识
					("Hello RocketMQ" + i).getBytes());    //	消息内容实体（byte[]）

			SendResult sr = producer.send(message);
			SendStatus status = sr.getSendStatus();
			System.err.println(status);
			System.err.println("消息发出: " + sr);
		}
		producer.shutdown();

	}
}
