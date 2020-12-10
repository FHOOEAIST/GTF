/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.builder;

import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.MetaTag;

import java.util.function.Consumer;

/**
 * <p>EdgeWithBuilder that is used inside {@link EdgeBuilder} and {@link EdgeDataBuilder} and allows to add a
 * callback method, that is called with the created edge. This allows to e.g. add {@link MetaTag}s.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface EdgeWithBuilder<V, E> {
    /**
     * Add a callback method, that is executed when the edge is created. This method allows performing different action
     * on the created edge. E.g. change the initial weight or add {@link MetaTag}s
     *
     * @param callback the callback method for the edge
     * @return the original {@link GraphBuilder}
     */
    GraphBuilder<V, E> with(Consumer<Edge<V, E>> callback);
}
