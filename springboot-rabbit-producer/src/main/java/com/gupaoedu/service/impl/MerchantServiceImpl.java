package com.gupaoedu.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gupaoedu.entity.Merchant;
import com.gupaoedu.mapper.MerchantMapper;
import com.gupaoedu.service.MerchantService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: qingshan
 * @Date: 2018/10/25 16:17
 * @Description: 咕泡学院，只为更好的你
 */
@Service
public class MerchantServiceImpl implements MerchantService,RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {


    @Value("${com.gupaoedu.topicexchange}")
    private String topicExchange;

    @Value("${com.gupaoedu.topicroutingkey1}")
    private String topicRoutingKey;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    RabbitTemplate gupaoTemplate;


    @Override
    public List<Merchant> getMerchantList(String name, int page, int limit) {
        return  merchantMapper.getMerchantList(name,page,limit);
    }

    @Override
    public Merchant getMerchantById(Integer id) {
        return merchantMapper.getMerchantById(id);
    }

    @Override
    public int add(Merchant merchant) {
        int k = merchantMapper.add(merchant);
        System.out.println("aaa : "+merchant.getId());
        JSONObject title = new JSONObject();
        String jsonBody = JSONObject.toJSONString(merchant);
        title.put("type","add");
        title.put("desc","新增商户");
        title.put("content",jsonBody);
        gupaoTemplate.setConfirmCallback(this);//发送确认
        gupaoTemplate.setReturnCallback(this);//消息确认
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("243432324");

        topicRoutingKey="112121";
//        gupaoTemplate.convertAndSend();
//        Message message = MessageBuilder.withBody(title.toJSONString().getBytes()).build();
//        gupaoTemplate.convertAndSend(topicExchange,topicRoutingKey,message,correlationData);
        gupaoTemplate.convertAndSend(topicExchange,topicRoutingKey, title.toJSONString(),correlationData);

        return k;
    }

    @Override
    public int updateState(Merchant merchant) {

        int k = merchantMapper.updateState(merchant);

        JSONObject title = new JSONObject();
        String jsonBody = JSONObject.toJSONString(merchant);
        title.put("type","state");
        title.put("desc","更新商户状态");
        title.put("content",jsonBody);
        gupaoTemplate.convertAndSend(topicExchange,topicRoutingKey, title.toJSONString());

        return k;
    }

    @Override
    public int update(Merchant merchant) {

        int k = merchantMapper.update(merchant);

        JSONObject title = new JSONObject();
        String jsonBody = JSONObject.toJSONString(merchant);
        title.put("type","update");
        title.put("desc","更新商户信息");
        title.put("content",jsonBody);
        gupaoTemplate.convertAndSend(topicExchange,topicRoutingKey, title.toJSONString());

        return k;
    }

    @Override
    public int delete(Integer id) {

        int k = merchantMapper.delete(id);

        JSONObject title = new JSONObject();
        Merchant merchant = new Merchant();
        merchant.setId(id);
        String jsonBody = JSONObject.toJSONString(merchant);
        title.put("type","delete");
        title.put("desc","删除商户");
        title.put("content",jsonBody);

        gupaoTemplate.convertAndSend(topicExchange,topicRoutingKey, title.toJSONString());

        return k;
    }

    @Override
    public int getMerchantCount() {

        return merchantMapper.getMerchantCount();
    }

    /**
     *消息从生产者到达exchange时返回ack，消息未到达exchange返回nack
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("=====已消费======");
        if(ack){
            System.out.println("消息: "+correlationData+"，已经被ack成功");
        }else{
            //可选择重试
            System.out.println("消息: "+correlationData+"，nack，失败原因是："+cause);
        }

    }

    /**
     * ReturnCallback接口用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调
     * @param message
     * @param i
     * @param s
     * @param s1
     * @param s2
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("sender return success" + message.toString());
    }


//    /**
//     * 指定 exchangeName  routingKeyName，发送payload
//     *
//     * @param exchangeName
//     * @param routingKeyName
//     * @param payload
//     * @throws JsonProcessingException
//     */
//    public void convertAndSend(String exchangeName, String routingKeyName, Object payload) throws JsonProcessingException {
//        try {
//            gupaoTemplate.setExchange(exchangeName);
//            gupaoTemplate.setRoutingKey(routingKeyName);
//
//
//            gupaoTemplate.convertAndSend(routingKeyName,message,correlationData);
//        } catch (Exception ex) {
//            gupaoTemplate.error("convertAndSend failed:{}", ex);
//        }
//    }

}
