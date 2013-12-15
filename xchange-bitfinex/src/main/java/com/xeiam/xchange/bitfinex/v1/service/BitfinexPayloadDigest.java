package com.xeiam.xchange.bitfinex.v1.service;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;
import si.mazi.rescu.utils.Base64;

public class BitfinexPayloadDigest implements ParamsDigest {
	@Override
	public String digestParams(RestInvocation restInvocation) {
		String postBody = restInvocation.getRequestBody();
		String query = postBody;
	    
		return Base64.encodeBytes(query.getBytes());
	}
}