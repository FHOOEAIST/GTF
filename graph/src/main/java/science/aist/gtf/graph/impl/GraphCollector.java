/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.GraphState;
import science.aist.gtf.graph.Vertex;
import science.aist.gtf.graph.VertexView;
import science.aist.gtf.graph.factory.GraphFactory;
import science.aist.gtf.graph.factory.GraphFactoryFactory;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>Final Class, which provides a static function, that provides a collector to create a graph</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public final class GraphCollector {
    /**
     * This class must not be created
     */
    private GraphCollector() {
    }

    /**
     * <p>Creates a collector, that allows to collect a stream of vertices and convert it into a graph. The given
     * predicate decides, if an edge is created. the edge is created directional.</p>
     * <br>
     * Usage example:
     * <pre>{@code
     *   Stream<Vertex<V>> someStream = ...;
     *   Graph<V> myGraph = someStream.collect(GraphCollector.toGraph((v1, v2) -> {
     *      // Some check if a edge from v1 to v2 should be created
     *      return true / false;
     *   });
     * }</pre>
     * <br>
     * Another use case is to transform a graph into another graph
     * <pre>{@code
     *   Graph<V>; myFirstGraph = ...;
     *   Graph<S>; myResult Graph = myFirstGraph
     *     .stream()
     *     .map(...) // Some mapping
     *     .xyz(...) // Some other streaming operations.
     *     .collect(GraphCollector.toGraph(myBiPredicate));
     * }</pre>
     *
     * @param predicate test if an edge from first element to the second element should be created
     * @param <V>       the type of the decorated vertex value
     * @param <E>       the type of the decorated edge value
     * @return a collector, that can be used in streams.
     */
    public static <V, E> Collector<Vertex<V, E>, ?, Graph<V, E>> toGraph(BiPredicate<Vertex<V, E>, Vertex<V, E>> predicate) {
        Objects.requireNonNull(predicate);
        return Collectors.collectingAndThen(Collectors.toSet(), (Set<Vertex<V, E>> set) -> {
            GraphState<V, E> graphState = new GraphStateImpl<>();
            set.forEach(graphState::addVertex);

            set.forEach(from -> set.stream()
                    .filter(to -> predicate.test(from, to))
                    .forEach(to -> graphState.addEdge(new EdgeImpl<>(null), from, to))
            );

            return GraphImpl.create(graphState);
        });
    }

    /**
     * <p>Creates a subgraph for a given set of vertices, where the edge between the vertices in the stream are
     * used.</p>
     * <h2>Example:</h2>
     * <span>Given Graph:</span>
     * <pre>
     *           (C)
     *            ↑
     *     (A) → (B)
     *      ↑     ↓
     *    (BC) ← (AB)
     * </pre>
     * <span>Selected Vertices: [A, B, C]</span>
     * <span>Resulting Graph:</span>
     * <pre>
     *          (C)
     *           ↑
     *    (A) → (B)
     * </pre>
     *
     * <p>So all edges, that leads to a vertex, that is again in the list of vertices, will be added, all other edges
     * are removed.</p>
     *
     * <p>Usage example (for the above example):</p>
     * <pre>{@code
     *    Graph<String, Void> graph = GraphBuilderImpl.<String, Void>create()
     *                   .from("A").to("B")
     *                   .from("B").to("C")
     *                   .from("B").to("AB")
     *                   .from("AB").to("BC")
     *                   .from("BC").to("A")
     *                .toGraph();
     *
     *    Graph<String, Void> collect = graph.stream()
     *                      .filter(x -> x.getElement().length() == 1)
     *                      .collect(GraphCollector.toSubGraph());
     * }</pre>
     *
     * @param <V> the type of the decorated vertex value
     * @param <E> the type of the decorated edge value
     * @return A collector which allows collecting for a vertex stream
     */
    public static <V, E> Collector<Vertex<V, E>, ?, Graph<V, E>> toSubGraph() {
        final GraphFactory defaultFactory = GraphFactoryFactory.getDefaultFactory();
        return Collectors.collectingAndThen(Collectors.toSet(), (Set<Vertex<V, E>> set) -> {
            GraphState<V, E> subGraphState = new GraphStateImpl<>();
            Map<Vertex<V, E>, VertexView<V, E>> cache = new HashMap<>();
            for (Vertex<V, E> veVertex : set) {
                VertexView<V, E> veVertexView = defaultFactory.createVertexView(veVertex);
                cache.put(veVertex, veVertexView);
                subGraphState.addVertex(veVertexView);
            }

            set.stream()
                    .map(Vertex::getEdges)
                    .flatMap(Collection::stream)
                    .distinct()
                    .filter(e -> set.contains(e.getSource()))
                    .filter(e -> set.contains(e.getTarget()))
                    .forEach(e -> subGraphState.addEdge(defaultFactory.createEdgeView(e), cache.get(e.getSource()), cache.get(e.getTarget())));

            return new GraphImpl<>(subGraphState.readonly());
        });
    }
}
