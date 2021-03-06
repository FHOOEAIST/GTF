/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph;

/**
 * <p>Interface that allows to provide a different view on a vertex. A vertex view delegates all method to the
 * viewed vertex except, the method, that belong to the graph structure. For this its own state will be maintained that
 * allows to e.g. give a view on a subgraph.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface VertexView<V, E> extends Vertex<V, E> {
    /**
     * Retrieves the viewed vertex
     *
     * @return the vertex on which the view is created
     */
    Vertex<V, E> getViewedVertex();
}
