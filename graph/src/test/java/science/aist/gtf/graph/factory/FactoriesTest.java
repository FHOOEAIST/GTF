/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.factory;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.*;

/**
 * <p>Tests {@link GraphFactory} {@link DefaultGraphFactory} {@link GraphFactoryFactory}</p>
 *
 * @author Andreas Pointner
 */
public class FactoriesTest {
    @Test
    public void testCreateDefaultFactory() {
        // given

        // when
        GraphFactory gf = GraphFactoryFactory.getDefaultFactory();

        // then
        Assert.assertNotNull(gf);
    }

    @Test
    public void testCreateGraphWithDefaultFactory() {
        // given
        GraphFactory gf = GraphFactoryFactory.getDefaultFactory();

        // when
        Graph<String, Void> graph = gf.createGraph(null);

        // then
        Assert.assertNotNull(graph);
    }

    @Test
    public void testCreateEdgeWithDefaultFactory() {
        // given
        GraphFactory gf = GraphFactoryFactory.getDefaultFactory();

        // when
        Edge<String, Void> edge = gf.createEdge(null);

        // then
        Assert.assertNotNull(edge);
    }

    @Test
    public void testCreateVertexWithDefaultFactory() {
        // given
        GraphFactory gf = GraphFactoryFactory.getDefaultFactory();

        // when
        Vertex<String, Void> vertex = gf.createVertex("");

        // then
        Assert.assertNotNull(vertex);
    }

    @Test
    public void testCreateMetaTagWithDefaultFactory() {
        // given
        GraphFactory gf = GraphFactoryFactory.getDefaultFactory();

        // when
        MetaTag<String> metaTag = gf.createMetaTag();

        // then
        Assert.assertNotNull(metaTag);
    }

    @Test
    public void testCreateMetaTagWithKeyAndValueWithDefaultFactory() {
        // given
        GraphFactory gf = GraphFactoryFactory.getDefaultFactory();

        // when
        MetaTag<String> metaTag = gf.createMetaTag("key", "val");

        // then
        Assert.assertNotNull(metaTag);
        Assert.assertEquals(metaTag.getKey(), "key");
        Assert.assertEquals(metaTag.getValue(), "val");
    }

    @Test
    public void testCreateVertexViewWithDefaultFactory() {
        // given
        GraphFactory gf = GraphFactoryFactory.getDefaultFactory();
        Vertex<Object, Void> v = gf.createVertex("x");

        // when
        VertexView<Object, Void> vertexView = gf.createVertexView(v);

        // then
        Assert.assertNotNull(vertexView);
        Assert.assertEquals(vertexView.getViewedVertex(), v);
        Assert.assertEquals(vertexView.getElement(), "x");
    }

    @Test
    public void testCreateEdgeViewWithDefaultFactory() {
        // given
        GraphFactory gf = GraphFactoryFactory.getDefaultFactory();
        Edge<Void, Object> v = gf.createEdge("x");

        // when
        EdgeView<Void, Object> vertexView = gf.createEdgeView(v);

        // then
        Assert.assertNotNull(vertexView);
        Assert.assertEquals(vertexView.getViewedEdge(), v);
        Assert.assertEquals(vertexView.getElement(), "x");
    }
}
