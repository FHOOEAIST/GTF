/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import science.aist.gtf.graph.*;
import science.aist.gtf.graph.impl.*;

/**
 * <p>Default Graph Factory</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultGraphFactory implements GraphFactory {
    @Override
    public <V, E> GraphState<V, E> createGraphState() {
        return new GraphStateImpl<>();
    }

    @Override
    public <T, E> Graph<T, E> createGraph(GraphStateAccessor<T, E> graphStateAccessor) {
        return GraphImpl.create(graphStateAccessor);
    }

    @Override
    public <T, E> Vertex<T, E> createVertex(T elem) {
        return new VertexImpl<>(elem);
    }

    @Override
    public <V, E> VertexView<V, E> createVertexView(Vertex<V, E> viewedVertex) {
        return new VertexViewImpl<>(viewedVertex);
    }

    @Override
    public <V, E> EdgeView<V, E> createEdgeView(Edge<V, E> viewedEdge) {
        return new EdgeViewImpl<>(viewedEdge);
    }

    @Override
    public <T, E> Edge<T, E> createEdge(E elem) {
        AbstractEdge<T, E> e = new EdgeImpl<>(elem);
        e.setWeight(1);
        return e;
    }

    @Override
    public <T> MetaTag<T> createMetaTag() {
        return new MetaTagImpl<>();
    }

    @Override
    public <T> MetaTag<T> createMetaTag(String key, T value) {
        return new MetaTagImpl<>(key, value);
    }
}
