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
import science.aist.gtf.graph.GraphStateAccessor;
import science.aist.gtf.graph.Vertex;

import java.util.Collection;
import java.util.Set;

/**
 * <p>Test class for {@link GraphStateImpl}</p>
 *
 * @author Andreas Pointner
 */

public class GraphStateImplTest {

    @Test
    public void testGetVertices() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v = new VertexImpl<>(null);
        gs.addVertex(v);

        // when
        Collection<Vertex<Void, Void>> vertices = gs.getVertices();

        // then
        Assert.assertNotNull(vertices);
        Assert.assertEquals(vertices.size(), 1);
        Assert.assertTrue(vertices.contains(v));
    }

    @Test
    public void testGetEdges() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);
        gs.addEdge(e, v1, v2);

        // when
        Set<Edge<Void, Void>> edges = gs.getEdges(v1);

        // then
        Assert.assertNotNull(edges);
        Assert.assertEquals(edges.size(), 1);
        Assert.assertTrue(edges.contains(e));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetEdgesFail() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v = new VertexImpl<>(null);

        // when
        gs.getEdges(v);

        // then
        // exception that v is not contained
    }

    @Test
    public void testGetSource() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);
        gs.addEdge(e, v1, v2);

        // when
        Vertex<Void, Void> source = gs.getSource(e);

        // then
        Assert.assertEquals(source, v1);
    }

    @Test
    public void testGetTarget() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);
        gs.addEdge(e, v1, v2);

        // when
        Vertex<Void, Void> target = gs.getTarget(e);

        // then
        Assert.assertEquals(target, v2);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetSourceFail() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);

        // when
        gs.getSource(e);

        // then
        // exception that e is not part of the graph state
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetTargetFail() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);

        // when
        gs.getTarget(e);

        // then
        // exception that e is not part of the graph state
    }

    @Test
    public void testAddVertex() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);

        // when
        gs.addVertex(v1);

        // then
        Assert.assertTrue(gs.getVertices().contains(v1));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAddVertexFail() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        gs.addVertex(v1);

        // when
        gs.addVertex(v1);

        // then
        // exception that v1 is already in the graph state
    }

    @Test
    public void testAddEdge() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);

        // when
        gs.addEdge(e, v1, v2);

        // then
        Assert.assertTrue(gs.getEdges(v1).contains(e));
    }

    @Test
    public void testAddEdgeFailMissingSource() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);

        // when
        gs.addEdge(e, v1, v2);

        // then
        Assert.assertTrue(gs.getEdges(v1).contains(e));
    }

    @Test
    public void testAddEdgeFailMissingTarget() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);

        // when
        gs.addEdge(e, v1, v2);

        // then
        Assert.assertTrue(gs.getEdges(v1).contains(e));
    }

    @Test
    public void testAddEdgeFailAlreadyExists() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);

        // when
        gs.addEdge(e, v1, v2);

        // then
        Assert.assertTrue(gs.getEdges(v1).contains(e));
    }

    @Test
    public void testReadonly() {
        // given
        GraphStateImpl<Void, Void> gs = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        gs.addVertex(v1);
        gs.addVertex(v2);
        gs.addEdge(e, v1, v2);

        // when
        GraphStateAccessor<Void, Void> readonly = gs.readonly();

        // then
        Assert.assertFalse(readonly instanceof GraphState);
        Assert.assertEquals(readonly.getVertices().size(), 2);
        Assert.assertEquals(readonly.getEdges(v1).size(), 1);
        Assert.assertEquals(readonly.getEdges(v2).size(), 1);
        Assert.assertEquals(readonly.getSource(e), v1);
        Assert.assertEquals(readonly.getTarget(e), v2);
    }
}