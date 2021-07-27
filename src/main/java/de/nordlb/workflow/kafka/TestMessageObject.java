/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestMessageObject {

  public static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private String name = "";
  private int id = 0;
  private String created = LocalDateTime.now().format(DATETIMEFORMATTER);

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCreated() { return created;}

  /*public LocalDateTime getCreatedLocalDateTime() {
    return LocalDateTime.parse(created, DATETIMEFORMATTER);
  }*/

  public String toString() {
    return "name:"+name+" # id:"+String.valueOf(id)+" # created:"+created;
  }

}
