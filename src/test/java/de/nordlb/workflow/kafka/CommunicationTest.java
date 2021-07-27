/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommunicationTest extends TestBase {
  
  @Before
  public void runBefore() {

  }
  
  @After
  public void runAfter() {
  
  }

  private void runProducerTest() throws Exception {
    Map<String, Map<String, Object>> messages = new HashMap<>();
    Map<String, Object> msgs1 = new HashMap<>();
    msgs1.put("message", "Hallo Welt!");
    msgs1.put("test", "testing...");
    messages.put(Const.TOPIC, msgs1);
    runProducerTest(messages);
  }

  private void runConsumerTest() throws Exception {
    Map<String, Object> results = runConsumerTest(Const.GROUPID, Const.TOPIC);
    if(results == null || results.isEmpty()) {
      throw new Exception("Beim Lesen der Messages sind Fehler aufgetreten!");
    }
  }

  @Test
  public void runCommunicationTest() throws Exception {
    //define message
    String key = "TestKey";
    String message = "TestMessage";
    //send message
    Map<String, Map<String, Object>> messages = new HashMap<>();
    Map<String, Object> msgs1 = new HashMap<>();
    msgs1.put(key, message);
    messages.put(Const.TOPIC, msgs1);
    runProducerTest(messages);
    //read message
    Map<String, Object> results = runConsumerTest(Const.GROUPID, Const.TOPIC);
    if(results == null || results.isEmpty()) {
      throw new Exception("Beim Lesen der Messages sind Fehler aufgetreten!");
    }
    //compare message
    boolean found = results.entrySet().stream()
                                      .filter(entry -> key.equals(entry.getKey()))
                                      .anyMatch(entry -> message.equals(entry.getValue()));
    if(!found) {
      throw new Exception("Die vorher übermittelten Daten konnten nicht wieder ausgelesen werden!");
    }
    logger.info("Testdaten ("+key+"#"+message+") auf Server gefunden!");
  }

  @Test
  public void runCommunicationObjectTest() throws Exception {
    //define message
    String key = "TestKey";
    TestMessageObject message = new TestMessageObject();
    message.setName("TestMessage-"+UUID.randomUUID().toString());
    message.setId((new Random()).nextInt(16));
    //send message
    Map<String, Map<String, Object>> messages = new HashMap<>();
    Map<String, Object> msgs1 = new HashMap<>();
    msgs1.put(key, message);
    messages.put(Const.TOPIC, msgs1);
    runProducerTest(messages);
    //read message
    Map<String, Object> results = runConsumerTest(Const.GROUPID, Const.TOPIC);
    if(results == null || results.isEmpty()) {
      throw new Exception("Beim Lesen der Messages sind Fehler aufgetreten!");
    }
    //compare message
    boolean found = results.entrySet().stream()
        .filter(entry -> key.equals(entry.getKey()))
        .anyMatch(entry -> message.toString().equals(((TestMessageObject) entry.getValue()).toString()));
    if(!found) {
      throw new Exception("Die vorher übermittelten Daten konnten nicht wieder ausgelesen werden!");
    }
    logger.info("Testdaten ("+key+"#"+message.toString()+") auf Server gefunden!");
  }

}
