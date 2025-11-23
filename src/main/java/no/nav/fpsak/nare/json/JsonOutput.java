package no.nav.fpsak.nare.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.TimeZone;

public class JsonOutput {

    private static final JsonMapper MAPPER = createJsonMapperBuilder().build();

    private JsonOutput() {
    }

    public static JsonMapper getJsonMapper() {
        return MAPPER;
    }

    public static String asJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JacksonException e) {
            throw mapException(obj, e);
        }
    }

    public static String asCompactJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JacksonException e) {
            throw mapException(obj, e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JacksonException e) {
            throw mapException(json, e);
        }
    }

    private static NareJsonException mapException(Object obj, JacksonException e) {
        return new NareJsonException(String.format("Kunne ikke (de)serialiseres: %s", obj), e);
    }

    private static JsonMapper.Builder createJsonMapperBuilder() {
        return JsonMapper.builder()
                .defaultTimeZone(TimeZone.getTimeZone("Europe/Oslo"))
                .disable(tools.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES) // Var noen tester med null for booleans
                .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .changeDefaultVisibility(v -> v
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.ANY)
                        .withScalarConstructorVisibility(JsonAutoDetect.Visibility.ANY));
    }
}
