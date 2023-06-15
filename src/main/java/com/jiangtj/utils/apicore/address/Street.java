package com.jiangtj.utils.apicore.address;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record Street(String code, String name, String areaCode, String cityCode, String provinceCode) {
}
