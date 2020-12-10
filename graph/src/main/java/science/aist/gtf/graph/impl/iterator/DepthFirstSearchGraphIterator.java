/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl.iterator;

import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.Vertex;

import java.util.*;

/**
 * <p>Depth first search iteration strategy for graphs</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public class DepthFirstSearchGraphIterator<V, E> implements Iterator<Vertex<V, E>> {
    /**
     * The already visited vertices
     */
    private final Set<Vertex<V, E>> visited = new HashSet<>();

    /**
     * The current stack in the graph depth
     */
    private final Deque<Vertex<V, E>> stack = new ArrayDeque<>();

    /**
     * @param graph the graph to be iterated
     */
    public DepthFirstSearchGraphIterator(Graph<V, E> graph) {
        stack.addAll(graph.getVertices());
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Vertex<V, E> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Vertex<V, E> v = stack.pop();
        visited.add(v);
        for (Edge<V, E> e : v.getEdges()) {
            Vertex<V, E> adj = e.getTarget();
            stack.remove(adj);
            if (!visited.contains(adj)) {
                stack.push(adj);
            }
        }
        return v;
    }
}
