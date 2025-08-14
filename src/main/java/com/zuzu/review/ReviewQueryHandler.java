package com.zuzu.review;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;

public class ReviewQueryHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
  private final EntityManagerFactory emf = OrmConfig.emf();
  private final ReviewRepository repo = new ReviewRepository(emf);

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
    try {
      String hotelIdStr = request.getQueryStringParameters() == null ? null : request.getQueryStringParameters().get("hotelId");
      if (hotelIdStr == null) return response(400, "{\"error\":\"hotelId is required\"}");
      long hid = Long.parseLong(hotelIdStr);
      var list = repo.findByHotelId(hid);
      return response(200, mapper.writeValueAsString(list));
    } catch (Exception e){
      return response(500, "{\"error\":\""+e.getMessage()+"\"}");
    }
  }

  private APIGatewayProxyResponseEvent response(int status, String body){
    var headers = new HashMap<String, String>();
    headers.put("Content-Type","application/json");
    headers.put("Access-Control-Allow-Origin","*");
    return new APIGatewayProxyResponseEvent().withStatusCode(status).withHeaders(headers).withBody(body);
  }
}
