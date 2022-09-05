// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.param.req;

public enum UsedFlag {
  YES,
  NO;

  public static UsedFlag ofValue(Integer val) {
    for (UsedFlag tmp : values()) {
      if (tmp.ordinal() == val) {
        return tmp;
      }
    }
    return null;
  }
}
