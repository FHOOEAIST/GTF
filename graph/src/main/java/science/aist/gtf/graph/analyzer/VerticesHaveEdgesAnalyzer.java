/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.analyzer;

import science.aist.gtf.graph.GraphAnalyzer;
import science.aist.gtf.graph.GraphStateAccessor;
import science.aist.gtf.graph.Vertex;

import java.util.Collection;

/**
 * <p>Analyzes if every vertex in the graph has at least a single edge</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public class VerticesHaveEdgesAnalyzer<V, E> implements GraphAnalyzer<V, E, Boolean> {
    @Override
    public Boolean analyzeGraphState(GraphStateAccessor<V, E> graphStateAccessor) {
        Collection<Vertex<V, E>> vertices = graphStateAccessor.getVertices();
        if (vertices.isEmpty()) return false;
        return vertices.stream().noneMatch(v -> graphStateAccessor.getEdges(v).isEmpty());
    }
}
