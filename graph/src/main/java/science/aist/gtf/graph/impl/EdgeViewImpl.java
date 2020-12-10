/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import lombok.Getter;
import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.EdgeView;
import science.aist.gtf.graph.MetaTag;

import java.util.Collection;
import java.util.Optional;

/**
 * <p>This class is used to provide a view on a given edge, this means, that it decorates an existing edge
 * but uses its own graph state which does only contain a view on the graph.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public class EdgeViewImpl<V, E> extends AbstractEdge<V, E> implements EdgeView<V, E> {

    @Getter
    private final Edge<V, E> viewedEdge;

    public EdgeViewImpl(Edge<V, E> viewedEdge) {
        this.viewedEdge = viewedEdge;
        setWeight(viewedEdge.getWeight());
    }

    @Override
    public E getElement() {
        return viewedEdge.getElement();
    }

    @Override
    public <T> T getMetaTagValue(String key, Class<T> clazz) {
        return viewedEdge.getMetaTagValue(key, clazz);
    }

    @Override
    public <T> Optional<T> tryGetMetaTagValue(String key, Class<T> clazz) {
        return viewedEdge.tryGetMetaTagValue(key, clazz);
    }

    @Override
    public <T> T getMetaTagValue(String key) {
        return viewedEdge.getMetaTagValue(key);
    }

    @Override
    public <T> Optional<T> tryGetMetaTagValue(String key) {
        return viewedEdge.tryGetMetaTagValue(key);
    }

    @Override
    public void removeMetaTag(String key, Class<?>... clazz) {
        viewedEdge.removeMetaTag(key, clazz);
    }

    @Override
    public <T> Collection<MetaTag<T>> getMetaTags() {
        return viewedEdge.getMetaTags();
    }

    @Override
    public <T> boolean addMetaTag(MetaTag<T> metaTag) {
        return viewedEdge.addMetaTag(metaTag);
    }

    @Override
    public <T> boolean removeMetaTag(MetaTag<T> metaTag) {
        return viewedEdge.removeMetaTag(metaTag);
    }
}
