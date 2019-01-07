package com.fasterxml.jackson.datatype.eclipsecollections.deser.map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import java.io.IOException;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.*;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.primitive.*;

/**
 * @author yawkat
 */
@SuppressWarnings("NewMethodNamingConvention")
interface TypeHandlerPair<M, K extends KeyHandler<K>, V extends ValueHandler<V>> {
    K keyHandler(JavaType type);

    V valueHandler(JavaType type);

    M createEmpty();

    void add(M target, K kh, V vh, DeserializationContext ctx, String k, JsonParser v)
            throws IOException;

    // Boolean-valued map types are generated separately because JPSG doesn't support boolean
    // specialization

    /* with char|byte|short|int|long|float|double|obj key */

    /* define KeyHandlerType //
    // if obj key //RefKeyHandler// elif !(obj key) //PrimitiveKVHandler.Char// endif //
    // enddefine */

    TypeHandlerPair<MutableCharBooleanMap,
            /*KeyHandlerType*/PrimitiveKVHandler.Char/**/,
            PrimitiveKVHandler.Boolean> CHAR_BOOLEAN =
            new TypeHandlerPair<MutableCharBooleanMap,
                    /*KeyHandlerType*/PrimitiveKVHandler.Char/**/,
                    PrimitiveKVHandler.Boolean>() {
                @Override
                public /*KeyHandlerType*/PrimitiveKVHandler.Char/**/ keyHandler(JavaType type) {
                    return /* if !(obj key) */PrimitiveKVHandler.Char.INSTANCE
                            /* elif obj key //new RefKeyHandler(type, null)// endif */;
                }

                @Override
                public PrimitiveKVHandler.Boolean valueHandler(JavaType type) {
                    return PrimitiveKVHandler.Boolean.INSTANCE;
                }

                @Override
                public MutableCharBooleanMap createEmpty() {
                    return CharBooleanMaps.mutable.empty();
                }

                @Override
                public void add(
                        MutableCharBooleanMap target,
                        /*KeyHandlerType*/PrimitiveKVHandler.Char/**/ kh,
                        PrimitiveKVHandler.Boolean vh,
                        DeserializationContext ctx, String k, JsonParser v
                ) throws IOException {
                    target.put(kh.key(ctx, k), vh.value(ctx, v));
                }
            };

    /* endwith */

    // All other map types

    /* with
        char|byte|short|int|long|float|double|obj key
        int|short|byte|char|long|float|double|obj value
    */

    /* define MapType //
    // if obj key obj value //MutableMap<Object, Object>
    // elif obj key //MutableObjectIntMap<Object>
    // elif obj value //MutableCharObjectMap<Object>
    // elif !(obj key) && !(obj value) //MutableCharIntMap
    // endif //
    // enddefine */

    /* define KeyHandlerType //
    // if obj key //RefKeyHandler// elif !(obj key) //PrimitiveKVHandler.Char// endif //
    // enddefine */

    /* define ValueHandlerType //
    // if obj value //RefValueHandler// elif !(obj value) //PrimitiveKVHandler.Int// endif //
    // enddefine */

    TypeHandlerPair</*MapType*/MutableCharIntMap/**/,
            /*KeyHandlerType*/PrimitiveKVHandler.Char/**/,
            /*ValueHandlerType*/PrimitiveKVHandler.Int/**/> CHAR_INT =
            new TypeHandlerPair</*MapType*/MutableCharIntMap/**/,
                    /*KeyHandlerType*/PrimitiveKVHandler.Char/**/,
                    /*ValueHandlerType*/PrimitiveKVHandler.Int/**/>() {
                @Override
                public /*KeyHandlerType*/PrimitiveKVHandler.Char/**/ keyHandler(JavaType type) {
                    return /* if !(obj key) */PrimitiveKVHandler.Char.INSTANCE
                            /* elif obj key //new RefKeyHandler(type, null)// endif */;
                }

                @Override
                public /*ValueHandlerType*/PrimitiveKVHandler.Int/**/ valueHandler(JavaType type) {
                    return /* if !(obj value) */PrimitiveKVHandler.Int.INSTANCE
                            /* elif obj value //new RefValueHandler(type, null, null)// endif */;
                }

                @Override
                public /*MapType*/MutableCharIntMap/**/ createEmpty() {
                    return /* if !(obj key obj value) */CharIntMaps
                            /* elif obj key obj value //Maps// endif */.mutable.empty();
                }

                @Override
                public void add(
                        /*MapType*/MutableCharIntMap/**/ target,
                        /*KeyHandlerType*/PrimitiveKVHandler.Char/**/ kh,
                        /*ValueHandlerType*/PrimitiveKVHandler.Int/**/ vh,
                        DeserializationContext ctx, String k, JsonParser v
                ) throws IOException {
                    target.put(kh.key(ctx, k), vh.value(ctx, v));
                }
            };

    /* endwith */
}
