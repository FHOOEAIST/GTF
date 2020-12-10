/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.GraphStateAccessor;
import science.aist.gtf.graph.Vertex;

/**
 * <p>Abstract base class to be used for different edge implementation, that are based on a graph state.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public abstract class AbstractEdge<V, E> extends MetaTagCollectionImpl implements Edge<V, E> {
    /**
     * The weight of the edge
     */
    @Getter
    @Setter
    private double weight;

    /**
     * The graph state used inside the edge. The setter is package protected, to allow the {@link GraphStateImpl} to set
     * itself into the edge, if the edge is added to a specific graph state object.
     */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PACKAGE)
    private GraphStateAccessor<V, E> graphState;

    @Override
    public Vertex<V, E> getSource() {
        return getGraphState().getSource(this);
    }

    @Override
    public Vertex<V, E> getTarget() {
        return getGraphState().getTarget(this);
    }
}
