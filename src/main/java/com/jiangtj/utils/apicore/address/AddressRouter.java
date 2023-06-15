package com.jiangtj.utils.apicore.address;

import io.netty.util.CharsetUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AddressRouter {

    @Bean
    public RouterFunction<ServerResponse> addressRoutes(AddressService addressService) {
        return RouterFunctions.route()
            .GET("address/update", request -> {
                return addressService.requestAddressData()
                    .then(ServerResponse.ok().build());
            })
            .GET("address/provinces", request -> ServerResponse.ok().bodyValue(addressService.getProvinces()))
            .GET("address/cities", request -> {
                Flux<City> result = Flux.fromIterable(addressService.getCityMap().values())
                    .flatMap(Flux::fromIterable);
                return ServerResponse.ok().body(result, City.class);
            })
            .GET("address/areas", request -> {
                Flux<Area> result = Flux.fromIterable(addressService.getAreaMap().values())
                    .flatMap(Flux::fromIterable);
                return ServerResponse.ok().body(result, Area.class);
            })
            .GET("address/streets", request -> {
                Flux<Street> result = Flux.fromIterable(addressService.getStreetMap().values())
                    .flatMap(Flux::fromIterable);
                return ServerResponse.ok().body(result, Street.class);
            })
            .GET("address/cities/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getCityMap().getOrDefault(request.pathVariable("code"), Collections.emptyList())))
            .GET("address/areas/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getAreaMap().getOrDefault(request.pathVariable("code"), Collections.emptyList())))
            .GET("address/streets/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getStreetMap().getOrDefault(request.pathVariable("code"), Collections.emptyList())))
            .build();
    }
}
