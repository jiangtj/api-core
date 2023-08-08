package com.jiangtj.utils.apicore.address;

import java.io.Serializable;

public record Area(int code, String name, int cityCode, int provinceCode) implements Serializable {
}
