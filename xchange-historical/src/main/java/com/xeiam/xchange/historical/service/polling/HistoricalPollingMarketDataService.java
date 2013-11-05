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
package com.xeiam.xchange.historical.service.polling;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.annotation.Nullable;

import org.fest.util.Lists;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Ticker.TickerBuilder;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.historical.HistoricalUtils;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.xeiam.xchange.utils.Assert;


public class HistoricalPollingMarketDataService implements
		PollingMarketDataService {
	private final TreeMultimap<Date, Class<?>> timestampToClass = TreeMultimap.create(Ordering.natural(), Ordering.arbitrary());
	private final TreeMap<Date, Ticker> timestampToTicker = Maps.newTreeMap();

	private Date currentHistoricalTimestampDate;
	private int timestampCounter = 0;

	public HistoricalPollingMarketDataService(
			@Nullable ExchangeSpecification exchangeSpecification) {
	}
	
	public static HistoricalPollingMarketDataService createFromFile(File tickerFile) {
			HistoricalFileParser<Ticker> tickerParser = new HistoricalFileParser<Ticker>(
			tickerFile, new LineFactory<Ticker>() {
				@Override
				public Ticker instantiateFromLine(String line) {
					Iterable<String> split = Splitter.on(',').trimResults()
							.split(line);
					String timestamp = Iterables.get(split, 0);
					String ask = Iterables.get(split, 1);
	
					try {
						Date timestampDate = new SimpleDateFormat(
								"dd-MM-yyyy k:m:s.SSS", Locale.ENGLISH)
								.parse(timestamp);
						return TickerBuilder.newInstance()
								.withTimestamp(timestampDate).withAsk(BigMoney.of(CurrencyUnit.USD, new BigDecimal(ask))).build();
	
					} catch (ParseException e) {
						throw new RuntimeException("Date issue", e);
					}
				}
			});	
			
			return new HistoricalPollingMarketDataService(tickerParser, null, null);
	}
	
	public HistoricalPollingMarketDataService(
			HistoricalFileParser<Ticker> tickerParser,
			HistoricalFileParser<Trade> tradeParser,
			HistoricalFileParser<OrderBook> orderBookParser) {
	
		List<Ticker> list;
		try {

			list = tickerParser.parse();

			for (Ticker ticker : list) {
				timestampToClass.put(ticker.getTimestamp(), Ticker.class);
				timestampToTicker.put(ticker.getTimestamp(), ticker);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initialiseHistoricalTimestamp();
	}
	
	/**
	 * Initialise the current timestamp to the beginning of all data points.
	 */
	private final void initialiseHistoricalTimestamp() {
		 currentHistoricalTimestampDate = Iterables.get(timestampToClass.entries(), 0).getKey();
	}
	
	/** 
	 * Get all available data points at the current timestamp.
	 */
	public List<Class<?>> getAvailableMarketData() {
		return Lists.newArrayList(timestampToClass.get(currentHistoricalTimestampDate));
	}
	
	/** 
	 * Increments simulation time by one tick to the next set of data points.
	 */
	public void tick() {
		if(timestampCounter >= timestampToClass.size() - 1) {
			// end of data
			throw new RuntimeException("End of simulation");
		} 
		
		currentHistoricalTimestampDate = Iterables.get(timestampToClass.entries(), ++timestampCounter).getKey();
	}

	@Override
	public Ticker getTicker(String tradableIdentifier, String currency)
			throws IOException {
		verify(tradableIdentifier, currency);
		Ticker latestTicker = timestampToTicker.get(currentHistoricalTimestampDate);
		// check if advance simulation time
		return latestTicker;
		
	}

	@Override
	public OrderBook getPartialOrderBook(String tradableIdentifier,
			String currency) throws IOException {

		throw new NotAvailableFromExchangeException();
	}

	@Override
	public OrderBook getFullOrderBook(String tradableIdentifier, String currency)
			throws IOException {

		verify(tradableIdentifier, currency);

		List<LimitOrder> asks = Lists.newArrayList();
		List<LimitOrder> bids = Lists.newArrayList();

		return new OrderBook(null, asks, bids);
	}

	@Override
	public Trades getTrades(String tradableIdentifier, String currency,
			Object... args) throws IOException {

		verify(tradableIdentifier, currency);

		return new Trades(Lists.<Trade> newArrayList());
	}

	private void verify(String tradableIdentifier, String currency)
			throws IOException {

		Assert.notNull(tradableIdentifier, "tradableIdentifier cannot be null");
		Assert.notNull(currency, "currency cannot be null");
	}

	@Override
	public List<CurrencyPair> getExchangeSymbols() {

		return HistoricalUtils.CURRENCY_PAIRS;
	}

}
