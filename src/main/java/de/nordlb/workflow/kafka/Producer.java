/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.nordlb.workflow.kafka.security.SASL_SSL_Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {

  private Properties properties = new SASL_SSL_Properties();
  private String bootstrapServer = "";
  private Logger logger = LoggerFactory.getLogger(Producer.class);
  
  private KafkaProducer<String, String> producer;
  
  public Producer(String bootstrapServer) {
    this.bootstrapServer = bootstrapServer;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public void connect() {
    java.util.Properties properties = this.properties.getProducer(this.bootstrapServer);
    this.producer = new KafkaProducer<>(properties);
    logger.info("Producer re-initialized for \""+bootstrapServer+"\" ("+properties.getProperty(
                   CommonClientConfigs.SECURITY_PROTOCOL_CONFIG)+")");
  }
  
  public void put(String topic, String key, Object value)
      throws ExecutionException, InterruptedException, JsonProcessingException {
    logger.info("Put value ("+value.toString()+") for key ("+key+") in topic ("+topic+").");
    ObjectMapper mapper = new ObjectMapper();
    ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, mapper.writeValueAsString(value));
    producer.send(record, (recordMetadata, e) -> {
      if(e != null) {
        logger.error("Error while producing event", e);
        return;
      }
      logger.info("Received new meta.\n"+
                  "topic: "+recordMetadata.topic()+"\n"+
                  "partition: "+recordMetadata.partition()+"\n"+
                  "offset: "+recordMetadata.offset()+"\n"+
                  "timestamp: "+recordMetadata.timestamp());
    }).get();//TODO: remove for prod
  }
  
  public void close() {
    logger.info("Closing connection...");
    producer.close();
  }
  

  
}
