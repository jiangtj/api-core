package com.jiangtj.utils.apicore.address;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AddressRouter {

    @Bean
    public RouterFunction<ServerResponse> addressRoutes(AddressService addressService) {
        return RouterFunctions.route()

            .GET("address/provinces", request -> ServerResponse.ok().bodyValue(addressService.getProvinces()))
            .GET("address/cities", request -> ServerResponse.ok().bodyValue(addressService.getCities()))
            .GET("address/areas", request -> ServerResponse.ok().bodyValue(addressService.getAreas()))

            .GET("address/cities/{code}", request -> ServerResponse.ok().bodyValue(
                    addressService.getCitiesByPCode(Integer.parseInt(request.pathVariable("code")))))
            .GET("address/areas/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getAreasByPCode(Integer.parseInt(request.pathVariable("code")))))
            .GET("address/{code}", request -> ServerResponse.ok().bodyValue(
                addressService.getAddressInfo(Integer.parseInt(request.pathVariable("code")))))

            .build();
    }
}
