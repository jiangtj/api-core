package com.jiangtj.utils.apicore.address;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
@ConfigurationProperties("addr")
public class AddressConfiguration {
    private String province = "https://raw.githubusercontent.com/modood/Administrative-divisions-of-China/master/dist/provinces.json";
    private String city = "https://raw.githubusercontent.com/modood/Administrative-divisions-of-China/master/dist/cities.json";
    private String area = "https://raw.githubusercontent.com/modood/Administrative-divisions-of-China/master/dist/areas.json";
    private String street = "https://raw.githubusercontent.com/modood/Administrative-divisions-of-China/master/dist/streets.json";
}
