package com.xeiam.xchange.gemini.v1.service;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;
import si.mazi.rescu.utils.Base64;

public class GeminiPayloadDigest implements ParamsDigest {

  @Override
  public synchronized String digestParams(RestInvocation restInvocation) {

    String postBody = restInvocation.getRequestBody();
    return Base64.encodeBytes(postBody.getBytes());
  }
}