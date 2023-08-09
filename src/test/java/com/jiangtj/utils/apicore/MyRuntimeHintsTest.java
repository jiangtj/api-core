package com.jiangtj.utils.apicore;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.predicate.RuntimeHintsPredicates;

import static org.assertj.core.api.Assertions.assertThat;

class MyRuntimeHintsTest {

    @Test
    void registerHints() {
        RuntimeHints hints = new RuntimeHints();
        new MyRuntimeHints().registerHints(hints, getClass().getClassLoader());
        assertThat(RuntimeHintsPredicates.resource().forResource("address/provinces.csv")).accepts(hints);
        assertThat(RuntimeHintsPredicates.resource().forResource("address/cities.csv")).accepts(hints);
        assertThat(RuntimeHintsPredicates.resource().forResource("address/areas.csv")).accepts(hints);
    }
}