/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.GraphState;
import science.aist.gtf.graph.Vertex;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * <p>Test class for {@link VertexImpl}</p>
 *
 * @author Andreas Pointner
 */
public class VertexImplTest {

    @Test
    public void testGetAdjacentVertices() {
        // given
        GraphState<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v3 = new VertexImpl<>(null);
        Stream.of(v1, v2, v3).forEach(gs::addVertex);
        gs.addEdge(new EdgeImpl<>(null), v1, v2);
        gs.addEdge(new EdgeImpl<>(null), v1, v3);

        // when
        Collection<Vertex<Void, Void>> adjacentVertices = v1.getAdjacentVertices();

        // then
        Assert.assertNotNull(adjacentVertices);
        Assert.assertEquals(adjacentVertices.size(), 2);
        Assert.assertTrue(adjacentVertices.contains(v2));
        Assert.assertTrue(adjacentVertices.contains(v3));
    }

    @Test
    public void testGetEdges() {
        // given
        GraphState<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v3 = new VertexImpl<>(null);
        Stream.of(v1, v2, v3).forEach(gs::addVertex);
        gs.addEdge(new EdgeImpl<>(null), v1, v2);
        gs.addEdge(new EdgeImpl<>(null), v1, v3);

        // when
        Collection<Edge<Void, Void>> edges = v1.getEdges();

        // then
        Assert.assertNotNull(edges);
        Assert.assertEquals(edges.size(), 2);
        Assert.assertTrue(edges.stream().allMatch(e -> e.getSource() == v1));
        Assert.assertTrue(edges.stream().anyMatch(e -> e.getTarget() == v2));
        Assert.assertTrue(edges.stream().anyMatch(e -> e.getTarget() == v3));
    }

    @Test
    public void testGetIncomingEdges() {
        // given
        GraphState<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v3 = new VertexImpl<>(null);
        Stream.of(v1, v2, v3).forEach(gs::addVertex);
        gs.addEdge(new EdgeImpl<>(null), v1, v2);
        gs.addEdge(new EdgeImpl<>(null), v3, v1);

        // when
        Collection<Edge<Void, Void>> edges = v1.getIncomingEdges();

        // then
        Assert.assertNotNull(edges);
        Assert.assertEquals(edges.size(), 1);
        Assert.assertTrue(edges.stream().anyMatch(e -> e.getSource() == v3));
        Assert.assertTrue(edges.stream().anyMatch(e -> e.getTarget() == v1));
    }

    @Test
    public void testGetOutgoingEdges() {
        // given
        GraphState<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v3 = new VertexImpl<>(null);
        Stream.of(v1, v2, v3).forEach(gs::addVertex);
        gs.addEdge(new EdgeImpl<>(null), v1, v2);
        gs.addEdge(new EdgeImpl<>(null), v3, v1);

        // when
        Collection<Edge<Void, Void>> edges = v1.getOutgoingEdges();

        // then
        Assert.assertNotNull(edges);
        Assert.assertEquals(edges.size(), 1);
        Assert.assertTrue(edges.stream().anyMatch(e -> e.getSource() == v1));
        Assert.assertTrue(edges.stream().anyMatch(e -> e.getTarget() == v2));
    }

    @Test
    public void testGetElement() {
        // given
        VertexImpl<String, Void> vertex = new VertexImpl<>("abc");

        // when
        String element = vertex.getElement();

        // then
        Assert.assertEquals(element, "abc");
    }
}