package com.xeiam.xchange.gemini.v1.dto.trade;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public final class GeminiNewMultiOrderRequest {
  @JsonProperty("request")
  protected String request;
  
  @JsonProperty("nonce")
  private String nonce;

  @JsonProperty("orders")
  List<GeminiNewOrderRequest> orders;

  public GeminiNewMultiOrderRequest(String nonce, List<GeminiNewOrderRequest> orders) {
    this.request = "/v1/order/new/multi";
    this.nonce = nonce;
    this.orders = orders;
  }

  public List<GeminiNewOrderRequest> getOrders() {
    return orders;
  }

  public String getNonce() {
    return nonce;
  }
  
  public String getRequest() {
    return request;
  }
}
