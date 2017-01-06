package com.github.xdcrafts.flower.core.spring.example.email;


import com.github.xdcrafts.flower.spring.MethodConverter;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;
import java.util.function.Function;

/**
 * EmailRequestValidator to Function<Map, Map> converter.
 */
public class EmailRequestValidatorMethodConverter implements MethodConverter<EmailRequestValidator> {

    @Override
    public Class<EmailRequestValidator> convertibleClass() {
        return EmailRequestValidator.class;
    }

    @Override
    public String methodName() {
        return "validate";
    }

    @Override
    public Function<Map, Map> convert(EmailRequestValidator source) {
        return ctx -> {
            source.validate(ctx);
            return ctx;
        };
    }
}
