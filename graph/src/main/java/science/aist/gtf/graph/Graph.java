/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph;

import science.aist.gtf.graph.impl.EdgeVisitor;
import science.aist.gtf.graph.impl.VertexVisitor;
import science.aist.gtf.graph.traversal.TraversalStrategy;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>Interface for a graph representation</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface Graph<V, E> extends MetaTagCollection, Iterable<Vertex<V, E>> {

    /**
     * gets value of field vertices
     *
     * @return value of field vertices
     */
    Collection<Vertex<V, E>> getVertices();

    /**
     * <p>returns all edges that are inside the graph.</p>
     * <p>Note: this operation traverses every node, and could therefore be extremely slow for big graphs</p>
     *
     * @return all edges that are inside the graph
     */
    default Set<Edge<V, E>> getEdges() {
        return getVertices().stream()
                .map(Vertex::getEdges)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Sets the traversal strategy
     *
     * @param traversalStrategy the traversal strategy
     */
    void setVertexTraversalStrategy(TraversalStrategy<Vertex<V, E>, Edge<V, E>> traversalStrategy);

    /**
     * traverses through the graph
     *
     * @param visitor the visitor which is executed for every visited vertex
     */
    void traverseVertices(VertexVisitor<V, E> visitor);

    /**
     * traverses through the graph
     *
     * @param visitor     the visitor which his executed for every visited vertex
     * @param edgeVisitor the visitor which is executed for every visited edge
     */
    void traverseEdges(VertexVisitor<V, E> visitor, EdgeVisitor<V, E> edgeVisitor);

    /**
     * Creates a stream and streams the vertices
     *
     * @return stream of vertices
     */
    default Stream<Vertex<V, E>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * The current state the graph is in
     *
     * @return the graph state object in the background which is used inside the graph
     */
    GraphStateAccessor<V, E> getGraphState();

}
