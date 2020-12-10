/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation;

import lombok.experimental.UtilityClass;
import science.aist.gtf.graph.Graph;
import science.aist.jack.general.util.CastUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>Well this class is necessary, because Java generics is the greatest nonsense which was ever developed ...</p>
 * <p>This class encapsulates all cast magic and then suppresses the unchecked cast message.</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@UtilityClass
public class TransformerUtils {
    /**
     * This method extracts the generic types from instantiated classes, which directly overwrite the generic parameters
     * of its parent class. e.g. if there is a class {@code ArrayList<T>} and the class is then extended by e.g.
     * <code>MyArrayList extends ArrayList&lt;String&gt;</code> then this method can extract the generic argument of
     * this class, which would be in that specific case be string, and the method class would result in something like:
     * <pre>getTypeArgument(MyArrayList.class, 0) = Class&lt;String&gt;</pre>
     *
     * @param clazz the class where the generic type should be extracted
     * @param idx   the index of the generic type
     * @param <T>   the type of the result class
     * @param <S>   the type of tine input class
     * @return a class which represents the type argument of clazz at idx position. There is also a special case, if the
     * GenericType is a ParameterizedType of the type Graph, then the generic argument from the graph is returned.
     * @throws RuntimeException if the type argument cannot be extracted from the class
     */
    public <T, S> Class<T> getTypeArgument(Class<S> clazz, int idx) {
        // Extract the Type Argument at a given index.
        Type t = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[idx];
        Type res;
        // Check if it is a parameterized type
        if (t instanceof ParameterizedType) {
            // cast it to the parameterized type class
            ParameterizedType pt = (ParameterizedType) t;
            // Check if it is a Graph class
            if (pt.getTypeName().startsWith(Graph.class.getName())) {
                // then return the first parameter of the graph type
                res = pt.getActualTypeArguments()[0];
            } else {
                // else return the class type of the parameterized type
                res = pt.getRawType();
            }
        } else {
            res = t;
        }
        // Finally, cast it to the class
        return CastUtils.cast(res);
    }

}
