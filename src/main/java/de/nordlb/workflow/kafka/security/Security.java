/*
 * Copyright © NORD/LB Norddeutsche Landesbank Girozentrale, Hannover - Alle Rechte vorbehalten -
 */

package de.nordlb.workflow.kafka.security;

public class Security {

  protected static final String SECURITY_PROTOCOL_SASL_SLL = "SASL_SSL";
  protected static final String SECURITY_SASL_SSL_MECHANISM_SHA512 = "SCRAM-SHA-512";

  protected static final String SECURITY_PROTOCOL_SASL_PLAINTEXT = "SASL_PLAINTEXT";
  protected static final String SECURITY_SASL_PLAIN_MECHANISM = "PLAIN";

  protected static final String SECURITY_SASL_JAAS_CONFIG = "org.apache.kafka.common.security.scram.ScramLoginModule "
                                                            +"required username=\"%s\" password=\"%s\";";
  protected static final String SECURITY_USERNAME = "admin";
  protected static final String SECURITY_PASSWORD = "admin-secret";
  
}
