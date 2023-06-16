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
@RegisterReflectionForBinding({Province.class, City.class, Area.class, Street.class})
public class AddressService {

    private List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private List<Area> areas = new ArrayList<>();
    private List<Street> streets = new ArrayList<>();
    private Map<String, Province> nameToP = new ConcurrentHashMap<>();
    private Map<String, City> nameToC = new ConcurrentHashMap<>();
    private Map<String, Area> nameToA = new ConcurrentHashMap<>();
    private Map<String, Street> nameToS = new ConcurrentHashMap<>();
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

    public Mono<Void> requestAddressData() {
        WebClient client = WebClient.builder()
            .codecs(c -> c.defaultCodecs().maxInMemorySize(10485760))
            .build();
        return requestProvinceData(client)
            .then(requestCityData(client))
            .then(requestAreaData(client))
            .then(requestStreetData(client))
            .doOnNext(r -> log.info("update address complete."))
            .then();
    }

    public Mono<List<Province>> requestProvinceData(WebClient client) {
        log.info("update Province data...");
        return client.get().uri(configuration.getProvince())
            .retrieve()
            .bodyToMono(String.class)
            .map(jsonPlain -> JsonUtil.getListFromJson(jsonPlain, Province.class))
            .doOnNext(result -> {
                provinces = result;
                nameToP = result.stream().collect(Collectors.toConcurrentMap(Province::code, Function.identity()));
            });
    }

    public Mono<List<City>> requestCityData(WebClient client) {
        log.info("update City data...");
        return client.get().uri(configuration.getCity())
            .retrieve()
            .bodyToMono(String.class)
            .map(jsonPlain -> JsonUtil.getListFromJson(jsonPlain, City.class))
            .doOnNext(result -> {
                cities = result;
                nameToC = result.stream().collect(Collectors.toConcurrentMap(City::code, Function.identity()));
                cityMap = result.stream().collect(Collectors.groupingByConcurrent(City::provinceCode, Collectors.mapping(
                    Function.identity(),
                    Collectors.toList()
                )));
            });
    }

    public Mono<List<Area>> requestAreaData(WebClient client) {
        log.info("update Area data...");
        return client.get().uri(configuration.getArea())
            .retrieve()
            .bodyToMono(String.class)
            .map(jsonPlain -> JsonUtil.getListFromJson(jsonPlain, Area.class))
            .doOnNext(result -> {
                areas = result;
                nameToA = result.stream().collect(Collectors.toConcurrentMap(Area::code, Function.identity()));
                areaMap = result.stream().collect(Collectors.groupingByConcurrent(Area::cityCode, Collectors.mapping(
                    Function.identity(),
                    Collectors.toList()
                )));
            });
    }

    public Mono<List<Street>> requestStreetData(WebClient client) {
        log.info("update Street data...");
        return client.get().uri(configuration.getStreet())
            .retrieve()
            .bodyToMono(String.class)
            .map(jsonPlain -> JsonUtil.getListFromJson(jsonPlain, Street.class))
            .doOnNext(result -> {
                streets = result;
                nameToS = result.stream().collect(Collectors.toConcurrentMap(Street::code, Function.identity()));
                streetMap = result.stream().collect(Collectors.groupingByConcurrent(Street::areaCode, Collectors.mapping(
                    Function.identity(),
                    Collectors.toList()
                )));
            });
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

    public AddressInfo getAddressInfo(String code) {
        int length = code.length();
        Province province;
        City city = null;
        Area area = null;
        Street street = null;
        switch (length) {
            case 2 :
                province = nameToP.get(code);
                break;
            case 4 :
                city = nameToC.get(code);
                province = nameToP.get(city.provinceCode());
                break;
            case 6 :
                area = nameToA.get(code);
                city = nameToC.get(area.cityCode());
                province = nameToP.get(area.provinceCode());
                break;
            default:
                street = nameToS.get(code);
                area = nameToA.get(street.areaCode());
                city = nameToC.get(street.cityCode());
                province = nameToP.get(street.provinceCode());
        }
        return new AddressInfo(province, city, area, street);
    }
}
