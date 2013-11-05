/**
 * Copyright (C) 2012 - 2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.historical;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.historical.service.polling.HistoricalPollingAccountService;
import com.xeiam.xchange.historical.service.polling.HistoricalPollingMarketDataService;
import com.xeiam.xchange.historical.service.polling.HistoricalPollingTradeService;

/**
 * <p>
 * Exchange implementation to provide the following to applications:
 * </p>
 * <ul>
 * <li>A wrapper for the BTCE exchange API</li>
 * </ul>
 */
public class HistoricalExchange extends BaseExchange implements Exchange {

  /**
   * Default constructor for ExchangeFactory
   */
  public HistoricalExchange() {

  }

  /**
   * @return A default configuration for this exchange
   */
  public static Exchange newInstance() {

    Exchange exchange = new HistoricalExchange();
    exchange.applySpecification(exchange.getDefaultExchangeSpecification());
    return exchange;
  }

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {

    super.applySpecification(exchangeSpecification);

    this.pollingMarketDataService = new HistoricalPollingMarketDataService(exchangeSpecification);
    this.pollingAccountService = new HistoricalPollingAccountService(exchangeSpecification);
    this.pollingTradeService = new HistoricalPollingTradeService(exchangeSpecification);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://btc-e.com");
    exchangeSpecification.setHost("btc-e.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("BTC-e");
    exchangeSpecification.setExchangeDescription("BTC-e is a Bitcoin exchange registered in Russia.");

    return exchangeSpecification;
  }
}
