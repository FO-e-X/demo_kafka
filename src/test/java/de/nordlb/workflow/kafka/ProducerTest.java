/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProducerTest extends TestBase {

  @Before
  public void runBefore() {

  }
  
  @After
  public void runAfter() {
  
  }

  @Test
  public void runProducerTest() throws Exception {
    Map<String, Map<String, Object>> messages = new HashMap<>();
    Map<String, Object> msgs1 = new HashMap<>();
    msgs1.put("message", "Hallo Welt!");
    msgs1.put("test", "testing...");
    messages.put(Const.TOPIC, msgs1);
    runProducerTest(messages);
  }
}
