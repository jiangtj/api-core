package com.jiangtj.utils.apicore.address;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record Province(String code, String name) {
}
