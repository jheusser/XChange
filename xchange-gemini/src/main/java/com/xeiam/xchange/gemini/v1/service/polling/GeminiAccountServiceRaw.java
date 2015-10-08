package com.xeiam.xchange.gemini.v1.service.polling;

import java.io.IOException;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.gemini.v1.dto.GeminiException;
import com.xeiam.xchange.gemini.v1.dto.account.GeminiBalancesRequest;
import com.xeiam.xchange.gemini.v1.dto.account.GeminiBalancesResponse;

public class GeminiAccountServiceRaw extends GeminiBasePollingService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GeminiAccountServiceRaw(Exchange exchange) {

    super(exchange);
  }

  public GeminiBalancesResponse[] getBitfinexAccountInfo() throws IOException {

    try {
      GeminiBalancesResponse[] balances = bitfinex.balances(apiKey, payloadCreator, signatureCreator,
          new GeminiBalancesRequest(String.valueOf(exchange.getNonceFactory().createValue())));
      return balances;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

}
