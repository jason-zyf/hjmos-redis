package com.pci.hjmos.redis.config;

import com.pci.hjmos.redis.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Log4j2
public class DefaultSerializer implements RedisSerializer<Object> {

    private static final byte[] EMPTY_ARRAY = new byte[0];

    private final Charset charset;

    public DefaultSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public DefaultSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        byte[] byteArray = null;

        if (null == object) {
            byteArray = EMPTY_ARRAY;
        } else {
            try {
                if (object instanceof String){
                    String str = (String) object;
                    return str.getBytes(charset);
                }

                String jsonString = JsonUtils.toJsonStr(object);

                return (jsonString == null ? EMPTY_ARRAY : jsonString.getBytes(charset));
            } catch (Exception ex) {
                log.error("serialize has a error, reason -> {}", ex.getMessage());
                byteArray = EMPTY_ARRAY;
            }
        }
        return byteArray;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        Object object = null;

        if (bytes == null || bytes.length == 0) {
            return object;
        } else {
            try {
                object = new String(bytes, charset);
                return object;
            } catch (Exception ex) {
                log.error("deserialize has a error, reason -> {}", ex.getMessage());
            }
        }

        return object;
    }
}
