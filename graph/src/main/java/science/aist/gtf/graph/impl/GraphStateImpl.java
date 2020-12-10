/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.GraphState;
import science.aist.gtf.graph.Vertex;
import science.aist.jack.data.Pair;

import java.util.*;

/**
 * <p>Represents a current state of a graph</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public class GraphStateImpl<V, E> implements GraphState<V, E> {
    private final Map<Vertex<V, E>, Set<Edge<V, E>>> edges = new HashMap<>();
    private final Map<Edge<V, E>, Pair<Vertex<V, E>, Vertex<V, E>>> edgePairMap = new HashMap<>();


    @Override
    public Collection<Vertex<V, E>> getVertices() {
        return Collections.unmodifiableSet(edges.keySet());
    }

    @Override
    public Set<Edge<V, E>> getEdges(Vertex<V, E> vertex) {
        if (!edges.containsKey(vertex))
            throw new IllegalStateException("Access unknown vertex");
        return Collections.unmodifiableSet(edges.get(vertex));
    }

    @Override
    public Vertex<V, E> getSource(Edge<V, E> edge) {
        if (!edgePairMap.containsKey(edge))
            throw new IllegalStateException("Access unknown edge");
        return edgePairMap.get(edge).getFirst();
    }

    @Override
    public Vertex<V, E> getTarget(Edge<V, E> edge) {
        if (!edgePairMap.containsKey(edge))
            throw new IllegalStateException("Access unknown edge");
        return edgePairMap.get(edge).getSecond();
    }

    @Override
    public void addVertex(Vertex<V, E> vertex) {
        if (!(vertex instanceof AbstractVertex))
            throw new IllegalStateException("Graph state implementation only supports subclasses of AbstractVertex");
        if (edges.containsKey(vertex)) {
            throw new IllegalStateException("Vertex already contained in graph state");
        }
        edges.put(vertex, new HashSet<>());
        ((AbstractVertex<V, E>) vertex).setGraphState(this);
    }

    @Override
    public void addEdge(Edge<V, E> edge, Vertex<V, E> source, Vertex<V, E> target) {
        if (!(edge instanceof AbstractEdge))
            throw new IllegalStateException("Graph state implementation only supports subclasses of AbstractVertex");
        if (edgePairMap.containsKey(edge)) {
            throw new IllegalStateException("Edge is already contained in graph state");
        }
        if (!edges.containsKey(source) || !edges.containsKey(target)) {
            throw new IllegalStateException("Source of Target vertex is not yet contained in graph state");
        }
        edges.get(source).add(edge);
        edges.get(target).add(edge);
        edgePairMap.put(edge, Pair.of(source, target));
        ((AbstractEdge<V, E>) edge).setGraphState(this);
    }
}
