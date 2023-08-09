package com.jiangtj.utils.apicore.address;

import java.io.Serializable;

public record AddressInfo(Province province, City city, Area area) implements Serializable {
}
