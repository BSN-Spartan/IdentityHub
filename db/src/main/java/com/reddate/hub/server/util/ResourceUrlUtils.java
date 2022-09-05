// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.util;

import java.util.UUID;

public class ResourceUrlUtils {

  public static final String PREFIX = "hub:udpn:";

  public static final String DID_PREFIX = "hub:udpn:";

  public static final String SPARE = "/";

  public static final String DID = "hub:udpn:";

  public static String getURL(String did, String id) {
    return PREFIX + did.substring(DID_PREFIX.length(), did.length()) + SPARE + id;
  }

  public static String getDid(String url) {
    return DID + url.substring(DID_PREFIX.length(), url.length());
  }

  public static boolean checkUrl(String url) {
    try {
      String[] did_resourceId = url.split("/");
      String uuid = did_resourceId[1];
      if (url.startsWith(DID_PREFIX)) {
        UUID.fromString(uuid).toString();
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
