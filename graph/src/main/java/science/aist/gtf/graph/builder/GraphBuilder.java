/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.builder;

import science.aist.gtf.graph.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * <p>The Graph Builder is the entry point of the builder pattern which was designed to create a graph using a
 * fluent api. It allows adding new vertices as well as defining edges between them.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface GraphBuilder<V, E> {
    /**
     * Method to add a new vertex to the graph builder. This method takes a value, which is used as the decorated value
     * for the created vertex. If there is already another vertex, that confirms to the value then an existing instance
     * of a vertex may be reused.
     *
     * @param value the value of the decorated element
     * @return the new created or cached vertex
     */
    Vertex<V, E> getOrAddVertex(V value);

    /**
     * Same as {@link GraphBuilder#getOrAddVertex(Object)} but it returns the builder itself to provide a fluent way.
     *
     * @param value the value of the decorated element
     * @return the graph builder instance
     * @see GraphBuilder#getOrAddVertex(Object)
     */
    GraphBuilder<V, E> addVertex(V value);

    /**
     * Same as {@link GraphBuilder#addVertex(Object)} but returns a {@link VertexBuilderWith} that allows to use a
     * callback method for the returned vertex, to be able to add additional parameters (e.g. {@link MetaTag}s)
     *
     * @param value the value of the decorated element
     * @return a {@link VertexBuilderWith}
     */
    VertexBuilderWith<V, E> addVertexWith(V value);

    /**
     * Start to create a new Edge to the vertex that decorates value. If there is no vertex that decorated the value
     * yet, a new vertex will be created, as it would be using {@link GraphBuilder#addVertex(Object)} method
     *
     * @param value the value of the decorated element
     * @return a {@link EdgeBuilder}
     */
    EdgeBuilder<V, E> from(V value);

    /**
     * Start to create a new Edge to the vertex that is identified by the given key. Make sure to add the vertex first,
     * otherwise this method may fail.
     *
     * @param key the key which identifies the vertex
     * @return a {@link EdgeBuilder}
     * @throws IllegalStateException if no vertex with given key was found
     */
    EdgeBuilder<V, E> fromByKey(Object key);

    /**
     * Creates the graph based on the vertices and edges defines in that graph builder. This makes the GraphBuilder as
     * "finished" which means, that all further operations on the graph builder will result in an {@link
     * IllegalStateException}.
     *
     * @return a {@link Graph}
     */
    Graph<V, E> toGraph();

    /**
     * Returns a readonly version of the current graph state.
     *
     * @return the current graph state of the builder
     * @see GraphState#readonly()
     */
    GraphStateAccessor<V, E> getGraphState();

    /**
     * Allows to update the key for a given node value in the list. The key must only be updated via this method to
     * maintain a consistent state. The element that is passed to this function must be part of the list and must have
     * the old key. The mapping function will be called with the element and the new key, and must update the elements
     * key value.
     *
     * @param element the element where the key should be updated
     * @param key     the new key
     * @param mapping the mapping function to set the new key
     * @param <T>     the type of the key
     */
    <T> void updateKey(V element, T key, BiConsumer<V, T> mapping);

    /**
     * This method allows adding callback methods, that are applied once the toGraph method is called. One the callback
     * method is added there is no more way to remove it and it will be executed on the graph creation. This method can
     * be used for e.g. adding MetaTags:
     * <pre>{@code
     *     graphBuilder.addGraphCreationCallback(g -> g.addMetaTag(new MetaTagImpl<>("key", "value")));
     * }</pre>
     *
     * @param callback the callback method to be added
     */
    void addGraphCreationCallback(Consumer<Graph<V, E>> callback);

    /**
     * <p>Adds the subgraph to this graph, and returns a new resulting graph, that represents a view on the
     * elements of the all vertices and edges.</p>
     * <p>Note: subGraph is no longer valid afterwards, as the resulting vertices and edges are
     * removed from the graph and added to the new graph.</p>
     *
     * @param subGraph the subgraph that should be added
     * @return the resulting snapshot on the subgraph
     */
    Graph<V, E> addSubGraph(Graph<V, E> subGraph);
}
