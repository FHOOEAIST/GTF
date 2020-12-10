/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph;

import java.util.Collection;
import java.util.Set;

/**
 * <p>Allows reading access to a given graph state</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface GraphStateAccessor<V, E> {
    /**
     * returns a snapshot collection of the vertices in the graph state
     *
     * @return a snapshot collection of the vertices in the graph state
     */
    Collection<Vertex<V, E>> getVertices();

    /**
     * returns the edges for a given vertex
     *
     * @param vertex the vertex for which the edges should be retrieved
     * @return the set of edges for the given vertex
     */
    Set<Edge<V, E>> getEdges(Vertex<V, E> vertex);

    /**
     * Returns the source vertex for a given edge
     *
     * @param edge the edge for which the source vertex should be returned
     * @return the source vertex of the given
     */
    Vertex<V, E> getSource(Edge<V, E> edge);

    /**
     * Returns the target vertex for a given edge
     *
     * @param edge the edge for which the target vertex should be returned
     * @return the target vertex of the given
     */
    Vertex<V, E> getTarget(Edge<V, E> edge);
}
