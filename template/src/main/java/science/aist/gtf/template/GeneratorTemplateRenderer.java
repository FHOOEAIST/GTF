/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import science.aist.gtf.transformation.renderer.TransformationRender;

import java.util.Optional;

/**
 * <p>Specification of a Transformation Renderer for Templates</p>
 *
 * @param <R> the result of the renderer
 * @param <S> the source object of the renderer
 * @author Andreas Schuler
 * @since 1.0
 */
public interface GeneratorTemplateRenderer<R, S> extends TransformationRender<Optional<R>, R, GeneratorTemplate, S> {
    /**
     * Executes a given task
     *
     * @param taskResult the task result
     * @param container  the generator template
     * @param task       the task that should be executed
     * @return the result of the task
     */
    R executeTask(R taskResult, GeneratorTemplate container, S task);
}
