package com.xeiam.xchange.gemini.v1.dto.account;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xeiam.xchange.gemini.v1.dto.account.GeminiBalancesResponse;

/**
 * Test BTCEDepth JSON parsing
 */
public class BitfinexAccountInfoJSONTest {

  @Test
  public void testUnmarshal() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = BitfinexAccountInfoJSONTest.class.getResourceAsStream("/v1/account/example-account-info-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    GeminiBalancesResponse readValue = mapper.readValue(is, GeminiBalancesResponse.class);

    assertEquals(readValue.getAmount().toString(), new BigDecimal("8.53524686").toString());

  }
}
