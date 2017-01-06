package com.github.xdcrafts.flower.core.spring.example.email;


import org.springframework.core.convert.converter.Converter;

import java.util.Map;
import java.util.function.Function;

/**
 * EmailRequestValidator to Function<Map, Map> converter.
 */
public class EmailRequestValidatorToFunctionConverter implements Converter<EmailRequestValidator, Function<Map, Map>> {
    @Override
    public Function<Map, Map> convert(EmailRequestValidator source) {
        return source::validate;
    }
}
