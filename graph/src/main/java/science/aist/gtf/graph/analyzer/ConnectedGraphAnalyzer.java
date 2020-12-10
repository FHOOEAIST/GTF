/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.analyzer;

import science.aist.gtf.graph.GraphAnalyzer;
import science.aist.gtf.graph.GraphStateAccessor;
import science.aist.gtf.graph.Vertex;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <p>Calculate if the graph state represents a connected graph, meaning every vertex can be reached.
 * via any other vertex over any hops of edges. The edges are seen as undirected.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public class ConnectedGraphAnalyzer<V, E> implements GraphAnalyzer<V, E, Boolean> {
    @Override
    public Boolean analyzeGraphState(GraphStateAccessor<V, E> graphStateAccessor) {
        Collection<Vertex<V, E>> vertices = graphStateAccessor.getVertices();
        Optional<Vertex<V, E>> any = vertices.stream().findAny();
        if (any.isPresent()) {
            Set<Vertex<V, E>> visited = new HashSet<>();
            visit(visited, any.get());
            return visited.containsAll(vertices);
        }
        return false;
    }

    private void visit(Set<Vertex<V, E>> visited, Vertex<V, E> toVisit) {
        visited.add(toVisit);
        toVisit.getEdges().stream().flatMap(e -> Stream.of(e.getSource(), e.getTarget()))
                .filter(v -> v != toVisit)
                .filter(v -> !visited.contains(v))
                .forEach(v -> visit(visited, v));
    }
}
