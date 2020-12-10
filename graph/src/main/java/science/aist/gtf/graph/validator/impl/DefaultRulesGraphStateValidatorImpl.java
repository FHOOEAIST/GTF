/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.validator.impl;

import science.aist.gtf.graph.GraphStateAccessor;
import science.aist.gtf.graph.analyzer.ConnectedGraphAnalyzer;
import science.aist.gtf.graph.analyzer.NumberOfEdgesAnalyzer;
import science.aist.gtf.graph.analyzer.NumberOfVertexAnalyzer;
import science.aist.gtf.graph.validator.GraphStateValidator;

/**
 * <p>Default Aist Way on how a graph has to be, to be valid</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public class DefaultRulesGraphStateValidatorImpl<V, E> implements GraphStateValidator<V, E> {

    private final RuleBasedGraphStateValidatorImpl<V, E> ruleBasedGraphStateValidator;

    public DefaultRulesGraphStateValidatorImpl() {
        ruleBasedGraphStateValidator = new RuleBasedGraphStateValidatorImpl<>();
        // must have at least two vertices (meaning a graph that contains a vertex pointing to itself, does not count)
        ruleBasedGraphStateValidator.addAnalyzerRule(new NumberOfVertexAnalyzer<>(), x -> x >= 2);
        // must have at least one edge
        ruleBasedGraphStateValidator.addAnalyzerRule(new NumberOfEdgesAnalyzer<>(), x -> x >= 1);
        // must be a connected graph
        ruleBasedGraphStateValidator.addAnalyzerRule(new ConnectedGraphAnalyzer<>(), Boolean.TRUE::equals);
    }

    @Override
    public boolean isValidGraphState(GraphStateAccessor<V, E> graphState) {
        return ruleBasedGraphStateValidator.isValidGraphState(graphState);
    }
}
