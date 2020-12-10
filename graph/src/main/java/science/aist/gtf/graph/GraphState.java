/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph;

import science.aist.jack.general.util.CastUtils;

import java.lang.reflect.Proxy;

/**
 * <p>Representation of a graph state</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface GraphState<V, E> extends GraphStateAccessor<V, E> {
    /**
     * Adds a vertex to the graph state
     *
     * @param vertex the vertex to add
     */
    void addVertex(Vertex<V, E> vertex);

    /**
     * Adds an edge between given source and target vertex
     *
     * @param edge   the edge to add
     * @param source the source vertex
     * @param target the target vertex
     * @throws IllegalStateException if the vertices were not added before
     */
    void addEdge(Edge<V, E> edge, Vertex<V, E> source, Vertex<V, E> target);

    /**
     * Returns a readonly instance of the given graph state
     *
     * @return the current graph state as ready only representation
     */
    default GraphStateAccessor<V, E> readonly() {
        // Creates a proxy of the current graph state object that basically just delegates the methods to the object
        // itself, but it avoids being able to cast it back to the graph state, nor does it allow to access the
        // variables via reflection, which makes the GraphState readonly
        return CastUtils.cast(Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{GraphStateAccessor.class},
                (proxy, method, args) -> method.invoke(this, args)));

    }
}
