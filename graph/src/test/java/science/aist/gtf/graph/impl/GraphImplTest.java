/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.GraphState;
import science.aist.gtf.graph.Vertex;
import science.aist.gtf.graph.traversal.TraversalStrategy;
import science.aist.jack.general.util.CastUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <p>Test class for {@link GraphImpl}</p>
 *
 * @author Andreas Pointner
 */

public class GraphImplTest {

    @Test
    public void testCreate() {
        // given
        GraphState<Void, Void> x = new GraphStateImpl<>();

        // when
        Graph<Void, Void> graph = GraphImpl.create(x);

        // then
        Assert.assertNotNull(graph);
        Assert.assertEquals(graph.getGraphState(), x);
    }

    @Test
    public void testGetVertices() {
        // given
        GraphState<Void, Void> x = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v3 = new VertexImpl<>(null);
        Stream.of(v1, v2, v3).forEach(x::addVertex);
        Graph<Void, Void> graph = GraphImpl.create(x);

        // when
        Collection<Vertex<Void, Void>> vertices = graph.getVertices();

        // then
        Assert.assertNotNull(vertices);
        Assert.assertEquals(vertices.size(), 3);
        Assert.assertTrue(vertices.contains(v1));
        Assert.assertTrue(vertices.contains(v2));
        Assert.assertTrue(vertices.contains(v3));
    }

    @Test
    public void testTraverseVertices() {
        // given
        Graph<Void, Void> graph = GraphImpl.create(null);
        TraversalStrategy<Vertex<Void, Void>, Edge<Void, Void>> traversalStrategy = CastUtils.cast(Mockito.mock(TraversalStrategy.class));
        VertexVisitor<Void, Void> vv = CastUtils.cast(Mockito.mock(VertexVisitor.class));
        graph.setVertexTraversalStrategy(traversalStrategy);

        // when
        graph.traverseVertices(vv);

        // then
        Mockito.verify(traversalStrategy, Mockito.times(1)).traverse(vv);
    }

    @Test
    public void testTraverseEdges() {
        // given
        Graph<Void, Void> graph = GraphImpl.create(null);
        TraversalStrategy<Vertex<Void, Void>, Edge<Void, Void>> traversalStrategy = CastUtils.cast(Mockito.mock(TraversalStrategy.class));
        EdgeVisitor<Void, Void> ev = CastUtils.cast(Mockito.mock(EdgeVisitor.class));
        graph.setVertexTraversalStrategy(traversalStrategy);

        // when
        graph.traverseEdges(null, ev);

        // then
        Mockito.verify(traversalStrategy, Mockito.times(1)).traverse(null, ev);
    }

    @Test
    public void testIterator() {
        // given
        GraphState<Void, Void> x = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v3 = new VertexImpl<>(null);
        Stream.of(v1, v2, v3).forEach(x::addVertex);
        Graph<Void, Void> graph = GraphImpl.create(x);

        // when & then
        for (Vertex<Void, Void> vertex : graph) {
            Assert.assertTrue(vertex == v1 || vertex == v2 || vertex == v3);
        }
    }

    @Test
    public void testGetEdges() {
        // given
        GraphState<Void, Void> x = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v3 = new VertexImpl<>(null);
        Stream.of(v1, v2, v3).forEach(x::addVertex);
        EdgeImpl<Void, Void> e1 = new EdgeImpl<>(null);
        EdgeImpl<Void, Void> e2 = new EdgeImpl<>(null);
        x.addEdge(e1, v1, v2);
        x.addEdge(e2, v2, v3);
        Graph<Void, Void> graph = GraphImpl.create(x);

        // when
        Set<Edge<Void, Void>> edges = graph.getEdges();

        // then
        Assert.assertNotNull(edges);
        Assert.assertEquals(edges.size(), 2);
        Assert.assertTrue(edges.contains(e1));
        Assert.assertTrue(edges.contains(e2));
    }
}