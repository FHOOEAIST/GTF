/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.builder;

import science.aist.gtf.graph.MetaTag;
import science.aist.gtf.graph.Vertex;

import java.util.function.Consumer;

/**
 * <p>VertexBuilderWith that is used inside {@link GraphBuilder} and allows to add a callback method to the vertex
 * creation method</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface VertexBuilderWith<V, E> {
    /**
     * Adds a callback method, that is called once the vertex is created. This allows adding additional information to
     * the vertex, such as {@link MetaTag}s. Note: This method is also called if a cached instance of a vertex is
     * reused, which could potentially already have specific information set.
     *
     * @param callback the callback method to be executed
     * @return the instance of the original graph builder
     */
    GraphBuilder<V, E> with(Consumer<Vertex<V, E>> callback);
}
