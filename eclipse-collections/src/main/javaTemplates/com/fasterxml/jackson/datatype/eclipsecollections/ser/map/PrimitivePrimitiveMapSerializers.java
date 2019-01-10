package com.fasterxml.jackson.datatype.eclipsecollections.ser.map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

import com.fasterxml.jackson.datatype.primitive_collections_base.ser.map.PrimitiveMapSerializer;
import org.eclipse.collections.api.PrimitiveIterable;
import org.eclipse.collections.api.map.primitive.*;

import static com.fasterxml.jackson.datatype.eclipsecollections.ser.map.PrimitiveRefMapSerializers.rethrowUnchecked;

/**
 * @author yawkat
 */
@SuppressWarnings({"Duplicates", "serial"})
public final class PrimitivePrimitiveMapSerializers {
    private PrimitivePrimitiveMapSerializers() {
    }

    public static Map<Class<? extends PrimitiveIterable>, PrimitiveMapSerializer<?>> getInstances() {
        return INSTANCES;
    }

    private static final Map<Class<? extends PrimitiveIterable>, PrimitiveMapSerializer<?>> INSTANCES;

    // used because the lambda passed to forEachKeyValue can't throw.
    @SuppressWarnings("unchecked")
    static <E extends Throwable> void rethrowUnchecked(IOException e) throws E {
        throw (E) e;
    }

    /* with char|byte|short|int|long|float|double key */
    public static final PrimitiveMapSerializer<CharBooleanMap> CHAR_BOOLEAN =
            new PrimitiveMapSerializer<CharBooleanMap>(CharBooleanMap.class) {
                @Override
                protected void serializeEntries(CharBooleanMap value, JsonGenerator gen, SerializerProvider serializers) {
                    value.forEachKeyValue((k, v) -> {
                        try {
                            gen.writeFieldName(String.valueOf(k));
                            gen.writeBoolean(v);
                        } catch (IOException e) {
                            rethrowUnchecked(e);
                        }
                    });
                }
            };
    /* endwith */

    /* with
        char|byte|short|int|long|float|double key
        int|byte|char|short|long|float|double value
    */
    public static final PrimitiveMapSerializer<CharIntMap> CHAR_INT =
            new PrimitiveMapSerializer<CharIntMap>(CharIntMap.class) {
                @Override
                protected void serializeEntries(CharIntMap value, JsonGenerator gen, SerializerProvider serializers) {
                    value.forEachKeyValue((k, v) -> {
                        try {
                            gen.writeFieldName(String.valueOf(k));
                            /* if !(char value) */
                            gen.writeNumber(v);
                            /* elif char value //
                            gen.writeString(new // with not key //char// endwith //[]{v}, 0, 1);
                            // endif */
                        } catch (IOException e) {
                            rethrowUnchecked(e);
                        }
                    });
                }
            };
    /* endwith */

    static {
        Map<Class<? extends PrimitiveIterable>, PrimitiveMapSerializer<?>> instances =
                new IdentityHashMap<>();
        /* with char|byte|short|int|long|float|double key */
        instances.put(CharBooleanMap.class, CHAR_BOOLEAN);
        /* endwith */

        /* with
            char|byte|short|int|long|float|double key
            int|byte|char|short|long|float|double value
        */
        instances.put(CharIntMap.class, CHAR_INT);
        /* endwith */
        INSTANCES = Collections.unmodifiableMap(instances);
    }
}
