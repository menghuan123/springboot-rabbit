package com.gupaoedu.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: qingshan
 * @Date: 2018/10/20 17:34
 * @Description: 咕泡学院，只为更好的你
 */
@Configuration
public class RabbitConfig {
//    /**
//     * 所有的消息发送都会转换成JSON格式发到交换机
//     * @param connectionFactory
//     * @return
//     */
    @Bean
    public RabbitTemplate gupaoTemplate(CachingConnectionFactory connectionFactory) {
        connectionFactory.setPublisherReturns(true);//当消息进入exchange未进入队列的时候会回调
        connectionFactory.setPublisherConfirms(true);//开启confrim确认
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
//@Bean
//public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//    factory.setConnectionFactory(connectionFactory);
//    factory.setMessageConverter(new Jackson2JsonMessageConverter());
//    //NONE（默认）：自动；AUTO：根据情况确认；MANUAL：手动确认
//    factory.setAutoStartup(true);
//    return factory;
//}
    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置连接工厂
     * @return
     */
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
//
//        connectionFactory.setHost("192.168.56.128");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("/");
//        connectionFactory.setUsername("roberto");
//        connectionFactory.setPassword("roberto");
//
//        connectionFactory.setAutomaticRecoveryEnabled(true);
//        connectionFactory.setNetworkRecoveryInterval(10000);
//        Map<String, Object> connectionFactoryPropertiesMap = new HashMap();
//        connectionFactoryPropertiesMap.put("principal", "RobertoHuang");
//        connectionFactoryPropertiesMap.put("description", "RGP订单系统V2.0");
//        connectionFactoryPropertiesMap.put("emailAddress", "RobertoHuang@foxmail.com");
//        connectionFactory.setClientProperties(connectionFactoryPropertiesMap);
//
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
//
//        // 将CachingConnectionFactory的PublisherConfirms设置为true
//        cachingConnectionFactory.setPublisherConfirms(true);
//        // 将CachingConnectionFactory的PublisherReturns设置为true
//        cachingConnectionFactory.setPublisherReturns(true);
//
//        return cachingConnectionFactory;
//    }


}
