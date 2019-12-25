package com.gupaoedu.confirm;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

/**
 * @author caowu
 * @version 1.0
 * @date 2019/11/25 15:10
 */
public class ConfirCallBack implements  RabbitTemplate.ConfirmCallback{

    /**
     * rabbitmq producer开启confirm后的消息确认
     * @param correlationData
     * @param b
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println("confirm: " + correlationData.getId() + ",s=" + s + ",b:" + b);
    }
}
