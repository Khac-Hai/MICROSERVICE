package com.example.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SystemHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        return chain.filter(exchange)
                .then(
                    Mono.fromRunnable(() -> {

                        exchange
                          .getResponse()
                          .getHeaders()
                          .add(
                              "X-System-Name",
                              "Api-Gateway-System"
                          );

                        System.out.println(
                          "Added response header X-System-Name"
                        );

                    })
                );

    }

    @Override
    public int getOrder() {

        return 1;
    }

}

