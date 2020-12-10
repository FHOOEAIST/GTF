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

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Abstract base class to be used for different vertex implementation, that are based on a graph state.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public abstract class AbstractVertex<V, E> extends MetaTagCollectionImpl implements Vertex<V, E> {
    /**
     * The graph state used inside the vertex. The setter is package protected, to allow the {@link GraphStateImpl} to
     * set itself into the edge, if the edge is added to a specific graph state object.
     */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PACKAGE)
    private GraphStateAccessor<V, E> graphState;


    @Override
    public Collection<Vertex<V, E>> getAdjacentVertices() {
        return Stream.of(
                getIncomingEdges().stream().map(Edge::getSource),
                getOutgoingEdges().stream().map(Edge::getTarget)
        ).flatMap(Function.identity())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Edge<V, E>> getEdges() {
        return getGraphState().getEdges(this);
    }

    @Override
    public Collection<Edge<V, E>> getIncomingEdges() {
        return this.getEdges().stream().filter(e -> e.getTarget().equals(this)).collect(Collectors.toSet());
    }

    @Override
    public Collection<Edge<V, E>> getOutgoingEdges() {
        return this.getEdges().stream().filter(e -> e.getSource().equals(this)).collect(Collectors.toSet());
    }

}
