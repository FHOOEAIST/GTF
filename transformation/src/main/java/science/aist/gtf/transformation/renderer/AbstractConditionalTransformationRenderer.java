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
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.util.Optional;

/**
 * <p>Transformation renderer, that is only used if a given renderer condition matches on the input element</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractConditionalTransformationRenderer<Result, Container, Input> implements TransformationRender<Optional<Result>, Result, Container, Input> {

    /**
     * Renderer Condition to be checked if the element can be renderer or not
     */
    private final RendererCondition<Input> rendererCondition;

    @Override
    public Optional<Result> renderElement(Container container, Input currentElement) {
        return Optional.ofNullable(rendererCondition.createCondition().test(currentElement) ? mapProperties(createElement(), container, currentElement) : null);
    }
}
