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

/**
 * <p>Interface to represent a vertex</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Schuler
 * @author Andreas Pointner
 * @since 1.0
 */
public interface Vertex<V, E> extends MetaTagCollection {
    /**
     * vertex has a collection of edges, thus pointing to several other vertices
     *
     * @return a collection of vertex
     */
    Collection<Vertex<V, E>> getAdjacentVertices();

    /**
     * @return returns a collection of edges
     */
    Collection<Edge<V, E>> getEdges();

    /**
     * <p>Returns all edges that are incoming into this vertex.</p>
     * <p>If this vertex is seen as v, then all edges e are returned that match x-(e)-&gt;v</p>
     *
     * @return returns a collection of edges
     */
    Collection<Edge<V, E>> getIncomingEdges();

    /**
     * <p>Returns all edges which are outgoing from this vertex.</p>
     * <p>If this vertex is seen as v, then all edges e are returned that match v-(e)-&gt;x</p>
     *
     * @return returns a collection of edges
     */
    Collection<Edge<V, E>> getOutgoingEdges();

    /**
     * @return returns the decorated element
     */
    V getElement();
}
