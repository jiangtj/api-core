package com.jiangtj.utils.apicore.address;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Collections;

@Configuration
public class AddressRouter {

    @Bean
    public RouterFunction<ServerResponse> addressRoutes(AddressService addressService) {
        return RouterFunctions.route()
            .GET("address/update", request -> addressService.requestAddressData()
                .then(ServerResponse.ok().bodyValue("更新成功！")))
            .GET("address/provinces", request -> ServerResponse.ok().bodyValue(addressService.getProvinces()))
            .GET("address/cities", request -> ServerResponse.ok().bodyValue(addressService.getCities()))
            .GET("address/areas", request -> ServerResponse.ok().bodyValue(addressService.getAreas()))
            .GET("address/streets", request -> ServerResponse.ok().bodyValue(addressService.getStreets()))
            .GET("address/cities/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getCityMap().getOrDefault(request.pathVariable("code"), Collections.emptyList())))
            .GET("address/areas/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getAreaMap().getOrDefault(request.pathVariable("code"), Collections.emptyList())))
            .GET("address/streets/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getStreetMap().getOrDefault(request.pathVariable("code"), Collections.emptyList())))
            .GET("address/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getAddressInfo(request.pathVariable("code"))))
            .build();
    }
}
