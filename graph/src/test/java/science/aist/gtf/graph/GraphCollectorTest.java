/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.builder.impl.GraphBuilderImpl;
import science.aist.gtf.graph.impl.GraphCollector;
import science.aist.gtf.graph.impl.VertexImpl;

import java.util.stream.Stream;

/**
 * <p>Test class for {@link GraphCollector}</p>
 *
 * @author Andreas Pointner
 */
public class GraphCollectorTest {

    @Test
    public void testCollector() {
        // given
        Vertex<Integer, Void> v1 = new VertexImpl<>(1);
        Vertex<Integer, Void> v2 = new VertexImpl<>(2);
        Vertex<Integer, Void> v3 = new VertexImpl<>(3);
        Vertex<Integer, Void> v4 = new VertexImpl<>(-1);

        // when
        Graph<Integer, Void> res = Stream.of(v1, v2, v3, v4)
                // Create an edge to the element that is greater than the element itself.
                // if one of them is below zero, do not create an edge
                .collect(GraphCollector.toGraph((elem1, elem2) -> (elem1.getElement() >= 0 && elem2.getElement() >= 0) && elem1.getElement() < elem2.getElement()));

        // then
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getVertices().size(), 4);
        Assert.assertEquals(v1.getEdges().size(), 2);
        Assert.assertEquals(v1.getOutgoingEdges().size(), 2);
        Assert.assertEquals(v1.getIncomingEdges().size(), 0);
        Assert.assertEquals(v2.getEdges().size(), 2);
        Assert.assertEquals(v2.getIncomingEdges().size(), 1);
        Assert.assertEquals(v2.getOutgoingEdges().size(), 1);
        Assert.assertEquals(v3.getEdges().size(), 2);
        Assert.assertEquals(v3.getIncomingEdges().size(), 2);
        Assert.assertEquals(v3.getOutgoingEdges().size(), 0);
        Assert.assertEquals(v4.getEdges().size(), 0);
    }

    @Test
    public void testEmpty() {
        // given

        // when
        Graph<String, Void> res = Stream.<Vertex<String, Void>>empty().collect(GraphCollector.toGraph((t1, t2) -> true));

        // then
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getVertices().size(), 0);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testCollectSubGraph() {
        // given
        Graph<String, Void> graph = GraphBuilderImpl.<String, Void>create()
                .from("A").to("B")
                .from("B").to("C")
                .from("B").to("AB")
                .from("AB").to("BC")
                .from("BC").to("A")
                .toGraph();

        // when
        Graph<String, Void> collect = graph.stream().filter(x -> x.getElement().length() == 1).collect(GraphCollector.toSubGraph());

        // then
        Assert.assertNotNull(collect);
        Assert.assertEquals(collect.getVertices().size(), 3);
        Assert.assertEquals(collect.getEdges().size(), 2);
        Assert.assertTrue(collect.getVertices().stream().map(Vertex::getElement).anyMatch(x -> x.equals("A")));
        Assert.assertTrue(collect.getVertices().stream().map(Vertex::getElement).anyMatch(x -> x.equals("B")));
        Assert.assertTrue(collect.getVertices().stream().map(Vertex::getElement).anyMatch(x -> x.equals("C")));
        Assert.assertTrue(collect.getEdges().stream().map(Edge::getSource).anyMatch(s -> s.getElement().equals("A")));
        Assert.assertTrue(collect.getEdges().stream().map(Edge::getSource).anyMatch(s -> s.getElement().equals("B")));
        Assert.assertTrue(collect.getEdges().stream().map(Edge::getTarget).anyMatch(s -> s.getElement().equals("B")));
        Assert.assertTrue(collect.getEdges().stream().map(Edge::getTarget).anyMatch(s -> s.getElement().equals("C")));
        Assert.assertEquals(collect.getVertices().stream().filter(x -> x.getElement().equals("A")).findAny().get().getIncomingEdges().size(), 0);
        Assert.assertEquals(collect.getVertices().stream().filter(x -> x.getElement().equals("A")).findAny().get().getOutgoingEdges().size(), 1);
        Assert.assertEquals(collect.getVertices().stream().filter(x -> x.getElement().equals("B")).findAny().get().getIncomingEdges().size(), 1);
        Assert.assertEquals(collect.getVertices().stream().filter(x -> x.getElement().equals("B")).findAny().get().getOutgoingEdges().size(), 1);
        Assert.assertEquals(collect.getVertices().stream().filter(x -> x.getElement().equals("C")).findAny().get().getIncomingEdges().size(), 1);
        Assert.assertEquals(collect.getVertices().stream().filter(x -> x.getElement().equals("C")).findAny().get().getOutgoingEdges().size(), 0);
    }
}
