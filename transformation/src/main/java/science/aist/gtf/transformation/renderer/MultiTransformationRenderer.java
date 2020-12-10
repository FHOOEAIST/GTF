/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation.renderer;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * <p>Multi transformation renderer, that iterates over all the child renderers, and returns the result of the first
 * renderer that itself returns a result.</p>
 *
 * @param <Result>    The result type
 * @param <Container> The type of the input container
 * @param <Input>     The type of the input element
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class MultiTransformationRenderer<Result, Container, Input> implements TransformationRender<Result, Result, Container, Input> {
    /**
     * The child renderers
     */
    private final List<TransformationRender<Optional<Result>, ?, ? super Container, ? super Input>> childRenderers;

    /**
     * Renders a given element with the first matching child renderer
     *
     * @param container      the container
     * @param currentElement the current element
     * @return the result of the first matching child renderer
     * @throws IllegalStateException if no child renderer returns a result
     */
    @Override
    public Result renderElement(Container container, Input currentElement) {
        return childRenderers.stream()
                .map(cr -> cr.renderElement(container, currentElement))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No renderer returned an element"));
    }

    /**
     * Function not supported in multi renderer
     *
     * @return nothing
     * @throws UnsupportedOperationException not supported
     */
    @Override
    public Result createElement() {
        throw new UnsupportedOperationException("Not supported to create the element in a MultiTransformationRenderer");
    }

    /**
     * Function not supported in multi renderer
     *
     * @return nothing
     * @throws UnsupportedOperationException not supported
     */
    @Override
    public Result mapProperties(Result result, Container container, Input currentElement) {
        throw new UnsupportedOperationException("Not supported to map the properties in a MultiTransformationRenderer");
    }
}
