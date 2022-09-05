// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.hub.constant;

public enum ErrorMessage {
  SUCCESS(0, "Success"),

  UNKNOWN_ERROR(9999, "Unknown exception"),

  PRIVATE_KEY_EMPTY(1303, "Private key is empty"),

  PRIVATE_KEY_FORMAT_ERROR(1304, "Private key format is invalid"),

  PUBLIC_KEY_EMPTY(1305, "Public key is empty"),

  PUBLIC_KEY_FORMAT_ERROR(1306, "Public key format is invalid"),

  CONTNET_EMPTY(1309, "The content is empty"),

  URL_EMPTY(1310, "The URL is empty"),

  HUB_URL_EMPTY(1321, "The URL of the Identity Hub cannot be empty"),

  HUB_PULIC_KEY_EMPTY(1322, "The public key of the Identity Hub cannot be empty"),

  SEND_REQ_ERROR(1327, "Failed to send the request"),

  GRANT_ERROR(1328, "The format of the grant is invalid"),

  GRANT_NOT_NULL(1329, "Grant cannot be empty"),

  KEY_IS_EMPTY(1336, "The key is empty"),

  ENCRYPT_KEY_FAILED(1337, "Failed to encrypt the key"),

  SIGNATURE_FAILED(1029, "Failed to sign the data"),

  ADD_PERMISSION_FAILED(1339, "Failed to add permission"),

  DELETE_PERMISSION_FAILED(1341, "Failed to delete permission"),

  QUERY_PERMISSION_FAILED(1342, "Failed to query permission"),

  CHECK_PERMISSION_FAILED(1344, "Failed to check permission"),

  GET_PUBLIC_KEY_FAILED(1347, "Failed to query publicKey"),

  CONFIG_FILE_NOT_FOUND(1008, "Config file does not exist"),

  NO_NEED_ADD_PERMISSION(1352, "You cannot add permissions to yourself"),

  FLAG_ERROR(1354, "Illegal flag"),

  USERID_EMPTY(1361, "The user ID is empty"),

  GRANT_USERID_EMPTY(1362, "The granted user ID is empty"),

  GRANT_USER_PUK_EMPTY(1363, "The public key of the granted user is empty"),

  GEN_USER_ID_FAILED(1364, "Failed to generate the user ID"),

  DECRYPT_FAILED(1365, "Failed to decrypt the data"),

  ENCRYPT_FAILED(1366, "Failed to encrypt the data"),

  DECRYPT_HUB_PK_FAILED(1367, "Failed to decrypt Identity Hub's private key"),

  HUB_PRIVATE_KEY_EMPTY(1368, "The private key of Identity Hub is empty"),

  NO_PERMSSION_ADD_RESOUIRCE(1370, "No permission to save resource"),

  NO_PERMSSION_UPDATE_RESOUIRCE(1371, "No permission to update resource"),

  CAN_NOT_SAVE_RESOURCE_AGAIN(1372, "The resource has been saved, cannot be saved again"),

  EXISTS_WRITE_AGAIN(1373, "The granted user already has an unused permission"),

  PERMISSION_NOT_FOUND(1400, "Permission does not exist"),

  SAVE_RESOURCE_ERROR(1408, "Failed to save the resource"),

  QUERY_RESOURCE_ERROR(1409, "Failed to query the resource"),

  DELETE_RESOURCE_ERROR(1410, "Failed to delete the resource"),

  HTTP_REQUEST_ERROR(1412, "Failed to get the HTTP request"),

  GET_HUB_PRIVATE_KEY_ERROR(1413, "Failed to get the private key of the Identity Hub"),

  MISSING_DADA(1414, "Missing request data"),

  REQUEST_PARAM_ERROR(1415, "Failed to convert the request parameter"),

  CODE_ERROR(1417, "Parse return code error"),

  RESOURCE_NOT_FOUND(1418, "Resource does not exist"),

  VALIDATE_SIGN_ERROR(1422, "Signature verification failed"),

  USER_NOT_EXISTS(1423, "The user is not registered"),

  USER_RESITERD(1425, "The user is already registered"),

