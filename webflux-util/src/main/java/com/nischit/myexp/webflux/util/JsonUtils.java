package com.nischit.myexp.webflux.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Util to translate object to json string or vice versa.
 */
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int BYTE_LEN = 100;

    private JsonUtils() {
    }

    /**
     * Converts to json string.
     *
     * @param obj Represents the object to be converted.
     * @return A json string.
     */
    public static String convertToJson(final Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (final IOException e) {
            throw new SerializerException(e);
        }
    }

    /**
     * Converts file to json string.
     *
     * @param fileName File name in the classpath.
     * @return A json string.
     */
    public static String convertFileToJsonString(final String fileName) {
        final byte[] input = new byte[BYTE_LEN];
        int bytesRead;
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            while ((bytesRead = inputStream.read(input)) != -1) {
                outputStream.write(input, 0, bytesRead);
            }
        } catch (final IOException ex) {
            throw new SerializerException(ex);
        }
        return outputStream.toString();
    }
}
