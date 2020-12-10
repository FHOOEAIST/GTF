/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.builder;

import science.aist.gtf.graph.MetaTag;

/**
 * <p>Edge Builder is used inside the {@link GraphBuilder} and is responsible to create a fluent way on how to
 * create an edge between two vertices</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface EdgeBuilder<V, E> {
    /**
     * Create an edge to the vertex that decorates value.
     *
     * @param value the value of the decorated vertex
     * @return the graph original graph builder
     */
    GraphBuilder<V, E> to(V value);

    /**
     * Creates an edge to the vertex that is identified by the key.
     *
     * @param key the key which identifies the vertex
     * @return the graph original graph builder
     * @throws IllegalStateException if no vertex with given key was found
     */
    GraphBuilder<V, E> toByKey(Object key);

    /**
     * Creates an edge to the vertex that decorates value and returns an {@link EdgeDataBuilder} that allows to add a
     * value to the created edge.
     *
     * @param value the value of the decorated vertex
     * @return a {@link EdgeDataBuilder}
     */
    EdgeDataBuilder<V, E> toData(V value);

    /**
     * Creates an edge to the vertex that is identified by the key and returns an {@link EdgeDataBuilder} that allows to
     * add a value to the created edge.
     *
     * @param key the key which identifies the vertex
     * @return a {@link EdgeDataBuilder}
     * @throws IllegalStateException if no vertex with given key was found
     */
    EdgeDataBuilder<V, E> toDataByKey(Object key);

    /**
     * Creates an edge to the vertex that decorates value and returns an {@link EdgeWithBuilder} that allows to add a
     * callback method, that is called with the created edge. This allows to e.g. add {@link MetaTag}s.
     *
     * @param value the value of the decorated vertex
     * @return a {@link EdgeWithBuilder}
     */
    EdgeWithBuilder<V, E> toWith(V value);

    /**
     * Creates an edge to the vertex that is identified by the key and returns an {@link EdgeWithBuilder} that allows to
     * add a callback method, that is called with the created edge. This allows to e.g. add {@link MetaTag}s.
     *
     * @param key the key which identifies the vertex
     * @return a {@link EdgeWithBuilder}
     * @throws IllegalStateException if no vertex with given key was found
     */
    EdgeWithBuilder<V, E> toWithByKey(Object key);
}
