/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

import com.alibaba.dcm.DnsCacheManipulator;
import com.fasterxml.jackson.core.JsonProcessingException;
import de.nordlb.workflow.kafka.security.SASL_PLAINTEXT_Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TestBase {
  
  protected final Logger logger = LoggerFactory.getLogger(Producer.class);
  protected Map<String, Object> results;

  protected void initDNSCacheManipulator() {
    //Chronos Server
    DnsCacheManipulator.setDnsCache("nlbbaowasve01.nlbapp.f-i-verbund.de", "127.0.0.1");
    DnsCacheManipulator.setDnsCache("nlbbaowasve02.nlbapp.f-i-verbund.de", "127.0.0.1");
    DnsCacheManipulator.setDnsCache("nlbbaowasve03.nlbapp.f-i-verbund.de", "127.0.0.1");
    //Kafka Server
    DnsCacheManipulator.setDnsCache("nlbkafappve05.nlbapp.f-i-verbund.de", "127.0.0.1");
    DnsCacheManipulator.setDnsCache("nlbkafappve06.nlbapp.f-i-verbund.de", "127.0.0.1");
    DnsCacheManipulator.setDnsCache("nlbkafappve07.nlbapp.f-i-verbund.de", "127.0.0.1");
  }

  private List<String> servers = new ArrayList<>();

  private void initLists() {
    servers.add(Const.SERVER_BROKER_PRIMAER);
    for(int i=0; i<Const.SERVER_ATLERNATIVES.length; i++) {
      servers.add(Const.SERVER_ATLERNATIVES[i]);
    }
  }

  //Consumer

  private boolean startConsumer(String server, String groupId, String topic) {
    Consumer consumer = new Consumer(server, groupId, topic);
    consumer.setProperties(new SASL_PLAINTEXT_Properties());
    logger.info("Frage Verbindung ab zu "+server+". ("+ LocalDateTime.now().format(
        DateTimeFormatter.ISO_DATE_TIME) +") Topic: "+ groupId + " * "+topic);
    consumer.run(Const.UNIT_TEST_LOOPS);
    results = consumer.getResults();
    if(results.isEmpty()) {
      logger.error("Es waren keine Nachrichten auf dem Server vorhanden! ("+server+")");
      return false;
    }
    return true;
  }

  public Map<String, Object> runConsumerTest(String groupId, String topic) throws Exception {
    initLists();
    for(String server: servers) {
      if(startConsumer(server, groupId, topic)) {
        break;
      }
    }
    return results;
  }

  //Producer

  private boolean putMessage(Producer producer, Map<String, Map<String, Object>> messages)
      throws ExecutionException, InterruptedException, JsonProcessingException {
    if(messages == null) {
      return false;
    }
    for(Entry<String, Map<String, Object>> topicEntry: messages.entrySet()) {
      String topic = topicEntry.getKey();
      Map<String, Object> msgs = topicEntry.getValue();
      for(Entry<String, Object> msgEntry: msgs.entrySet()) {
        String key = msgEntry.getKey();
        Object message = msgEntry.getValue();
        producer.put(topic, key, message);
      }
    }
    return true;
  }

  private boolean setTopic(String server, Map<String, Map<String, Object>> messages) {
    try {
      Producer producer = new Producer(server);
      producer.setProperties(new SASL_PLAINTEXT_Properties());
      logger.info("Baue Verbindung auf zu "+server+". ("+ LocalDateTime.now().format(
          DateTimeFormatter.ISO_DATE_TIME) +")");
      producer.connect();
      putMessage(producer, messages);
      producer.close();

    } catch (ExecutionException | InterruptedException | JsonProcessingException ex) {
      logger.error("Verbindung zu "+server+" fehlgeschlagen. ("+ LocalDateTime.now().format(
          DateTimeFormatter.ISO_DATE_TIME) +")", ex);
      return false;
    }
    return true;
  }

  public void runProducerTest(Map<String, Map<String, Object>> messages) throws Exception {
    initLists();
    boolean result = false;
    for (String server : servers) {
      result = setTopic(server, messages);
      if(result) {
        break;
      }
    }
    if(!result) {
      throw new Exception("Es konnte keine Verbindung zu einem der verfügbaren Server hergestellt werden.");
    } else {
      logger.info("Nachricht erfolgreich übermittelt.");
    }
  }
  
}
