package com.jiangtj.utils.apicore.address;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record Area(String code, String name, String cityCode, String provinceCode) {
}
