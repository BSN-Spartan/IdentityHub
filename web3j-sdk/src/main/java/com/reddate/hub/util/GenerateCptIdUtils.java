// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

/** Get long unique ID */
public class GenerateCptIdUtils {

  public static long getId() {
    StringBuffer generateId = new StringBuffer();
    generateId
        .append(RandomUtil.randomString("123456789", 2))
        .append(DateUtil.format(DateUtil.date(), "yyMMddHHmmssSSS"))
        .append(RandomUtil.randomString("123456789", 1));
    return Long.parseLong(generateId.toString());
  }

  public static void main(String[] args) {

    System.out.println(getId());
    System.out.println(DateUtil.format(DateUtil.date(), "yyMMddHHmmssSSS"));
  }
}
