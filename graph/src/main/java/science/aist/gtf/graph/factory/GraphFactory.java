/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.factory;

import science.aist.gtf.graph.*;

/**
 * <p>Interface for a graph factory</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public interface GraphFactory {
    /**
     * Returns a new instance of a graph state
     *
     * @param <V> the type of the decorated vertex value
     * @param <E> the type of the decorated edge value
     * @return a new graph state instance
     */
    <V, E> GraphState<V, E> createGraphState();

    /**
     * Returns a new instance of a graph
     *
     * @param <V>        the type of the decorated vertex value
     * @param <E>        the type of the decorated edge value
     * @param graphState the internal data state representation of the graph
     * @return the created graph
     */
    <V, E> Graph<V, E> createGraph(GraphStateAccessor<V, E> graphState);

    /**
     * Returns a new instance of a vertex with elem as decorated value
     *
     * @param elem the value to be decorated by the vertex
     * @param <V>  the type of the decorated vertex value
     * @param <E>  the type of the decorated edge value
     * @return the created vertex
     */
    <V, E> Vertex<V, E> createVertex(V elem);

    /**
     * Returns a new instance of a Vertex view
     *
     * @param viewedVertex the vertex onto which the view should be created
     * @param <V>          the type of the decorated vertex value
     * @param <E>          the type of the decorated edge value
     * @return the created vertex view
     */
    <V, E> VertexView<V, E> createVertexView(Vertex<V, E> viewedVertex);

    /**
     * Returns a new instance of an edge view
     *
     * @param viewedEdge the edge onto which the view should be created
     * @param <V>        the type of the decorated vertex value
     * @param <E>        the type of the decorated edge value
     * @return the created edge view
     */
    <V, E> EdgeView<V, E> createEdgeView(Edge<V, E> viewedEdge);

    /**
     * Returns a new instance of an edge
     *
     * @param <V>  the type of the decorated vertex value
     * @param <E>  the type of the decorated edge value
     * @param elem the decorated element
     * @return the created edge
     */
    <V, E> Edge<V, E> createEdge(E elem);

    /**
     * Returns a new instance of a meta tag
     *
     * @param <T> the type of the meta tag value
     * @return the created meta tag
     */
    <T> MetaTag<T> createMetaTag();

    /**
     * Returns a new instance of a meta tag with key and value set
     *
     * @param key   the key value
     * @param value the value
     * @param <T>   the type of the value
     * @return the resulting meta tag
     */
    default <T> MetaTag<T> createMetaTag(String key, T value) {
        MetaTag<T> metaTag = createMetaTag();
        metaTag.setKey(key);
        metaTag.setValue(value);
        return metaTag;
    }

}
