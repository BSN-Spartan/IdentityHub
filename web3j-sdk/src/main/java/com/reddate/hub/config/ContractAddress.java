// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.config;

public class ContractAddress {

  private String didContractAddress;

  private String issuerAddress;

  private String cptAddress;

  private Integer timeOut;
  
  private String didProxyAdminAddress;
  
  private String issuerProxyAdminAddress;
  
  private String cptProxyAdminAddress;

  private String didLogicContractAddress;

  private String issuerLogicContractAddress;

  private String cptLogicContractAddress;


  public String getDidLogicContractAddress() {
    return didLogicContractAddress;
  }

  public void setDidLogicContractAddress(String didLogicContractAddress) {
    this.didLogicContractAddress = didLogicContractAddress;
  }

  public String getIssuerLogicContractAddress() {
    return issuerLogicContractAddress;
  }

  public void setIssuerLogicContractAddress(String issuerLogicContractAddress) {
    this.issuerLogicContractAddress = issuerLogicContractAddress;
  }

  public String getCptLogicContractAddress() {
    return cptLogicContractAddress;
  }

  public void setCptLogicContractAddress(String cptLogicContractAddress) {
    this.cptLogicContractAddress = cptLogicContractAddress;
  }

  public Integer getTimeOut() {
    return timeOut;
  }

  public void setTimeOut(Integer timeOut) {
    this.timeOut = timeOut;
  }

  public String getDidContractAddress() {
    return didContractAddress;
  }

  public void setDidContractAddress(String didContractAddress) {
    this.didContractAddress = didContractAddress;
  }

  public String getIssuerAddress() {
    return issuerAddress;
  }

  public void setIssuerAddress(String issuerAddress) {
    this.issuerAddress = issuerAddress;
  }

  public String getCptAddress() {
    return cptAddress;
  }

  public void setCptAddress(String cptAddress) {
    this.cptAddress = cptAddress;
  }

  public String getDidProxyAdminAddress() {
	return didProxyAdminAddress;
  }

  public void setDidProxyAdminAddress(String didProxyAdminAddress) {
	this.didProxyAdminAddress = didProxyAdminAddress;
  }

  public String getIssuerProxyAdminAddress() {
	return issuerProxyAdminAddress;
  }

  public void setIssuerProxyAdminAddress(String issuerProxyAdminAddress) {
	this.issuerProxyAdminAddress = issuerProxyAdminAddress;
  }

  public String getCptProxyAdminAddress() {
	return cptProxyAdminAddress;
  }

  public void setCptProxyAdminAddress(String cptProxyAdminAddress) {
	this.cptProxyAdminAddress = cptProxyAdminAddress;
  }
  
}
