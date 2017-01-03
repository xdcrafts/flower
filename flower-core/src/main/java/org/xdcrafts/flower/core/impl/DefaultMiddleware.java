package org.xdcrafts.flower.core.impl;

import org.xdcrafts.flower.core.Middleware;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Default implementation of Middleware.
 */
public class DefaultMiddleware implements Middleware {

    private final String name;
    private final BiFunction<Map<String, Object>, Function<Map, Map>, Function<Map, Map>> body;

    public DefaultMiddleware(
        String name,
        BiFunction<Map<String, Object>, Function<Map, Map>, Function<Map, Map>> body
    ) {
        this.name = name;
        this.body = body;
    }

    @Override
    public Function<Map, Map> apply(Map<String, Object> stringObjectMap, Function<Map, Map> mapMapFunction) {
        return this.body.apply(stringObjectMap, mapMapFunction);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "DefaultMiddleware{"
                + "name='" + name + '\''
                + ", body=" + body
                + '}';
    }
}
