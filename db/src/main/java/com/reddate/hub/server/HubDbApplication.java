// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.reddate.hub.server.config.HubApplicationStartingEventListener;

@SpringBootApplication
public class HubDbApplication {

  /*
   *
   * java -jar udpn.hub-0.0.1-SNAPSHOT.jar --spring.config.location=D:/JavaSoft/application.properties --pwd=123456789
   *
   *encpryt private key by AES
   *
   *
   *
   */
  public static void main(String[] args) throws Exception {
    SpringApplication springApplication = new SpringApplication(HubDbApplication.class);
    springApplication.addListeners(new HubApplicationStartingEventListener());
    springApplication.run(args);
  }
}
