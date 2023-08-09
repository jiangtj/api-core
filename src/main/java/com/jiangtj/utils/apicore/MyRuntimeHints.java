package com.jiangtj.utils.apicore;

import com.jiangtj.utils.apicore.address.Area;
import com.jiangtj.utils.apicore.address.City;
import com.jiangtj.utils.apicore.address.Province;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class MyRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        // Register resources
        hints.resources().registerPattern("address/*.csv");

        // Register serialization
        hints.serialization().registerType(Province.class);
        hints.serialization().registerType(City.class);
        hints.serialization().registerType(Area.class);
    }

}
