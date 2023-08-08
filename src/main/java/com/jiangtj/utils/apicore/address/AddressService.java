package com.jiangtj.utils.apicore.address;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RegisterReflectionForBinding({Province.class, City.class, Area.class, AddressInfo.class})
public class AddressService {

    CSVFormat csvFormat;

    @Getter
    private final List<Province> provinces = new ArrayList<>();
    @Getter
    private final List<City> cities = new ArrayList<>();
    @Getter
    private final List<Area> areas = new ArrayList<>();

    private final Map<Integer, Province> codeToP = new ConcurrentHashMap<>();
    private final Map<Integer, City> codeToC = new ConcurrentHashMap<>();
    private final Map<Integer, Area> codeToA = new ConcurrentHashMap<>();
    private final Map<Integer, List<City>> cityGroup = new ConcurrentHashMap<>();
    private final Map<Integer, List<Area>> areaGroup = new ConcurrentHashMap<>();

    private final AddressConfiguration configuration;

    public AddressService (AddressConfiguration configuration) {
        this.configuration = configuration;
    }


    @PostConstruct
    public void setup() {
        csvFormat = CSVFormat.RFC4180.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
        try {
            initP();
            initC();
            initA();
            log.info("init address data complete !");
        } catch (Throwable e) {
            log.error("地址数据初始化失败！", e);
        }
    }

    public void initP() throws IOException {
        log.info("init province data...");
        ClassPathResource classPathResource = new ClassPathResource("address/provinces.csv");
        Reader in = new InputStreamReader(classPathResource.getInputStream());

        Iterable<CSVRecord> records = csvFormat.parse(in);
        for (CSVRecord record : records) {
            int code = Integer.parseInt(record.get("code"));
            String name = record.get("name");
            Province province = new Province(code, name);
            provinces.add(province);
            codeToP.put(code, province);
        }
    }

    public void initC() throws IOException {
        log.info("init city data...");
        ClassPathResource classPathResource = new ClassPathResource("address/cities.csv");
        Reader in = new InputStreamReader(classPathResource.getInputStream());

        Iterable<CSVRecord> records = csvFormat.parse(in);
        for (CSVRecord record : records) {
            int code = Integer.parseInt(record.get("code"));
            String name = record.get("name");
            int provinceCode = Integer.parseInt(record.get("provinceCode"));
            City city = new City(code, name, provinceCode);
            cities.add(city);
            codeToC.put(code, city);
            cityGroup.compute(provinceCode, (k, v) -> initListAndAdd(v, city));
        }
    }

    public void initA() throws IOException {
        log.info("init area data...");
        ClassPathResource classPathResource = new ClassPathResource("address/areas.csv");
        Reader in = new InputStreamReader(classPathResource.getInputStream());

        Iterable<CSVRecord> records = csvFormat.parse(in);
        for (CSVRecord record : records) {
            int code = Integer.parseInt(record.get("code"));
            String name = record.get("name");
            int provinceCode = Integer.parseInt(record.get("provinceCode"));
            int cityCode = Integer.parseInt(record.get("cityCode"));
            Area area = new Area(code, name, provinceCode, cityCode);
            areas.add(area);
            codeToA.put(code, area);
            areaGroup.compute(cityCode, (k, v) -> initListAndAdd(v, area));
        }
    }

    public <T> List<T> initListAndAdd(List<T> list, T v) {
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        list.add(v);
        return list;
    }

    public List<City> getCitiesByPCode(int code) {
        return cityGroup.get(code);
    }

    public List<Area> getAreasByPCode(int code) {
        return areaGroup.get(code);
    }

    public Optional<Province> getProvince(int code) {
        return Optional.of(codeToP.get(code));
    }

    public Optional<City> getCity(int code) {
        return Optional.of(codeToC.get(code));
    }

    public Optional<Area> getArea(int code) {
        return Optional.of(codeToA.get(code));
    }

    public AddressInfo getAddressInfo(int code) {
        Province province;
        City city = null;
        Area area = null;
        if (code < 100) {
            province = codeToP.get(code);
        } else if (code < 10000) {
            city = codeToC.get(code);
            province = codeToP.get(city.provinceCode());
        } else {
            area = codeToA.get(code);
            city = codeToC.get(area.cityCode());
            province = codeToP.get(area.provinceCode());
        }
        return new AddressInfo(province, city, area);
    }

}
