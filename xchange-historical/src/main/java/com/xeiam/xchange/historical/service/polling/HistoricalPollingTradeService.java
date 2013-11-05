/**
 * Copyright (C) 2012 - 2013 Matija Mazi
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
package com.xeiam.xchange.historical.service.polling;

import java.io.IOException;

import org.fest.util.Lists;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.historical.HistoricalUtils;
import com.xeiam.xchange.service.polling.PollingTradeService;
import com.xeiam.xchange.utils.Assert;

public class HistoricalPollingTradeService extends HistoricalBasePollingService implements PollingTradeService {

  public HistoricalPollingTradeService(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {
    return new OpenOrders(Lists.<LimitOrder>newArrayList());
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {
    throw new UnsupportedOperationException("Market orders not supported");
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {

    Assert.isTrue(HistoricalUtils.isValidCurrencyPair(new CurrencyPair(limitOrder.getTradableIdentifier(), limitOrder.getTransactionCurrency())), "currencyPair is not valid:"
        + limitOrder.getTradableIdentifier() + " " + limitOrder.getTransactionCurrency());

    return Long.toString(1L);
  }

  @Override
  public boolean cancelOrder(String orderId) throws IOException {
    return true;
  }

  @Override
  public Trades getTradeHistory(final Object... arguments) throws IOException {

    Long numberOfTransactions = Long.MAX_VALUE;
    String tradableIdentifier = "";
    String transactionCurrency = "";
    try {
      numberOfTransactions = (Long) arguments[0];
      tradableIdentifier = (String) arguments[1];
      transactionCurrency = (String) arguments[2];
    } catch (ArrayIndexOutOfBoundsException e) {
      // ignore, can happen if no arg given.
    }

    String pair = String.format("%s_%s", tradableIdentifier, transactionCurrency).toLowerCase();
    return new Trades(Lists.<Trade>newArrayList());
  }

}
