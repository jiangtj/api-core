package com.jiangtj.utils.apicore.address;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("addr")
public class AddressConfiguration {
}
