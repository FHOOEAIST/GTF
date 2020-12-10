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
import org.testng.annotations.Test;
import science.aist.gtf.graph.GraphState;
import science.aist.gtf.graph.MetaTag;
import science.aist.jack.general.util.CastUtils;

import static org.mockito.Matchers.any;

/**
 * <p>Test class for {@link VertexViewImpl}</p>
 *
 * @author Andreas Pointner
 */

@SuppressWarnings("unchecked")
public class VertexViewImplTest {

    @Test
    public void testGetMetaTags() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);

        // when
        vv.getMetaTags();

        // then
        Mockito.verify(vertex, Mockito.times(1)).getMetaTags();
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testAddMetaTag() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");

        // when
        vv.addMetaTag(mt);

        // then
        Mockito.verify(vertex, Mockito.times(1)).addMetaTag(mt);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testRemoveMetaTag() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(vertex);

        // when
        vv.removeMetaTag(mt);

        // then
        Mockito.verify(vertex, Mockito.times(1)).removeMetaTag(mt);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testGetMetaTagValue() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(vertex);

        // when
        vv.getMetaTagValue("key");

        // then
        Mockito.verify(vertex, Mockito.times(1)).getMetaTagValue("key");
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testTryGetMetaTagValue() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(vertex);

        // when
        vv.tryGetMetaTagValue("key");

        // then
        Mockito.verify(vertex, Mockito.times(1)).tryGetMetaTagValue("key");
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testTestGetMetaTagValue() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(vertex);

        // when
        vv.getMetaTagValue("key", String.class);

        // then
        Mockito.verify(vertex, Mockito.times(1)).getMetaTagValue("key", String.class);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testTestTryGetMetaTagValue() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(vertex);

        // when
        vv.tryGetMetaTagValue("key", String.class);

        // then
        Mockito.verify(vertex, Mockito.times(1)).tryGetMetaTagValue("key", String.class);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testTestRemoveMetaTag() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(vertex);

        // when
        vv.removeMetaTag("key", String.class);

        // then
        Mockito.verify(vertex, Mockito.times(1)).removeMetaTag("key", String.class);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testGetAdjacentVertices() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        GraphState<Object, Void> graphState = CastUtils.cast(Mockito.mock(GraphState.class));
        vv.setGraphState(graphState);

        // when
        vv.getAdjacentVertices();

        // then
        Mockito.verify(graphState, Mockito.times(2)).getEdges(any());
        Mockito.verifyNoMoreInteractions(graphState);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testGetEdges() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        GraphState<Object, Void> graphState = CastUtils.cast(Mockito.mock(GraphState.class));
        vv.setGraphState(graphState);

        // when
        vv.getEdges();

        // then
        Mockito.verify(graphState, Mockito.times(1)).getEdges(any());
        Mockito.verifyNoMoreInteractions(graphState);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testGetIncomingEdges() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        GraphState<Object, Void> graphState = CastUtils.cast(Mockito.mock(GraphState.class));
        vv.setGraphState(graphState);

        // when
        vv.getIncomingEdges();

        // then
        Mockito.verify(graphState, Mockito.times(1)).getEdges(any());
        Mockito.verifyNoMoreInteractions(graphState);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testGetOutgoingEdges() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);
        GraphState<Object, Void> graphState = CastUtils.cast(Mockito.mock(GraphState.class));
        vv.setGraphState(graphState);

        // when
        vv.getOutgoingEdges();

        // then
        Mockito.verify(graphState, Mockito.times(1)).getEdges(any());
        Mockito.verifyNoMoreInteractions(graphState);
        Mockito.verifyNoMoreInteractions(vertex);
    }

    @Test
    public void testGetElement() {
        // given
        AbstractVertex<Object, Void> vertex = CastUtils.cast(Mockito.mock(AbstractVertex.class));
        VertexViewImpl<Object, Void> vv = new VertexViewImpl<>(vertex);

        // when
        vv.getElement();

        // then
        Mockito.verify(vertex, Mockito.times(1)).getElement();
        Mockito.verifyNoMoreInteractions(vertex);
    }
}