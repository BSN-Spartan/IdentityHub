// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.constant;

public enum ErrorCode {
  SUCCESS(0, "Success"),

  PARAMETER_IS_EMPTY(1001, "is null"),

  PARAMETER_ILLEGAL_FORMAT(1002, "The format of is invalid"),

  DATA_IS_INCOMPLETE_OR_CONTAINS_EMPTY_ATTRIBUTES(1003, "contains a null attribute value"),

  PARAMETER_TOO_LONG(1004, "is too long"),

  TRANSACTION_TIMEOUT(1005, "Transaction timeout"),

  UNKNOWN_ERROR(9999, "Unknown exception"),

  CONFIG_FILE_NOT_FOUND(1008, "Config file does not exist"),

  DID_SDK_INIT_FAIL(1014, "Failed to initialize the DID SDK"),

  CREATE_KEY_PAIR_FAIL(1020, "Failed to create the key pair"),

  PUBLIC_AND_PRIVATE_KEY_MISMATCH(1021, "Public and private keys do not match"),

  PUBLIC_KEY_IS_EMPTY(1022, "Public key is empty"),

  PUBLIC_KEY_ILLEGAL_FORMAT(1023, "Invalid public key format"),

  PRIVATE_KEY_IS_EMPTY(1024, "Private key is empty"),

  PRIVATE_KEY_ILLEGAL_FORMAT(1025, "Invalid private key format"),

  ENCRYPTION_TYPE_IS_EMPTY(1027, "Encryption Type is empty"),

  ENCRYPTION_TYPE_ILLEGAL(1028, "Invalid Encryption Type"),

  SIGNATURE_FAIL(1029, "Failed to sign the data"),

  SIGNER_DID_NOT_MATCH(1030, "Signer and DID do not match"),

  SIGNATURE_VERIFICATION_FAIL(1031, "Signature verification failed"),

  GRANT_PUBLIC_KEY_WITH_DB_NOT_MATCH(
      1032, "grantPublicKey with public key in database do not match"),

  DID_EXISTED(1040, "DID already exists"),

  DID_NOT_EXIST(1041, "DID does not exist"),

  CREATE_DID_FAIL(1042, "Failed to create DID"),

  INVALID_DID(1043, "Invalid DID"),

  GENERATE_DID_FAIL(1044, "Failed to generate the DID"),

  GENERATE_DID_DOCUMENT_FAIL(1045, "Failed to generate the DID Document"),

  DID_DOCUMENT_VALIDATION_SUCCESS(1046, "DID Document verification success"),

  ISSUER_EXISTED(1050, "DID is registered as the issuer"),

  ISSUER_NOT_EXIST(1051, "DID is not registered as the issuer"),

  CREATE_ISSUER_FAIL(1052, "Failed to register as the issuer"),

  NO_DATA_OF_ISSUER(1054, "Issuer does not exist"),

  CPT_NOT_EXIST(1060, "CPT does not exist"),

  CPT_AND_ISSUER_CANNOT_MATCH(1062, "Issuer and publisherDid in the CPT do not match"),

  CREDENTIAL_REVOKED(1070, "The credential has been revoked"),

  CREDENTIAL_EXPIRED(1071, "The credential has expired"),

  CREDENTIAL_REVOCATION_FAIL(1072, "Failed to revoke the credential"),

  CPT_AND_CREDENTIAL_CANNOT_MATCH(
      1073, "CPT and credential do not match"),

  CREATE_CREDENTIAL_FAIL(1074, "Failed to create credential"),

  CREDENTIAL_VALIDATION_SUCCESS(1075, "Credential verification success"),

  NO_DATA_OF_REVOKE_CREDENTIAL(1076, "The credential is not in the revoke list"),

  DID_DOCUMENT_NOT_MATCH(1077, "Computed DID from the document is not the same with the DID in the document"),

  DID_CREATE_UPDATE_NOT_SAME(1078, "Created time is different with updated time in DID Document"),

  VERIFY_PUBLIC_KEY_SIGN_ERR(1079, "Public key signature verification failed"),

  CPT_DID_NE_PROOF_CREATOR(1080, "DID is not the same with the proof creator in CPT"),

  DOC_VRESION_NOT_MATHCH(1081, "The DID Document version does not match the one on-chain"),

  DOC_CREATED_NOT_MATHCH(1082, "The DID Document created time does not match the one on-chain"),

  DOC_RECOVERY_KEY_NOT_MATHCH(1083, "The DID Document recovery key does not match the one on-chain"),

  STORE_DID_DOC_FAILED(1084, "Failed to add DID Document to the chain"),

  CREAT_KEY_FAIL(1085, "Failed to create the key pair"),

  HASH_DID_FAIL(1086, "Failed to calculate the DID"),

  CREATE_DID_DOC_FAIL(1088, "Failed to create the DID Document"),
  
  CRYPT_TYPE_IS_EMPTY(1089, "cryptoTyp is empty"),

  CRED_ID_NOT_MATCH_CPT_ID(2000, "credId does not match cptId"),
  ;

  private Integer code;
  private String enMessage;

  ErrorCode(Integer code, String enMessage) {
    this.code = code;
    this.enMessage = enMessage;
  }

  public static ErrorCode getTypeByErrorCode(Integer codeParam) {
    for (ErrorCode code : ErrorCode.values()) {
      if (code.getCode().equals(codeParam)) {
        return code;
      }
    }
    return ErrorCode.UNKNOWN_ERROR;
  }

  public static boolean isSuccess(Integer code) {
    return ErrorCode.SUCCESS.getCode().equals(code);
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getEnMessage() {
    return enMessage;
  }

  public void setEnMessage(String enMessage) {
    this.enMessage = enMessage;
  }

  public ErrorCode editEnMessage(String enMessage) {
    this.setEnMessage(enMessage + this.enMessage);
    return this;
  }
}
