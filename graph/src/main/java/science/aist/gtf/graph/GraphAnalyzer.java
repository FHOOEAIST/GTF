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
 * <p>Performs an analyzes of the graph</p>
 *
 * @param <R> result of the analysis
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface GraphAnalyzer<V, E, R> {
    /**
     * Analyzes a graph state
     *
     * @param graphStateAccessor the graph state to be analyzed
     * @return the analyze result
     */
    R analyzeGraphState(GraphStateAccessor<V, E> graphStateAccessor);
}