  CLOSE_PERMISSION_ERROR(1428, "Failed to close the permission"),

  NOT_OWNER_DELETE(1429, "Only the resource owner can delete it"),

  ADD_OPERATE_ERROR(1430, "Failed to add operation record"),

  REGISTER_PUBLIC_KEY_ERROR(1431, "Failed to register the user"),

  PARSE_REQ_PARAM_FAILED(1433, "Failed to parse the request parameter"),

  ADD_OPERATION_HISTORY_FAILED(1434, "Failed to add the operation record"),

  UPDATE_RESOURCE_FAILED(1435, "Failed to update the resource"),

  SAVE_HUB_USER_FAILED(1438, "Failed to save the user information to the database"),

  ADD_ADDR_PUB_FAILED(1439, "add address and publicKey failed"),

  ADDR_HAS_EXIST(1440, "the address has exists"),

  PERMISSION_USED_OR_NOT_HAVE(1441, "The current permission has been used or the user does not have permission"),

  QUERY_RESOURCE_HISTORY_ERROR(1443, "query resource history error"),
  SYNC_HUB_DATA_ENABLE_EMPTY(1444, "synchronize hub data to others enable config is empty"),
  SYNC_HUB_DATA_HOST_EMPTY(1445, "synchronize hub data receive hosts config is empty"),
  SYNC_HUB_DATA_HOST_URL_EMPTY(1446, "synchronize hub data receive host's url is empty"),
  SYNC_HUB_DATA_HOST_PK_EMPTY(1447, "synchronize hub data receive host's public key is empty"),
  SYNC_HUB_DATA_HOST_AES_KEY_EMPTY(
      1448, "synchronize hub data receive host's AES encryption key is empty"),
  SYNC_HUB_DATA_HOST_ID_EMPTY(1449, "synchronize hub data receive host's Id is empty"),
  SYNC_HUB_DATA_DECRYPT_FAILED(1450, "decrypt data failed when process synchronize hub data"),
  SYNC_HUB_DATA_HUB_ID_EMPTY(1451, "hub Id is empty in received synchronize hub data"),
  SYNC_HUB_DATA_DATA_EMPTY(1452, "data content is empty in received synchronize hub data"),
  AES_KEY_NOT_FIND(1453, "can not find the asc key in the config file"),
  SYNC_HUB_LAST_FILE_PATH_EMPTY(1454, "synchronize hub data last update file path name is empty"),

  DECRYPT_KEY_FAILED(1455, "decrypt encptyKey failed"),

  DECRYPT_CONTENT_FAILED(1456, "decrypt content failed"),

  IPFS_NUTI_ADDRE(1457, "ipfs mutil address can not empty"),

  IPFS_FILE_HASH_EMPTY(1458, "file mutil hash is empty"),

  SERVICE_CRYPTO_TYPE_EMPTY(1459, "service crypto type is empty"),

  NEW_OWNER_UID_EMPTY(1460, "New owner's user ID is empty"),

  TRANSFER_OWNER_ERROR(1461, "Failed to change the owner"),

  UID_OWNERUID_EQ_ERROR(1462, "The uid and the ownerUid cannot be the same"),

  OWNER_UID_NOT_EXISTS(1463, "The ownerUid is not exist"),
  
  GET_CRYPTO_TYPE_FAILED(1464, "query crypto type failed"),
  
  NEW_OWNER_PUBLIC_KEY_NOT_MATCH(1465, "The new owner and new owner's public key do not match"),
  
  CRYPTO_TYPE_INCORRECT(1466, "crypto type is illegal format"),
  ;

  private Integer code;

  private String message;

  private ErrorMessage(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public Integer getCode() {
    return code;
  }

  public static String getMessage(Integer code) {
    for (ErrorMessage error : ErrorMessage.values()) {
      if (error.code.equals(code)) {
        return error.message;
      }
    }
    return null;
  }

  public static String getMessage(ErrorMessage errorMessage) {
    for (ErrorMessage error : ErrorMessage.values()) {
      if (error.code.equals(errorMessage.code)) {
        return error.message;
      }
    }
    return null;
  }
}
