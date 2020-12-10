/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.validator;

import science.aist.gtf.graph.GraphStateAccessor;

/**
 * <p>Calculates if the given graph state is valid</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Pointner
 * @since 1.0
 */
public interface GraphStateValidator<V, E> {
    /**
     * verifies if a given graph state confirms to a given ruleset
     *
     * @param graphState the graph state to be verified
     * @return to if the graph state is valid
     */
    boolean isValidGraphState(GraphStateAccessor<V, E> graphState);
}
