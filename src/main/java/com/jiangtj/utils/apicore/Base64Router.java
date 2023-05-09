package com.jiangtj.utils.apicore;

import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.netty.util.CharsetUtil;

@Configuration
public class Base64Router {
  
	@Bean
	public RouterFunction<ServerResponse> baseRoutes() {
		return RouterFunctions.route()
    .GET("/base64d", serverRequest -> {
      return serverRequest.queryParam("url")
      .map(url -> {
        return WebClient.create().get().uri(url)
        .retrieve()
        .bodyToMono(String.class)
        .map(b64 -> {
          return new String(
            Base64.getDecoder().decode(b64.getBytes(CharsetUtil.UTF_8)),
            CharsetUtil.UTF_8);
        })
        .flatMap(result -> ServerResponse.ok().bodyValue(result));
      })
      .orElseGet(() -> ServerResponse.ok().bodyValue("ok"));
			})
			.build();
	}
  
}
