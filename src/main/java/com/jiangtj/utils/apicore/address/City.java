package com.jiangtj.utils.apicore.address;

import java.io.Serializable;

public record City(int code, String name, int provinceCode) implements Serializable {
}
