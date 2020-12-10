/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl.traversal;

import lombok.RequiredArgsConstructor;
import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.Vertex;
import science.aist.gtf.graph.Visitor;
import science.aist.gtf.graph.traversal.TraversalStrategy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Depth-First Search traversal strategy for a given graph PseudoCode:</p>
 * <pre>{@code
 *  1 procedure DFS(G, v):
 *  2     label v as explored
 *  3     for all edges e in G.incidentEdges(v) do
 *  4         if edge e is unexplored then
 *  5             w ← G.adjacentVertex(v, e)
 *  6             if vertex w is unexplored then
 *  7                 label e as a discovered edge
 *  8                 recursively call DFS(G, w)
 *  9             else
 *  10               label e as a back edge
 *  }</pre>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Schuler
 * @since 1.0
 */
@RequiredArgsConstructor
public class DepthFirstSearchTraversalStrategy<V, E> implements TraversalStrategy<Vertex<V, E>, Edge<V, E>> {

    private final Graph<V, E> graph;
    private Set<Vertex<V, E>> visitedSet;
    private Set<Edge<V, E>> visitedEdgeSet;

    @Override
    public void traverse(final Visitor<Vertex<V, E>> visitor, final Visitor<Edge<V, E>> edgeVisitor) {
        visitedSet = new HashSet<>();
        visitedEdgeSet = new HashSet<>();
        for (Vertex<V, E> vertex : graph.getVertices()) {
            dfs(vertex, visitor, edgeVisitor);
            if (isUnexplored(vertex))
                visitor.visit(vertex);
        }
    }

    @Override
    public void traverse(final Visitor<Vertex<V, E>> visitor) {
        this.traverse(visitor, null);
    }

    private boolean isUnexplored(Vertex<V, E> vertex) {
        return visitedSet.add(vertex);
    }

    private boolean isUnexploredEdge(Edge<V, E> edge) {
        return visitedEdgeSet.add(edge);
    }

    private void dfs(final Vertex<V, E> vertex, final Visitor<Vertex<V, E>> visitor, final Visitor<Edge<V, E>> edgeVisitor) {
        Collection<Edge<V, E>> edges = vertex.getEdges();
        for (Edge<V, E> edge : edges) {
            Vertex<V, E> adjacent = edge.getTarget();
            if (isUnexplored(adjacent)) {
                dfs(adjacent, visitor, edgeVisitor);
                visitor.visit(adjacent);
            }
            if (edgeVisitor != null && isUnexploredEdge(edge))
                edgeVisitor.visit(edge);
        }
    }
}
