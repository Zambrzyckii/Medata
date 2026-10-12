package com.medata.category.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  // Plain builder stays @Primary so framework internals (e.g. the Eureka
  // transport itself) never go through the load balancer by accident.
  @Bean
  @Primary
  RestClient.Builder restClientBuilder() {
    return RestClient.builder();
  }

  @Bean
  @LoadBalanced
  RestClient.Builder loadBalancedRestClientBuilder() {
    return RestClient.builder();
  }
}
