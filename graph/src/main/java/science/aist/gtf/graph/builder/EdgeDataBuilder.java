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

/**
 * <p>EdgeDataBuilder is used inside {@link EdgeBuilder} and is used to add the data element (decorated element) to
 * the created edge.</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface EdgeDataBuilder<V, E> {
    /**
     * Add the given value to the edge (which can be retrieved using {@link Edge#getElement()} and returns the original
     * {@link GraphBuilder}
     *
     * @param value the value to be added
     * @return the original {@link GraphBuilder}
     */
    GraphBuilder<V, E> data(E value);

    /**
     * Add the given value to the edge (which can be retrieved using {@link Edge#getElement()} and returns an {@link
     * EdgeWithBuilder} that allows to add a callback method, that is called with the created edge. This allows to e.g.
     * add {@link MetaTag}s.
     *
     * @param value the value to be added
     * @return a {@link EdgeWithBuilder}
     */
    EdgeWithBuilder<V, E> dataWith(E value);
}
