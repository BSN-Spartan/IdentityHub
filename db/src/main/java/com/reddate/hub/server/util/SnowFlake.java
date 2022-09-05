// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.util;

/**
 * twitter snowflake java implement
 *
 * @author danny
 * @date 2016/11/26
 */
public class SnowFlake {

  /** Starting timestamp */
  private static final long START_STMP = 1480166465631L;

  /** Number of bits occupied by each part */
  private static final long SEQUENCE_BIT = 12;
  // Number of digits occupied by the machine ID
  private static final long MACHINE_BIT = 5;
  // Number of bits occupied by the data center
  private static final long DATACENTER_BIT = 5;

  /** Maximum value of each part */
  private static final long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);

  private static final long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
  private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

  /** Displacement of each part to the left */
  private static final long MACHINE_LEFT = SEQUENCE_BIT;

  private static final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
  private static final long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

  // data center
  private long datacenterId;
  // Machine identification
  private long machineId;
  // serial number
  private long sequence = 0L;
  // Last timestamp
  private long lastStmp = -1L;

  public SnowFlake(long datacenterId, long machineId) {
    if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
      throw new IllegalArgumentException(
          "datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
    }
    if (machineId > MAX_MACHINE_NUM || machineId < 0) {
      throw new IllegalArgumentException(
          "machineId can't be greater than MAX_MACHINE_NUM or less than 0");
    }
    this.datacenterId = datacenterId;
    this.machineId = machineId;
  }

  /**
   * Generate the next ID
   *
   * @return
   */
  public synchronized long nextId() {
    long currStmp = getNewstmp();
    if (currStmp < lastStmp) {
      throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
    }

    if (currStmp == lastStmp) {
      // Within the same millisecond, the serial number increases automatically
      sequence = (sequence + 1) & MAX_SEQUENCE;
      // The number of sequences in the same millisecond has reached the maximum
      if (sequence == 0L) {
        currStmp = getNextMill();
      }
    } else {
      // In different milliseconds, the serial number is set to 0
      sequence = 0L;
    }

    lastStmp = currStmp;

    return (currStmp - START_STMP) << TIMESTMP_LEFT
        | datacenterId << DATACENTER_LEFT
        | machineId << MACHINE_LEFT
        | sequence;
  }

  private long getNextMill() {
    long mill = getNewstmp();
    while (mill <= lastStmp) {
      mill = getNewstmp();
    }
    return mill;
  }

  private long getNewstmp() {
    return System.currentTimeMillis();
  }
}
