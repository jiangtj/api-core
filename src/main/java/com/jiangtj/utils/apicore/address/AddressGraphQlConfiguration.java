package com.jiangtj.utils.apicore.address;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class AddressGraphQlConfiguration {

    @Bean
    RuntimeWiringConfigurer customWiringConfigurer(AddressService addressService) {
        return wiringBuilder -> wiringBuilder
                .type("Query", builder -> builder
                        .dataFetcher("provinces", environment -> {
                            return addressService.getProvinces();
                        })
                        .dataFetcher("province", environment -> {
                            String code = environment.getArgument("code");
                            return addressService.getProvince(Integer.parseInt(code)).orElseThrow();
                        })
                        .dataFetcher("city", environment -> {
                            String code = environment.getArgument("code");
                            return addressService.getCity(Integer.parseInt(code)).orElseThrow();
                        })
                        .dataFetcher("area", environment -> {
                            String code = environment.getArgument("code");
                            return addressService.getArea(Integer.parseInt(code)).orElseThrow();
                        }))
                .type("Province", builder -> builder
                        .dataFetcher("cities", environment -> {
                            Province source = environment.getSource();
                            return addressService.getCitiesByPCode(source.code());
                        }))
                .type("City", builder -> builder
                        .dataFetcher("areas", environment -> {
                            City source = environment.getSource();
                            return addressService.getAreasByPCode(source.code());
                        }));
    }

}
