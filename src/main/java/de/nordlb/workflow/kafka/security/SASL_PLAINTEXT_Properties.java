/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka.security;

import de.nordlb.workflow.kafka.Const;
import de.nordlb.workflow.kafka.Properties;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class SASL_PLAINTEXT_Properties extends Security implements Properties {

  @Override
  public java.util.Properties getProducer(String bootstrapServer) {
    java.util.Properties properties = producerProperties(bootstrapServer);
    properties.setProperty(ProducerConfig.RETRIES_CONFIG, "1");

    String serializer = StringSerializer.class.getName();
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializer);
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer);

    return properties;
  }

  @Override
  public java.util.Properties getConsumer(String bootstrapServer, String groupId) {
    java.util.Properties properties = producerProperties(bootstrapServer);
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");//latest

    String deserializer = StringDeserializer.class.getName();
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deserializer);
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

    return properties;
  }

  private java.util.Properties producerProperties(String bootstrapServer) {
    java.util.Properties properties = new java.util.Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer+":"+Const.PORT_PLAIN_TEXT);

    properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SECURITY_PROTOCOL_SASL_PLAINTEXT);
    properties.setProperty(SaslConfigs.SASL_MECHANISM, SECURITY_SASL_PLAIN_MECHANISM);

    String saslJaasConfig = String.format(SECURITY_SASL_JAAS_CONFIG, SECURITY_USERNAME, SECURITY_PASSWORD);
    properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);

    return properties;
  }

}
