package com.xeiam.xchange.gemini.v1.service.polling;

import java.io.IOException;
import java.math.BigDecimal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.trade.FixedRateLoanOrder;
import com.xeiam.xchange.dto.trade.FloatingRateLoanOrder;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.gemini.v1.GeminiOrderType;
import com.xeiam.xchange.gemini.v1.GeminiUtils;
import com.xeiam.xchange.gemini.v1.dto.GeminiException;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiCancelOfferRequest;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiCancelOrderRequest;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiNewOfferRequest;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiNewOrderRequest;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiNonceOnlyRequest;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiOfferStatusRequest;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiOfferStatusResponse;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiOrderStatusRequest;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiOrderStatusResponse;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiPastTradesRequest;
import com.xeiam.xchange.gemini.v1.dto.trade.GeminiTradeResponse;

public class GeminiTradeServiceRaw extends GeminiBasePollingService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public GeminiTradeServiceRaw(Exchange exchange) {

    super(exchange);
  }

  public GeminiOrderStatusResponse[] getBitfinexOpenOrders() throws IOException {

    try {
      GeminiOrderStatusResponse[] activeOrders = bitfinex.activeOrders(apiKey, payloadCreator, signatureCreator, new GeminiNonceOnlyRequest(
          "/v1/orders", String.valueOf(exchange.getNonceFactory().createValue())));
      return activeOrders;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiOfferStatusResponse[] getBitfinexOpenOffers() throws IOException {

    try {
      GeminiOfferStatusResponse[] activeOffers = bitfinex.activeOffers(apiKey, payloadCreator, signatureCreator, new GeminiNonceOnlyRequest(
          "/v1/offers", String.valueOf(exchange.getNonceFactory().createValue())));
      return activeOffers;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiOrderStatusResponse placeBitfinexMarketOrder(MarketOrder marketOrder, GeminiOrderType bitfinexOrderType) throws IOException {

    String pair = GeminiUtils.toPairString(marketOrder.getCurrencyPair());
    String type = marketOrder.getType().equals(Order.OrderType.BID) ? "buy" : "sell";
    String orderType = bitfinexOrderType.toString();

    try {
      GeminiOrderStatusResponse newOrder = bitfinex.newOrder(apiKey, payloadCreator, signatureCreator,
          new GeminiNewOrderRequest(String.valueOf(exchange.getNonceFactory().createValue()), pair, marketOrder.getTradableAmount(),
              BigDecimal.ONE, "bitfinex", type, orderType));
      return newOrder;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiOrderStatusResponse placeBitfinexLimitOrder(LimitOrder limitOrder, GeminiOrderType bitfinexOrderType, boolean hidden)
      throws IOException {

    String pair = GeminiUtils.toPairString(limitOrder.getCurrencyPair());
    String type = limitOrder.getType().equals(Order.OrderType.BID) ? "buy" : "sell";
    String orderType = bitfinexOrderType.toString();

    GeminiNewOrderRequest request;
    if (hidden) {
      request = null;
    } else {
      request = new GeminiNewOrderRequest(String.valueOf(exchange.getNonceFactory().createValue()), pair, limitOrder.getTradableAmount(),
          limitOrder.getLimitPrice(), "bitfinex", type, orderType);
    }

    try {
      GeminiOrderStatusResponse newOrder = bitfinex.newOrder(apiKey, payloadCreator, signatureCreator, request);
      return newOrder;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiOfferStatusResponse placeBitfinexFixedRateLoanOrder(FixedRateLoanOrder loanOrder, GeminiOrderType orderType) throws IOException {

    String direction = loanOrder.getType() == OrderType.BID ? "loan" : "lend";

    try {
      GeminiOfferStatusResponse newOrderResponse = bitfinex.newOffer(
          apiKey,
          payloadCreator,
          signatureCreator,
          new GeminiNewOfferRequest(String.valueOf(exchange.getNonceFactory().createValue()), loanOrder.getCurrency(), loanOrder
              .getTradableAmount(), loanOrder.getRate(), loanOrder.getDayPeriod(), direction));
      return newOrderResponse;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiOfferStatusResponse placeBitfinexFloatingRateLoanOrder(FloatingRateLoanOrder loanOrder, GeminiOrderType orderType)
      throws IOException {

    String direction = loanOrder.getType() == OrderType.BID ? "loan" : "lend";

    try {
      GeminiOfferStatusResponse newOrderResponse = bitfinex.newOffer(
          apiKey,
          payloadCreator,
          signatureCreator,
          new GeminiNewOfferRequest(String.valueOf(exchange.getNonceFactory().createValue()), loanOrder.getCurrency(), loanOrder
              .getTradableAmount(), new BigDecimal("0.0"), loanOrder.getDayPeriod(), direction));
      return newOrderResponse;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public boolean cancelBitfinexOrder(String orderId) throws IOException {

    try {
      bitfinex.cancelOrders(apiKey, payloadCreator, signatureCreator,
          new GeminiCancelOrderRequest(String.valueOf(exchange.getNonceFactory().createValue()), Integer.valueOf(orderId)));
      return true;
    } catch (GeminiException e) {
      if (e.getMessage().equals("Order could not be cancelled.")) {
        return false;
      } else {
        throw new ExchangeException(e.getMessage());
      }
    }
  }

  public GeminiOfferStatusResponse cancelBitfinexOffer(String offerId) throws IOException {

    try {
      GeminiOfferStatusResponse cancelResponse = bitfinex.cancelOffer(apiKey, payloadCreator, signatureCreator, new GeminiCancelOfferRequest(
          String.valueOf(exchange.getNonceFactory().createValue()), Integer.valueOf(offerId)));
      return cancelResponse;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiOrderStatusResponse getBitfinexOrderStatus(String orderId) throws IOException {

    try {
      GeminiOrderStatusResponse orderStatus = bitfinex.orderStatus(apiKey, payloadCreator, signatureCreator,
          new GeminiOrderStatusRequest(String.valueOf(exchange.getNonceFactory().createValue()), Integer.valueOf(orderId)));
      return orderStatus;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }

  }

  public GeminiOfferStatusResponse getBitfinexOfferStatusResponse(String offerId) throws IOException {

    try {
      GeminiOfferStatusResponse offerStatus = bitfinex.offerStatus(apiKey, payloadCreator, signatureCreator,
          new GeminiOfferStatusRequest(String.valueOf(exchange.getNonceFactory().createValue()), Integer.valueOf(offerId)));
      return offerStatus;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public GeminiTradeResponse[] getBitfinexTradeHistory(String symbol, long timestamp, int limit) throws IOException {

    try {
      GeminiTradeResponse[] trades = bitfinex.pastTrades(apiKey, payloadCreator, signatureCreator,
          new GeminiPastTradesRequest(String.valueOf(exchange.getNonceFactory().createValue()), symbol, timestamp, limit));
      return trades;
    } catch (GeminiException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

}
