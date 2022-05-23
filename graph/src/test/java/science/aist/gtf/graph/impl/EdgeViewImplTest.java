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

/**
 * <p>Test class for {@link EdgeViewImpl}</p>
 *
 * @author Andreas Pointner
 */

@SuppressWarnings("unchecked")
public class EdgeViewImplTest {

    @Test
    public void testGetSource() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> ev = new EdgeViewImpl<>(edge);
        GraphState<Object, Void> graphState = CastUtils.cast(Mockito.mock(GraphState.class));
        ev.setGraphState(graphState);
        Mockito.reset(edge);

        // when
        ev.getSource();

        // then
        Mockito.verify(graphState, Mockito.times(1)).getSource(ev);
        Mockito.verifyNoMoreInteractions(graphState);
        Mockito.verifyNoInteractions(edge);
    }

    @Test
    public void testGetTarget() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> ev = new EdgeViewImpl<>(edge);
        GraphState<Object, Void> graphState = CastUtils.cast(Mockito.mock(GraphState.class));
        ev.setGraphState(graphState);
        Mockito.reset(edge);

        // when
        ev.getTarget();

        // then
        Mockito.verify(graphState, Mockito.times(1)).getTarget(ev);
        Mockito.verifyNoMoreInteractions(graphState);
        Mockito.verifyNoInteractions(edge);
    }

    @Test
    public void testGetElement() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> ev = new EdgeViewImpl<>(edge);
        Mockito.reset(edge);

        // when
        ev.getElement();

        // then
        Mockito.verify(edge, Mockito.times(1)).getElement();
        Mockito.verifyNoMoreInteractions(edge);
    }

    @Test
    public void testGetMetaTags() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> vv = new EdgeViewImpl<>(edge);
        Mockito.reset(edge);

        // when
        vv.getMetaTags();

        // then
        Mockito.verify(edge, Mockito.times(1)).getMetaTags();
        Mockito.verifyNoMoreInteractions(edge);
    }

    @Test
    public void testAddMetaTag() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> vv = new EdgeViewImpl<>(edge);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        Mockito.reset(edge);

        // when
        vv.addMetaTag(mt);

        // then
        Mockito.verify(edge, Mockito.times(1)).addMetaTag(mt);
        Mockito.verifyNoMoreInteractions(edge);
    }

    @Test
    public void testRemoveMetaTag() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> vv = new EdgeViewImpl<>(edge);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(edge);

        // when
        vv.removeMetaTag(mt);

        // then
        Mockito.verify(edge, Mockito.times(1)).removeMetaTag(mt);
        Mockito.verifyNoMoreInteractions(edge);
    }

    @Test
    public void testGetMetaTagValue() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> vv = new EdgeViewImpl<>(edge);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(edge);

        // when
        vv.getMetaTagValue("key");

        // then
        Mockito.verify(edge, Mockito.times(1)).getMetaTagValue("key");
        Mockito.verifyNoMoreInteractions(edge);
    }

    @Test
    public void testTryGetMetaTagValue() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> vv = new EdgeViewImpl<>(edge);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(edge);

        // when
        vv.tryGetMetaTagValue("key");

        // then
        Mockito.verify(edge, Mockito.times(1)).tryGetMetaTagValue("key");
        Mockito.verifyNoMoreInteractions(edge);
    }

    @Test
    public void testTestGetMetaTagValue() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> vv = new EdgeViewImpl<>(edge);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(edge);

        // when
        vv.getMetaTagValue("key", String.class);

        // then
        Mockito.verify(edge, Mockito.times(1)).getMetaTagValue("key", String.class);
        Mockito.verifyNoMoreInteractions(edge);
    }

    @Test
    public void testTestTryGetMetaTagValue() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> vv = new EdgeViewImpl<>(edge);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(edge);

        // when
        vv.tryGetMetaTagValue("key", String.class);

        // then
        Mockito.verify(edge, Mockito.times(1)).tryGetMetaTagValue("key", String.class);
        Mockito.verifyNoMoreInteractions(edge);
    }

    @Test
    public void testTestRemoveMetaTag() {
        // given
        AbstractEdge<Object, Void> edge = CastUtils.cast(Mockito.mock(AbstractEdge.class));
        EdgeViewImpl<Object, Void> vv = new EdgeViewImpl<>(edge);
        MetaTag<String> mt = new MetaTagImpl<>("key", "Hello World");
        vv.addMetaTag(mt);
        Mockito.reset(edge);

        // when
        vv.removeMetaTag("key", String.class);

        // then
        Mockito.verify(edge, Mockito.times(1)).removeMetaTag("key", String.class);
        Mockito.verifyNoMoreInteractions(edge);
    }

}