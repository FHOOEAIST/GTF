/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation;

import science.aist.gtf.graph.Graph;

/**
 * <p>Implementation for a GraphTransformer which uses a Graph with a generic input as an input and returns
 * any output type</p>
 *
 * @param <InputV> the generic input vertex type
 * @param <InputE> the generic input edge type
 * @param <Output> the generic output type
 * @author Andreas Pointner
 * @since 1.0
 */
public interface GraphTransformer<InputV, InputE, Output> extends Transformer<Graph<InputV, InputE>, Output> {
}
