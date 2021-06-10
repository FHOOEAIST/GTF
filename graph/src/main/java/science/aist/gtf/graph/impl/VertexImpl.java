/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.impl;

import lombok.AllArgsConstructor;

/**
 * <p>Implementation of a Vertex</p>
 *
 * @param <V> the type of the decorated vertex value
 * @param <E> the type of the decorated edge value
 * @author Andreas Schuler
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class VertexImpl<V, E> extends AbstractVertex<V, E> {
    /**
     * The element in the vertex
     */
    private V decoratedElement;

    @Override
    public V getElement() {
        return decoratedElement;
    }

    @Override
    public void setElement(V element) {
        decoratedElement = element;
    }

    /**
     * Generated Code
     *
     * @return String representation of the object
     */
    @Override
    public String toString() {
        return "VertexImpl (" + super.toString() + "){" +
                "decoratedElement=" + decoratedElement +
                '}';
    }

}
