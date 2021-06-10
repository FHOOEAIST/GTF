/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import science.aist.gtf.graph.MetaTag;
import science.aist.gtf.graph.Vertex;
import science.aist.gtf.graph.VertexView;

import java.util.Collection;
import java.util.Optional;

/**
 * <p>This class is used to provide a view on a given vertex, this means, that it decorates an existing vertex
 * but uses its own graph state which does only contain a view on the graph.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class VertexViewImpl<V, E> extends AbstractVertex<V, E> implements VertexView<V, E> {
    @Getter
    private final Vertex<V, E> viewedVertex;

    @Override
    public <T> Collection<MetaTag<T>> getMetaTags() {
        return viewedVertex.getMetaTags();
    }

    @Override
    public <T> boolean addMetaTag(MetaTag<T> metaTag) {
        return viewedVertex.addMetaTag(metaTag);
    }

    @Override
    public <T> boolean removeMetaTag(MetaTag<T> metaTag) {
        return viewedVertex.removeMetaTag(metaTag);
    }

    @Override
    public <T> T getMetaTagValue(String key, Class<T> clazz) {
        return viewedVertex.getMetaTagValue(key, clazz);
    }

    @Override
    public <T> Optional<T> tryGetMetaTagValue(String key, Class<T> clazz) {
        return viewedVertex.tryGetMetaTagValue(key, clazz);
    }

    @Override
    public <T> T getMetaTagValue(String key) {
        return viewedVertex.getMetaTagValue(key);
    }

    @Override
    public <T> Optional<T> tryGetMetaTagValue(String key) {
        return viewedVertex.tryGetMetaTagValue(key);
    }

    @Override
    public void removeMetaTag(String key, Class<?>... clazz) {
        viewedVertex.removeMetaTag(key, clazz);
    }

    @Override
    public V getElement() {
        return viewedVertex.getElement();
    }

    @Override
    public void setElement(V element) {
        throw new UnsupportedOperationException("VertexView does not allow to change the decorated element");
    }
}
