package com.xeiam.xchange.gemini.v1.service.polling;

import java.io.IOException;
import java.math.BigDecimal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.exceptions.NotAvailableFromExchangeException;
import com.xeiam.xchange.gemini.v1.GeminiAdapters;
import com.xeiam.xchange.service.polling.account.PollingAccountService;

public class GeminiAccountService extends GeminiAccountServiceRaw implements PollingAccountService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GeminiAccountService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {

    return GeminiAdapters.adaptAccountInfo(getBitfinexAccountInfo());
  }

  @Override
  public String withdrawFunds(String currency, BigDecimal amount, String address) throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  @Override
  public String requestDepositAddress(String currency, String... arguments) throws IOException {

    throw new NotAvailableFromExchangeException();
  }
}
