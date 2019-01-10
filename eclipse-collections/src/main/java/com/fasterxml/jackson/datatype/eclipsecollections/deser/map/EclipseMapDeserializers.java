package com.fasterxml.jackson.datatype.eclipsecollections.deser.map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.primitive.PrimitiveObjectMap;
import com.fasterxml.jackson.datatype.eclipsecollections.deser.map.TypeHandlerPair;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yawkat
 */
public final class EclipseMapDeserializers {
    private static final Map<Class<?>, Entry<?, ?, ?, ?>> ENTRIES = new IdentityHashMap<>();

    private EclipseMapDeserializers() {
    }

    public static EclipseMapDeserializer<?, ?, ?, ?> createDeserializer(JavaType type) {
        Class<?> rawClass = type.getRawClass();
        Entry<?, ?, ?, ?> entry = ENTRIES.get(rawClass);
        if (entry == null) { return null; }

        return entry.createDeserializer(type);
    }

    static <T, K extends KeyHandler<K>, V extends ValueHandler<V>> void add(
            Class<T> type,
            TypeHandlerPair<? extends T, K, V> handlerPair
    ) {
        ENTRIES.put(type, new Entry<>(handlerPair, null));
    }

    static <T, I> void add(
            Class<T> type,
            TypeHandlerPair<I, ?, ?> handlerPair,
            Function<I, T> finish
    ) {
        ENTRIES.put(type, new Entry<>(handlerPair, finish));
    }

    private static final class Entry<T, I, K extends KeyHandler<K>, V extends ValueHandler<V>> {
        final TypeHandlerPair<I, K, V> typeHandlerPair;
        // null if this is the identity function
        final Function<I, T> finish;

        Entry(TypeHandlerPair<I, K, V> typeHandlerPair, Function<I, T> finish) {
            this.typeHandlerPair = typeHandlerPair;
            this.finish = finish;
        }

        EclipseMapDeserializer<T, I, K, V> createDeserializer(JavaType type) {
            Class<?> rawClass = type.getRawClass();
            List<JavaType> typeParameters = type.getBindings().getTypeParameters();
            boolean refValue = PrimitiveObjectMap.class.isAssignableFrom(rawClass) ||
                               MapIterable.class.isAssignableFrom(rawClass);
            boolean refKey = refValue ? (typeParameters.size() == 2) : (typeParameters.size() == 1);

            K keyHandler = typeHandlerPair.keyHandler(refKey ? typeParameters.get(0) : null);
            V valueHandler = typeHandlerPair.valueHandler(refValue ? typeParameters.get(typeParameters.size() - 1) : null);

            return new DeserializerImpl(keyHandler, valueHandler);
        }

        class DeserializerImpl extends EclipseMapDeserializer<T, I, K, V> {
            DeserializerImpl(K keyHandler, V valueHandler) {
                super(keyHandler, valueHandler);
            }

            @Override
            protected EclipseMapDeserializer<T, ?, ?, ?> withResolved(K keyHandler, V valueHandler) {
                return new DeserializerImpl(keyHandler, valueHandler);
            }

            @Override
            protected I createIntermediate() {
                return typeHandlerPair.createEmpty();
            }

            @Override
            protected void deserializeEntry(
                    I target,
                    K keyHandler,
                    V valueHandler,
                    DeserializationContext ctx,
                    String key,
                    JsonParser valueParser
            ) throws IOException {
                typeHandlerPair.add(target, keyHandler, valueHandler, ctx, key, valueParser);
            }

            @SuppressWarnings("unchecked")
            @Override
            protected T finish(I intermediate) {
                //noinspection unchecked
                return finish == null ? (T) intermediate : finish.apply(intermediate);
            }
        }
    }

    static {
        TypeHandlerPair.addDeserializers();
    }
}
