package com.mk.evaexchange;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

@Configuration
public class JacksonConfiguration {

  @Bean
  com.fasterxml.jackson.databind.Module javaTimeModule() {
    return new MoneyModule();
  }
}
