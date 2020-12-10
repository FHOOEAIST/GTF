/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.renderer;

import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.GeneratorTemplateRenderer;
import science.aist.gtf.transformation.renderer.AbstractConditionalTransformationRenderer;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.util.Optional;

/**
 * <p>Abstract generator template renderer</p>
 *
 * @author Andreas Pointner
 * @author Andreas Schuler
 * @since 1.0
 */
public abstract class AbstractGeneratorTemplateRenderer<R, S> extends AbstractConditionalTransformationRenderer<R, GeneratorTemplate, S> implements GeneratorTemplateRenderer<R, S> {
    public AbstractGeneratorTemplateRenderer(RendererCondition<S> rendererCondition) {
        super(rendererCondition);
    }

    @Override
    public R executeTask(R taskResult, GeneratorTemplate container, S task) {
        // by default just return the input
        return taskResult;
    }

    @Override
    public R createElement() {
        // by default this operation is unsupported
        throw new UnsupportedOperationException("The method createElement is not supported for the type " + this.getClass().getCanonicalName());
    }

    @Override
    public R mapProperties(R r, GeneratorTemplate generatorTemplate, S currentElement) {
        // by default no mapping of properties is necessary
        // Override here to map properties from input to release
        return r;
    }

    @Override
    public Optional<R> renderElement(GeneratorTemplate container, S currentElement) {
        return Optional.ofNullable(getRendererCondition().createCondition().test(currentElement) ? executeTask(mapProperties(createElement(), container, currentElement), container, currentElement) : null);
    }

}
