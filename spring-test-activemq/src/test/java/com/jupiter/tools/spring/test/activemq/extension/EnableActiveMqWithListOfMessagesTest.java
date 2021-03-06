package com.jupiter.tools.spring.test.activemq.extension;

import com.jupiter.tools.spring.test.activemq.annotation.ExpectedMessages;
import com.jupiter.tools.spring.test.activemq.annotation.meta.EnableActiveMqTest;
import com.jupiter.tools.spring.test.activemq.extension.expected.Foo;
import org.junit.jupiter.api.Test;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * Created on 25.03.2019.
 *
 * @author Korovin Anatoliy
 */
@EnableActiveMqTest
public class EnableActiveMqWithListOfMessagesTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    @ExpectedMessages(queue = "test-queue", messagesFile = "/datasets/expected_messages.json")
    void testSendListOfMessages() {
        jmsTemplate.convertAndSend("test-queue", new Foo("123"));
        jmsTemplate.convertAndSend("test-queue", new Foo("456"));
        jmsTemplate.convertAndSend("test-queue", new Foo("789"));
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public Queue testQueue() {
            return new Queue("test-queue");
        }

        @Bean // Serialize message content to json using TextMessage
        public MessageConverter jacksonJmsMessageConverter() {
            MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
            converter.setTargetType(MessageType.TEXT);
            converter.setTypeIdPropertyName("_type");
            return converter;
        }
    }
}
