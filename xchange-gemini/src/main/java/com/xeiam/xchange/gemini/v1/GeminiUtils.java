package com.xeiam.xchange.gemini.v1;

import com.xeiam.xchange.currency.CurrencyPair;

/**
 * A central place for shared Bitfinex properties
 */
public final class GeminiUtils {

  /**
   * private Constructor
   */
  private GeminiUtils() {

  }

  public static String toPairString(CurrencyPair currencyPair) {

    return currencyPair.baseSymbol.toLowerCase() + currencyPair.counterSymbol.toLowerCase();
  }

}
