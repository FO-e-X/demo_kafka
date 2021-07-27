/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

public interface Properties {

  public java.util.Properties getProducer(String bootstrapServer);
  public java.util.Properties getConsumer(String bootstrapServer, String groupId);

}
