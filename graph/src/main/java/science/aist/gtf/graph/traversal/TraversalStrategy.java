/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.traversal;


import science.aist.gtf.graph.Visitor;

/**
 * <p>Interface for traversal strategy</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Schuler
 * @since 1.0
 */
public interface TraversalStrategy<V, E> {
    /**
     * Traverses through the graph
     *
     * @param visitor visitor is call for every vertex which is visited
     */
    void traverse(Visitor<V> visitor);

    /**
     * Traverses through the graph
     *
     * @param vertexVisitor visitor which is call when a vertex is visited
     * @param edgeVisitor   visitor which is call when an edge is visited
     */
    void traverse(Visitor<V> vertexVisitor, Visitor<E> edgeVisitor);
}
