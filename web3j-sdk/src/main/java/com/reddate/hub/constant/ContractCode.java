// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.constant;

public enum ContractCode {
  SUCCESS("0000", "Success"),
  UNKNOWN_ERROR("9999", "Unknow exception"),
  CPT_NOT_EXIST("2001", "cpt not exist"),
  CPT_ALREADY_EXIST("2002", "cpt already exist"),
  CREDLIST_NOT_EXIST("2003", "credList not exist"),
  CREDID_NOT_EXIST("2004", "creDid not exist"),
  CPTLIST_NOT_EXIST("2005", "cptList not exist"),
  DID_NOT_CORRESPOND_TO_CPTID("2006", "hub not correspond to cptId"),
  VOUCHER_HAS_BEEN_REVOKED("2007", "voucher has been revoked"),
  CREDINFO_NOT_EXIST("2008", "credInfo not exist"),
  DID_NOT_EXIST("2011", "hub not exist"),
  DID_ALREADY_EXIST("2012", "hub already exist"),
  ISSUER_NOT_EXIST("2021", "issuer not exist"),
  ISSUER_ALREADY_EXIST("2022", "issuer already exist"),
  CONTRACT_ADDR_INVALID("2023", "contract is not valid");

  private String code;

  private String enMessage;

  ContractCode(String code, String enMessage) {
    this.code = code;
    this.enMessage = enMessage;
  }

  public static ContractCode getByCode(String errorCode) {
    for (ContractCode code : ContractCode.values()) {
      if (code.code.equals(errorCode)) {
        return code;
      }
    }
    return ContractCode.UNKNOWN_ERROR;
  }

  public static boolean isSuccess(ContractCode other) {
    return ContractCode.SUCCESS.getCode().equals(other.getCode());
  }

  public static boolean isSuccess(String code) {
    return ContractCode.SUCCESS.getCode().equals(code);
  }

  public String getCode() {
    return code;
  }

  public String getEnMessage() {
    return enMessage;
  }

  public static String getEnMessage(ContractCode other) {
    for (ContractCode code : ContractCode.values()) {
      if (code.code.equals(other.code)) {
        return code.getEnMessage();
      }
    }

    return null;
  }
}
