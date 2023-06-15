package com.jiangtj.utils.apicore.address;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record City(String code, String name, String provinceCode) {
}
