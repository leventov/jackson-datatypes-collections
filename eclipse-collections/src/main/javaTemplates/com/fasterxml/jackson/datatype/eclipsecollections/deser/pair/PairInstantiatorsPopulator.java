package com.fasterxml.jackson.datatype.eclipsecollections.deser.pair;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.eclipse.collections.api.tuple.primitive.*;
import org.eclipse.collections.impl.tuple.primitive.PrimitiveTuples;

import java.util.function.Function;

import static com.fasterxml.jackson.datatype.eclipsecollections.deser.pair.PairInstantiators.*;

public class PairInstantiatorsPopulator {

    static void populate() {
        // boolean -> numeric primitive
        /* with char|byte|short|int|long|float|double key */
        purePrimitiveInstantiator(BooleanCharPair.class, boolean.class, char.class,
                (one, two) -> PrimitiveTuples.pair((boolean) one, (char) two));
        /* endwith */
        // numeric primitive -> boolean
        /* with char|byte|short|int|long|float|double value */
        purePrimitiveInstantiator(CharBooleanPair.class, char.class, boolean.class,
                (one, two) -> PrimitiveTuples.pair((char) one, (boolean) two));
        /* endwith */
        // numeric primitive -> numeric primitive
        /* with
            char|byte|short|int|long|float|double key
            int|byte|short|char|long|float|double value
        */
        purePrimitiveInstantiator(CharIntPair.class, char.class, int.class,
                (one, two) -> PrimitiveTuples.pair((char) one, (int) two));
        /* endwith */

        // boolean -> object
        Function<JavaType, ValueInstantiator> booleanObjectPairLambda = (JavaType beanType) ->
                primitiveObjectInstantiator(beanType, boolean.class,
                        (one, two) -> PrimitiveTuples.pair((boolean) one, two));
        PairInstantiators.add(BooleanObjectPair.class, booleanObjectPairLambda);
        // numeric primitive -> object
        /* with char|byte|short|int|long|float|double key */
        Function<JavaType, ValueInstantiator> charObjectPairLambda = (JavaType beanType) ->
                primitiveObjectInstantiator(beanType, char.class,
                        (one, two) -> PrimitiveTuples.pair((char) one, two));
        PairInstantiators.add(CharObjectPair.class, charObjectPairLambda);
        /* endwith */

        // object -> boolean
        Function<JavaType, ValueInstantiator> objectBooleanPairLambda = (JavaType beanType) ->
                objectPrimitiveInstantiator(beanType, boolean.class,
                        (one, two) -> PrimitiveTuples.pair(one, (boolean) two));
        PairInstantiators.add(ObjectBooleanPair.class, objectBooleanPairLambda);
    }

    private PairInstantiatorsPopulator() {}
}
