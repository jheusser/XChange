package com.xeiam.xchange.gemini.v1;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import si.mazi.rescu.ParamsDigest;

import com.xeiam.xchange.gemini.v1.dto.GeminiException;
import com.xeiam.xchange.gemini.v1.dto.account.GeminiBalancesRequest;
import com.xeiam.xchange.gemini.v1.dto.account.GeminiBalancesResponse;
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

@Path("v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GeminiAuthenticated extends Gemini {

  @POST
  @Path("order/new")
  GeminiOrderStatusResponse newOrder(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiNewOrderRequest newOrderRequest) throws IOException, GeminiException;

  @POST
  @Path("offer/new")
  GeminiOfferStatusResponse newOffer(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiNewOfferRequest newOfferRequest) throws IOException, GeminiException;

  @POST
  @Path("balances")
  GeminiBalancesResponse[] balances(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiBalancesRequest balancesRequest) throws IOException, GeminiException;

  @POST
  @Path("order/cancel")
  GeminiOrderStatusResponse cancelOrders(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiCancelOrderRequest cancelOrderRequest) throws IOException, GeminiException;

  @POST
  @Path("offer/cancel")
  GeminiOfferStatusResponse cancelOffer(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiCancelOfferRequest cancelOfferRequest) throws IOException, GeminiException;

  @POST
  @Path("orders")
  GeminiOrderStatusResponse[] activeOrders(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiNonceOnlyRequest nonceOnlyRequest) throws IOException, GeminiException;

  @POST
  @Path("offers")
  GeminiOfferStatusResponse[] activeOffers(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiNonceOnlyRequest nonceOnlyRequest) throws IOException, GeminiException;

  @POST
  @Path("order/status")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  GeminiOrderStatusResponse orderStatus(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiOrderStatusRequest orderStatusRequest) throws IOException, GeminiException;

  @POST
  @Path("offer/status")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  GeminiOfferStatusResponse offerStatus(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiOfferStatusRequest offerStatusRequest) throws IOException, GeminiException;

  @POST
  @Path("mytrades")
  GeminiTradeResponse[] pastTrades(@HeaderParam("X-GEMINI-APIKEY") String apiKey, @HeaderParam("X-GEMINI-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-GEMINI-SIGNATURE") ParamsDigest signature, GeminiPastTradesRequest pastTradesRequest) throws IOException, GeminiException;
}
