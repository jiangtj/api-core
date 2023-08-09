package com.jiangtj.utils.apicore.address;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class AddressServiceTest {

    @Autowired
    AddressService addressService;

    @Test
    void testLoad() {
        assertFalse(CollectionUtils.isEmpty(addressService.getProvinces()));
        assertFalse(CollectionUtils.isEmpty(addressService.getCities()));
        assertFalse(CollectionUtils.isEmpty(addressService.getAreas()));
    }
}