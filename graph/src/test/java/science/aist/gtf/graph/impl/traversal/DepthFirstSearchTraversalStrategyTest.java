/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl.traversal;

import org.mockito.Mockito;
import org.testng.annotations.Test;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.GraphState;
import science.aist.gtf.graph.impl.*;
import science.aist.jack.general.util.CastUtils;

import java.util.stream.Stream;

/**
 * <p>Test class for {@link DepthFirstSearchTraversalStrategy}</p>
 *
 * @author Andreas Pointner
 */

public class DepthFirstSearchTraversalStrategyTest {

    @Test
    public void testTraverseVertex() {
        // given
        GraphState<Void, Void> x = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        Stream.of(v1, v2).forEach(x::addVertex);
        EdgeImpl<Void, Void> e1 = new EdgeImpl<>(null);
        x.addEdge(e1, v1, v2);
        Graph<Void, Void> graph = GraphImpl.create(x);
        DepthFirstSearchTraversalStrategy<Void, Void> depthFirstSearchTraversalStrategy = new DepthFirstSearchTraversalStrategy<>(graph);
        VertexVisitor<Void, Void> vv = CastUtils.cast(Mockito.mock(VertexVisitor.class));

        // when
        depthFirstSearchTraversalStrategy.traverse(vv);

        // then
        Mockito.verify(vv, Mockito.times(1)).visit(v1);
        Mockito.verify(vv, Mockito.times(1)).visit(v2);
    }

    @Test
    public void testTraverseEdge() {
        // given
        GraphState<Void, Void> x = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        Stream.of(v1, v2).forEach(x::addVertex);
        EdgeImpl<Void, Void> e1 = new EdgeImpl<>(null);
        x.addEdge(e1, v1, v2);
        Graph<Void, Void> graph = GraphImpl.create(x);
        DepthFirstSearchTraversalStrategy<Void, Void> depthFirstSearchTraversalStrategy = new DepthFirstSearchTraversalStrategy<>(graph);
        VertexVisitor<Void, Void> vv = CastUtils.cast(Mockito.mock(VertexVisitor.class));
        EdgeVisitor<Void, Void> ev = CastUtils.cast(Mockito.mock(EdgeVisitor.class));

        // when
        depthFirstSearchTraversalStrategy.traverse(vv, ev);

        // then
        Mockito.verify(vv, Mockito.times(1)).visit(v1);
        Mockito.verify(vv, Mockito.times(1)).visit(v2);
        Mockito.verify(ev, Mockito.times(1)).visit(e1);
    }
}