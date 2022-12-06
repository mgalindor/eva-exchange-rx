package com.mk.evaexchange;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import com.mk.evaexchange.ExchangeService.ExchangeRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  ExchangeService getExchangeService() {
    return new ExchangeService();
  }

  @Bean
  public RouterFunction<ServerResponse> handlers() {
    return RouterFunctions.route()
        .POST("/exchange", RequestPredicates.accept(MediaType.APPLICATION_JSON),
            serverRequest ->
                serverRequest.bodyToMono(ExchangeRequest.class)
                    .map(getExchangeService()::exchange)
                    .flatMap(exchangeResponse -> {
                      log.debug("Result [{}]", exchangeResponse);
                      return ServerResponse.ok()
                          .contentType(MediaType.APPLICATION_JSON)
                          .bodyValue(exchangeResponse);
                    })
                    .onErrorResume(throwable -> {
                      ErrorResponse response = exceptionHandler(serverRequest.exchange(),
                          throwable);
                      return ServerResponse.status(response.status)
                          .contentType(MediaType.APPLICATION_JSON)
                          .bodyValue(response);
                    })
        )
        .build();

  }

  ErrorResponse exceptionHandler(ServerWebExchange exchange, Throwable ex) {
    ErrorResponse.ErrorResponseBuilder builder = ErrorResponse.builder()
        .timestamp(OffsetDateTime.now())
        .path(exchange.getRequest().getURI().getPath());

    return Match(ex).of(
        Case($(instanceOf(ConstraintViolationException.class)),
            cause -> {
              List<LocationResponse> details = cause.getConstraintViolations().stream().
                  map(violation -> new LocationResponse(violation.getPropertyPath().toString(),
                      violation.getMessage()))
                  .collect(Collectors.toList());
              return builder
                  .status(HttpStatus.BAD_REQUEST.value())
                  .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                  .details(details)
                  .build();
            }),
        Case($(), cause -> builder
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message(cause.getMessage())
            .build())
    );

  }

  @Builder
  record ErrorResponse(OffsetDateTime timestamp, int status, String error, String message,
                       String path,
                       List<LocationResponse> details, String type) {

  }

  record LocationResponse(String field, String error) {

  }


}
