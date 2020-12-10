/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.GraphStateAccessor;
import science.aist.gtf.graph.Vertex;
import science.aist.gtf.graph.impl.iterator.DepthFirstSearchGraphIterator;
import science.aist.gtf.graph.traversal.TraversalStrategy;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>Implementation of a graph</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Schuler
 * @author Andreas Pointner
 * @since 1.0
 */
@RequiredArgsConstructor
public class GraphImpl<V, E> extends MetaTagCollectionImpl implements Graph<V, E> {

    @Getter
    private final GraphStateAccessor<V, E> graphState;

    /**
     * TraversalStrategy
     */
    @Setter
    private TraversalStrategy<Vertex<V, E>, Edge<V, E>> vertexTraversalStrategy;

    /**
     * Creates a new Graph
     *
     * @param <V>        the type of the decorated vertex value
     * @param <E>        the type of the decorated edge value
     * @param graphState the internal data state representation of the graph
     * @return a new graph
     */
    public static <V, E> Graph<V, E> create(GraphStateAccessor<V, E> graphState) {
        return new GraphImpl<>(graphState);
    }

    @Override
    public Collection<Vertex<V, E>> getVertices() {
        return graphState.getVertices();
    }

    /**
     * traverses through the graph
     *
     * @param visitor the visitor which is executed for every visited vertex
     */
    @Override
    public void traverseVertices(VertexVisitor<V, E> visitor) {
        if (vertexTraversalStrategy != null)
            vertexTraversalStrategy.traverse(visitor);
    }

    /**
     * traverses through the graph
     *
     * @param visitor     the visitor which his executed for every visited vertex
     * @param edgeVisitor the visitor which is executed for every visited edge
     */
    @Override
    public void traverseEdges(VertexVisitor<V, E> visitor, EdgeVisitor<V, E> edgeVisitor) {
        if (vertexTraversalStrategy != null)
            vertexTraversalStrategy.traverse(visitor, edgeVisitor);
    }

    /**
     * Iterates over all Vertices (depth first Search Strategy)
     *
     * @return iterator for vertices
     */
    @Override
    public Iterator<Vertex<V, E>> iterator() {
        return new DepthFirstSearchGraphIterator<>(this);
    }
}
