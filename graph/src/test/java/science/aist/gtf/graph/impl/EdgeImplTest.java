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
import science.aist.gtf.graph.GraphState;
import science.aist.gtf.graph.Vertex;

/**
 * <p>Test class for {@link EdgeImpl}</p>
 *
 * @author Andreas Pointner
 */

public class EdgeImplTest {

    @Test
    public void testGetWeight() {
        // given
        EdgeImpl<Void, Void> edge = new EdgeImpl<>(null);
        edge.setWeight(5.0);

        // when
        double weight = edge.getWeight();

        // then
        Assert.assertEquals(weight, 5.0);
    }

    @Test
    public void testGetSource() {
        // given
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);

        GraphState<Void, Void> gs = new GraphStateImpl<>();
        gs.addVertex(v1);
        gs.addVertex(v2);
        gs.addEdge(e, v1, v2);

        // when
        Vertex<Void, Void> source = e.getSource();

        // then
        Assert.assertEquals(source, v1);
    }

    @Test
    public void testGetTarget() {
        // given
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);

        GraphState<Void, Void> gs = new GraphStateImpl<>();
        gs.addVertex(v1);
        gs.addVertex(v2);
        gs.addEdge(e, v1, v2);

        // when
        Vertex<Void, Void> target = e.getTarget();

        // then
        Assert.assertEquals(target, v2);
    }

    @Test
    public void testGetElement() {
        // given
        EdgeImpl<Void, String> e = new EdgeImpl<>("test");

        // when
        String element = e.getElement();

        // then
        Assert.assertEquals(element, "test");
    }
}