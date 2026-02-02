package no.nav.fpsak.nare.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonOutput {

    private static final ObjectMapper MAPPER = createObjectMapper();


    private JsonOutput() {
    }

    public static String asJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw mapException(obj, e);
        }
    }

    public static String asCompactJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw mapException(obj, e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readerFor(clazz).readValue(json);
        } catch (JsonProcessingException e) {
            throw mapDeserializationException(json, e);
        }
    }

    private static NareJsonException mapException(Object obj, JsonProcessingException e) {
        return new NareJsonException(String.format("Kunne ikke serialiseres til json: %s", obj), e);
    }

    private static NareJsonException mapDeserializationException(String json, JsonProcessingException e) {
        return new NareJsonException(String.format("Kunne ikke deserialisere fra json: %s", json), e);
    }

    private static ObjectMapper createObjectMapper() {
        var om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.registerModule(new Jdk8Module());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
        om.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
        om.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
        om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        om.setVisibility(PropertyAccessor.CREATOR, Visibility.ANY);
        return om;
    }
}
