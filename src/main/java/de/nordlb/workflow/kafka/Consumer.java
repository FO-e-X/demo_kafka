/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.nordlb.workflow.kafka.security.SASL_SSL_Properties;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

  private Properties properties = new SASL_SSL_Properties();
  
  private Logger logger = LoggerFactory.getLogger(Consumer.class.getName());
  
  private String bootstrapServer;
  private String groupId;
  private String topic;

  private static Map<String, Object> results = new LinkedHashMap<>();
  
  public Consumer(String bootstrapServer, String groupId, String topic) {
    this.bootstrapServer = bootstrapServer;
    this.groupId = groupId;
    this.topic = topic;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }
  
  public void run(int loopCount) {
    results.clear();
    logger.info("Creating consumer thread...");
    CountDownLatch latch = new CountDownLatch(1);
    ConsumerRunnable consumerRunnable = new ConsumerRunnable(bootstrapServer, groupId, topic, latch, loopCount);
    Thread thread = new Thread(consumerRunnable);
    thread.start();
    
    Runtime.getRuntime().addShutdownHook(new Thread(()-> {
      logger.info("Caught shutdown hook");
      consumerRunnable.shutdown();
      await(latch);
      logger.info("Application has exited");
    }));
    
    await(latch);
  }

  public static Map<String, Object> getResults() {
    return results;
  }
  
  private void await(CountDownLatch latch) {
    try {
      latch.await();
    } catch (InterruptedException iex) {
      logger.error("Application got interrupted", iex);
    } finally {
      logger.info("Application is closing...");
    }
  }
  
  //Inner class for consumer in own thread
  
  private class ConsumerRunnable implements Runnable {
    private CountDownLatch latch;
    private KafkaConsumer<String, Object> consumer;
    private int loopCount = 0;
    
    public ConsumerRunnable(String bootstrapServer, String groupId, String topic, CountDownLatch latch, int loopCount) {
      this.latch = latch;

      this.loopCount = (loopCount>0)?loopCount:0;
      consumer = new KafkaConsumer<>(properties.getConsumer(bootstrapServer, groupId));
      consumer.subscribe(Collections.singletonList(topic));
    }

    @Override
    public void run() {
      try{
        int i=0;
        ObjectMapper mapper = new ObjectMapper();
        while(loopCount == 0 || i<=loopCount) {
          ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(Const.POLLING_MILLIS));
          for(ConsumerRecord<String, Object> record : records) {
            logger.info("key: "+record.key()+", value: "+record.value());
            logger.info("partition: "+record.partition()+", offset: "+record.offset());
            Consumer.getResults().put(record.key(), mapper.readValue((String) record.value(), Object.class));
            //Consumer.getResults().put(record.key(), mapper.readValue((String) record.value(), TestMessageObject.class));
            //Consumer.getResults().put(record.key(), record.value());
          }
          if(!Consumer.getResults().isEmpty()) {
            break;
          }
          i++;
        }
      } catch (WakeupException wex) {
        logger.info("Received shutdown signal!");
      } catch (JsonProcessingException e) {
        logger.error("Error processing JSON value", e);
      } finally {
        consumer.close();
        latch.countDown();
      }
    }
    
    public void shutdown() {
      consumer.wakeup();
    }

  }
  
}
