package com.xeiam.xchange.gemini.v1.service.polling;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.LoanOrderBook;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.FixedRateLoanOrder;
import com.xeiam.xchange.dto.trade.FloatingRateLoanOrder;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.gemini.v1.GeminiAdapters;
import com.xeiam.xchange.gemini.v1.GeminiUtils;
import com.xeiam.xchange.gemini.v1.dto.marketdata.GeminiDepth;
import com.xeiam.xchange.gemini.v1.dto.marketdata.GeminiTrade;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;

/**
 * <p>
 * Implementation of the market data service for Bitfinex
 * </p>
 * <ul>
 * <li>Provides access to various market data values</li>
 * </ul>
 */
public class GeminiMarketDataService extends GeminiMarketDataServiceRaw implements PollingMarketDataService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GeminiMarketDataService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {

    return GeminiAdapters.adaptTicker(getBitfinexTicker(GeminiUtils.toPairString(currencyPair)), currencyPair);
  }

  /**
   * @param args If two integers are provided, then those count as limit bid and limit ask count
   */
  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {

    // null will cause fetching of full order book, the default behavior in XChange
    Integer limitBids = null;
    Integer limitAsks = null;

    if (args.length == 2) {
      Object arg0 = args[0];
      if (!(arg0 instanceof Integer)) {
        throw new ExchangeException("Argument 0 must be an Integer!");
      } else {
        limitBids = (Integer) arg0;
      }
      Object arg1 = args[1];
      if (!(arg1 instanceof Integer)) {
        throw new ExchangeException("Argument 1 must be an Integer!");
      } else {
        limitAsks = (Integer) arg1;
      }
    }

    GeminiDepth bitfinexDepth = getBitfinexOrderBook(GeminiUtils.toPairString(currencyPair), limitBids, limitAsks);

    OrderBook orderBook = GeminiAdapters.adaptOrderBook(bitfinexDepth, currencyPair);

    return orderBook;
  }

  /**
   * @param currencyPair The CurrencyPair for which to query trades.
   * @param args One argument may be supplied which is the timestamp after which trades should be collected. Trades before this time are not reported.
   *        The argument may be of type java.util.Date or Number (milliseconds since Jan 1, 1970)
   */
  @Override
  public Trades getTrades(CurrencyPair currencyPair, Object... args) throws IOException {

    long lastTradeTime = 0;
    if (args != null && args.length == 1) {
      // parameter 1, if present, is the last trade timestamp
      if (args[0] instanceof Number) {
        Number arg = (Number) args[0];
        lastTradeTime = arg.longValue() / 1000; // divide by 1000 to convert to unix timestamp (seconds)
      } else if (args[0] instanceof Date) {
        Date arg = (Date) args[0];
        lastTradeTime = arg.getTime() / 1000; // divide by 1000 to convert to unix timestamp (seconds)
      } else {
        throw new IllegalArgumentException("Extra argument #1, the last trade time, must be a Date or Long (millisecond timestamp) (was "
            + args[0].getClass() + ")");
      }
    }
    GeminiTrade[] trades = getBitfinexTrades(GeminiUtils.toPairString(currencyPair), lastTradeTime);

    return GeminiAdapters.adaptTrades(trades, currencyPair);
  }

}
