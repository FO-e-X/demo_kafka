/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConsumerTest extends TestBase {

  @Before
  public void runBefore() {

  }
  
  @After
  public void runAfter() {
  
  }

  @Test
  public void runConsumerTest() throws Exception {
    Map<String, Object> results = runConsumerTest(Const.GROUPID, Const.TOPIC);
    if(results == null || results.isEmpty()) {
      throw new Exception("Beim Lesen der Messages sind Fehler aufgetreten!");
    }
  }
  
}
