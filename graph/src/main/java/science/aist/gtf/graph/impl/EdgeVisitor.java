/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import science.aist.gtf.graph.Edge;
import science.aist.gtf.graph.Visitor;

/**
 * <p>Interface to implement visitor pattern for edges</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Schuler
 * @since 1.0
 */
public interface EdgeVisitor<V, E> extends Visitor<Edge<V, E>> {
}
