package com.xeiam.xchange.gemini.v1;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.xeiam.xchange.gemini.v1.dto.GeminiException;
import com.xeiam.xchange.gemini.v1.dto.marketdata.GeminiDepth;
import com.xeiam.xchange.gemini.v1.dto.marketdata.GeminiTicker;
import com.xeiam.xchange.gemini.v1.dto.marketdata.GeminiTrade;

@Path("v1")
@Produces(MediaType.APPLICATION_JSON)
public interface Gemini {

  @GET
  @Path("pubticker/{symbol}")
  GeminiTicker getTicker(@PathParam("symbol") String symbol) throws IOException, GeminiException;

  @GET
  @Path("book/{symbol}")
  GeminiDepth getBook(@PathParam("symbol") String symbol, @QueryParam("limit_bids") int limit_bids, @QueryParam("limit_asks") int limit_asks)
      throws IOException, GeminiException;

  @GET
  @Path("book/{symbol}")
  GeminiDepth getBook(@PathParam("symbol") String symbol) throws IOException, GeminiException;

 
  @GET
  @Path("trades/{symbol}")
  GeminiTrade[] getTrades(@PathParam("symbol") String symbol, @QueryParam("timestamp") long timestamp) throws IOException, GeminiException;

   @GET
  @Path("symbols")
  Set<String> getSymbols() throws IOException, GeminiException;

}
