/*
 * Project GreenBox
 * (c) 2015-2018 VMware, Inc. All rights reserved.
 * VMware Confidential.
 */

package com.nischit.myexp.webflux.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance;

public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeFactory TYPE_FACTORY = defaultInstance();

    private JsonUtils() {
    }

    public static String convertToJson(final Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }

    public static String convertFileToJsonString(final String fileName) {
        byte[] input = new byte[100];
        int bytesRead;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try(final InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            while((bytesRead = inputStream.read(input)) != -1){
                outputStream.write(input, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw new SerializerException(ex);
        }
        return outputStream.toString();
    }
}
