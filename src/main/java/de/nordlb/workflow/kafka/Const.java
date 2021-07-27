/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka;

public class Const {
  
  //public static final String SERVER = "127.0.0.1";//Localhost
  //public static final String SERVER = "2.106.41.73";//NIT TENT (noch ausgeliehen und nicht final)
  //public static final String PORT = "9092";
  //2.109.60.51
  //2.109.60.52
  //2.109.60.53
  //6667
  //public static final String SERVER_BROKER_PRIMAER = "2.109.0.249";//NIT TENT Broker
  //public static final String SERVER_BROKER_ALTERNATIVE_1 = "2.109.0.250";//NIT TENT Broker
  //public static final String SERVER_BROKER_ALTERNATIVE_2 = "2.109.0.248";//NIT TENT Broker
  public static final String SERVER_BROKER_PRIMAER = "nlbkafappve05.nlbapp.f-i-verbund.de";//NIT TENT Broker
  public static final String SERVER_BROKER_ALTERNATIVE_1 = "nlbkafappve06.nlbapp.f-i-verbund.de";//NIT TENT Broker
  public static final String SERVER_BROKER_ALTERNATIVE_2 = "nlbkafappve07.nlbapp.f-i-verbund.de";//NIT TENT Broker
  //public static final String SERVER_ALTERNATIVE_2 = "2.109.0.244";//NIT TENT
  //public static final String SERVER_ALTERNATIVE_3 = "2.109.0.245";//NIT TENT
  //public static final String SERVER_ALTERNATIVE_4 = "2.109.0.246";//NIT TENT
  public static final String SERVER_ATLERNATIVES[] = new String[] {SERVER_BROKER_ALTERNATIVE_1,
                                                                   SERVER_BROKER_ALTERNATIVE_2};
  //public static final String PORT_PRIMAER = "6667";
  public static final String PORT_PRIMAER = "9091";// Producer/Consumer
  public static final String PORT_PLAIN_TEXT = "9094";// PlainText -> ohne SSL Security
  //public static final String PORT_ALTERNATIVE_1 = "9095";// unbekannte Funktion
  //public static final String PORT_ALTERNATIVE_2 = "9092";
  //public static final String PORT_ALTERNATIVE_3 = "8081";
  //public static final String PORT_ALTERNATIVE_4 = "8082";
  //public static final String PORT_ALTERNATIVE_5 = "8083";
  public static final String PORT_ALTERNATIVES[] = new String[] {PORT_PLAIN_TEXT};
  public static final String TOPIC = "workflow.nordlb.test";
  public static final String GROUPID = "test_group";
  public static final int POLLING_MILLIS = 100;
  public static final int UNIT_TEST_LOOPS = 100;//600 Loops * 100ms = 1min
  
}
