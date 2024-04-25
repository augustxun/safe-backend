package com.augustxun.safe.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class JacksonObjectMapper extends ObjectMapper {
    public JacksonObjectMapper() {
        super();
        this.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule simpleModule = new SimpleModule();

        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);

        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        simpleModule.addSerializer(long.class, ToStringSerializer.instance);

        registerModule(simpleModule);
    }
}
