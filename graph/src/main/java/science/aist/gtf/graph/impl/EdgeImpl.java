/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>Implementation of Edge</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Schuler
 * @since 1.0
 */
@RequiredArgsConstructor
public class EdgeImpl<V, E> extends AbstractEdge<V, E> {
    /**
     * Edge Element
     */
    @Getter
    private final E element;
}
