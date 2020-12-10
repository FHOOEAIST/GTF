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
 * <p>an edge has a source and a target</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Schuler
 * @since 1.0
 */
public interface Edge<V, E> extends MetaTagCollection {
    /**
     * get the source of the edge
     *
     * @return the source
     */
    Vertex<V, E> getSource();

    /**
     * get the target of the edge
     *
     * @return the target
     */
    Vertex<V, E> getTarget();

    /**
     * Returns the weight of the edge
     *
     * @return the weight of the edge
     */
    double getWeight();

    /**
     * Returns the element associated with the edge
     *
     * @return the element associated with the edge, or null if not present
     */
    E getElement();

}
