package org.bbsv2.gateway.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.nio.charset.StandardCharsets;

import reactor.core.publisher.Mono;

@Component
@Order(-2) // 确保优先级高于默认处理器
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    String errorMsg = String.format("{\"code\":\"500\",\"message\":\"%s\"}", ex.getMessage());
    System.out.println("Gateway 捕获到异常：" + ex.getClass().getName() + " - " + ex.getMessage());

    return exchange.getResponse()
            .writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory()
                    .wrap(errorMsg.getBytes(StandardCharsets.UTF_8))));
  }
}
