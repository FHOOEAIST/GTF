/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation.renderer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.Vertex;

import java.util.List;
import java.util.Optional;

/**
 * <p>Provides a Multi Graph Transformation Renderer, which has a list of child renderer and returns the
 * result of the first matching one.</p>
 *
 * @param <Result>  The result type
 * @param <SourceE> The input type of the decorated edge element
 * @param <SourceV> The input type of the decorated vertex element
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class MultiGraphTransformationRenderer<Result, SourceV, SourceE> implements TransformationRender<Result, Result, Graph<SourceV, SourceE>, Vertex<SourceV, SourceE>> {

    /**
     * List of Child Renderers
     */
    @Getter(AccessLevel.PROTECTED)
    private final List<GraphTransformationRenderer<? extends Result, SourceV, SourceE>> childRenderers;

    /**
     * Executes the list of child renderer, and returns the result of the first one, that matches
     *
     * @param container      the container
     * @param currentElement the current element
     * @return the value of the first renderer, that matches
     * @throws IllegalStateException if no renderer matches
     */
    @Override
    public Result renderElement(Graph<SourceV, SourceE> container, Vertex<SourceV, SourceE> currentElement) {
        return getChildRenderers().stream()
                .map(cr -> cr.renderElement(container, currentElement))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No renderer matches!"));
    }

    /**
     * No supported in {@link MultiGraphTransformationRenderer}
     *
     * @throws UnsupportedOperationException not supported in this class
     */
    @Override
    public Result createElement() {
        throw new UnsupportedOperationException("Not supported in MultiGraphTransformationRenderer");
    }

    /**
     * No supported in {@link MultiGraphTransformationRenderer}
     *
     * @throws UnsupportedOperationException not supported in this class
     */
    @Override
    public Result mapProperties(Result result, Graph<SourceV, SourceE> sGraph, Vertex<SourceV, SourceE> currentElement) {
        throw new UnsupportedOperationException("Not supported in MultiGraphTransformationRenderer");
    }
}
