/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation.renderer;

import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.Vertex;

import java.util.Optional;

/**
 * <p>Override parameters from {@link TransformationRender} specifically for graphs</p>
 *
 * @param <R> Result type
 * @param <S> type of the decorated element of the vertex
 * @param <E> type of the decorated element of the ege
 * @author Andreas Schuler
 * @since 1.0
 */
public interface GraphTransformationRenderer<R, S, E> extends TransformationRender<Optional<R>, R, Graph<S, E>, Vertex<S, E>> {

}
