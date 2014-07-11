package com.coderme.core.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;


public class ApmgtMessageConverter implements MessageConverter {

    private Log log = LogFactory.getLog(ApmgtMessageConverter.class);

    private SimpleMessageConverter converter;

    public void setConverter(SimpleMessageConverter converter) {
        this.converter = converter;
    }

    //fromMessage是用来把一个JMS Message转换成对应的Java对象
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (message instanceof ObjectMessage) {
            ObjectMessage o_message = (ObjectMessage)message;
            MessageHeader header = new MessageHeader();
            header.setId(message.getLongProperty("id"));
            header.setReceiver(message.getIntProperty("receiver"));
            header.setSender(message.getIntProperty("sender"));
            header.setSendPerson(message.getStringProperty("sendPerson"));
            header.setType(message.getIntProperty("type"));
            Serializable messageContent = o_message.getObject();
            ApmgtMessageData<Serializable> messageData = new ApmgtMessageData<Serializable>();
            messageData.setMessageContent(messageContent);
            messageData.setMessageHeader(header);
            return messageData;
        }
        return null;
    }

    //toMessage方法是用来把一个Java对象转换成对应的JMS Message
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        if (object instanceof ApmgtMessageData) {
            ApmgtMessageData data = (ApmgtMessageData) object;
            Message message = converter.toMessage(data.getMessageContent(), session);
            message.setLongProperty("id", data.getMessageHeader().getId());
            message.setIntProperty("receiver", data.getMessageHeader().getReceiver());
            message.setIntProperty("sender", data.getMessageHeader().getSender());
            message.setIntProperty("type", data.getMessageHeader().getType());
            message.setStringProperty("sendPerson", data.getMessageHeader().getSendPerson());
            log.info("发送消息[MessageSender]:\n" + message);
             return message;
        } else {
            return null;
        }
    }

}