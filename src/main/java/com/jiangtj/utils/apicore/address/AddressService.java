package com.jiangtj.utils.apicore.address;

import com.jiangtj.utils.apicore.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressService {

    private List<Province> provinces = new ArrayList<>();
    private Map<String, List<City>> cityMap = new ConcurrentHashMap<>();
    private Map<String, List<Area>> areaMap = new ConcurrentHashMap<>();
    private Map<String, List<Street>> streetMap = new ConcurrentHashMap<>();

    private final AddressConfiguration configuration;

    public AddressService (AddressConfiguration configuration) {
        this.configuration = configuration;
    }


    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    public void updateAddressData() {
        requestAddressData().block();
    }

    @RegisterReflectionForBinding({Province.class, City.class, Area.class, Street.class})
    public Mono<Void> requestAddressData() {
        log.info("update Province data...");
        WebClient client = WebClient.builder()
            .codecs(c -> c.defaultCodecs().maxInMemorySize(10485760))
            .build();

        return client.get().uri(configuration.getProvince())
            .retrieve()
            .bodyToMono(String.class)
            .map(jsonPlain -> JsonUtil.getListFromJson(jsonPlain, Province.class))
            .doOnNext(result -> provinces = result)
            .doOnNext(r -> log.info("update City data..."))
            .then(client.get().uri(configuration.getCity())
                .retrieve()
                .bodyToMono(String.class))
            .map(jsonPlain -> JsonUtil.getListFromJson(jsonPlain, City.class)
                .stream()
                .collect(Collectors.groupingByConcurrent(City::provinceCode, Collectors.mapping(
                    Function.identity(),
                    Collectors.toList()
                ))))
            .doOnNext(result -> cityMap = result)
            .doOnNext(r -> log.info("update Area data..."))
            .then(client.get().uri(configuration.getArea())
                .retrieve()
                .bodyToMono(String.class))
            .map(jsonPlain -> JsonUtil.getListFromJson(jsonPlain, Area.class)
                .stream()
                .collect(Collectors.groupingByConcurrent(Area::cityCode, Collectors.mapping(
                    Function.identity(),
                    Collectors.toList()
                ))))
            .doOnNext(result -> areaMap = result)
            .doOnNext(r -> log.info("update Street data..."))
            .then(client.get().uri(configuration.getStreet())
                .retrieve()
                .bodyToMono(String.class))
            .map(jsonPlain -> JsonUtil.getListFromJson(jsonPlain, Street.class)
                .stream()
                .collect(Collectors.groupingByConcurrent(Street::areaCode, Collectors.mapping(
                    Function.identity(),
                    Collectors.toList()
                ))))
            .doOnNext(result -> streetMap = result)
            .doOnNext(r -> log.info("update address complete."))
            .then();
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public Map<String, List<City>> getCityMap() {
        return cityMap;
    }

    public Map<String, List<Area>> getAreaMap() {
        return areaMap;
    }

    public Map<String, List<Street>> getStreetMap() {
        return streetMap;
    }
}