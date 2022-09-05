// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.constant;

public enum CryptoType {

  ECDSA;
//  SM;

  public static CryptoType ofVlaue(Integer value) {
    for (CryptoType tmp : CryptoType.values()) {
      if (tmp.ordinal() == value) {
        return tmp;
      }
    }
    return null;
  }
}
