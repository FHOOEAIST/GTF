/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl;

import lombok.CustomLog;
import lombok.Setter;
import science.aist.gtf.template.*;
import science.aist.gtf.template.exception.TemplateEngineException;
import science.aist.gtf.template.impl.renderer.AbstractGeneratorTemplateRenderer;
import science.aist.jack.general.function.ToBooleanBiFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static science.aist.jack.exception.ExceptionUtils.unchecked;

/**
 * <p>Execute a "pipeline" of {@link GeneratorTemplateRenderer}</p>
 *
 * @author Andreas Pointner
 * @author Andreas Schuler
 * @since 1.0
 */
@CustomLog
public class GeneratorTemplateRendererImpl extends AbstractGeneratorTemplateRenderer<GeneratorTemplate, Void> {

    private final List<GeneratorTemplateRenderer<? extends TemplateTaskResult, ? super TemplateTask>> childRenderer;

    @Setter
    private ToBooleanBiFunction<TemplateTask, TemplateTaskResult> taskVerifier = (i, o) -> o.getTemplateTaskResultTypeEnum() == TemplateTaskResultTypeEnum.SUCCESS;

    @Setter
    private BiConsumer<TemplateTask, Boolean> verificationResultSink = (task, res) -> {
        if (Boolean.FALSE.equals(res)) throw unchecked(new TemplateEngineException("Failed to execute task" + task));
    };

    @SuppressWarnings("unused" /*This is because it is used in spring xml configs only */)
    public GeneratorTemplateRendererImpl(List<GeneratorTemplateRenderer<? extends TemplateTaskResult, ? super TemplateTask>> childRenderer) {
        super(null);
        this.childRenderer = childRenderer;
    }

    @SafeVarargs
    @SuppressWarnings("unused" /*This is because it is used in spring xml configs only */)
    public GeneratorTemplateRendererImpl(GeneratorTemplateRenderer<? extends TemplateTaskResult, ? super TemplateTask>... childRenderer) {
        super(null);
        this.childRenderer = Arrays.asList(childRenderer);
    }

    @Override
    public Optional<GeneratorTemplate> renderElement(GeneratorTemplate container, Void none) {
        container.getTemplateTasks().forEach(templateTask -> {
                    if (childRenderer.stream().noneMatch(renderer ->
                            renderer.renderElement(container, templateTask)
                                    .map(res -> taskVerifier.applyAsBoolean(templateTask, res))
                                    .map(res -> {
                                        verificationResultSink.accept(templateTask, res);
                                        return true; // true: template task executed
                                    })
                                    .orElse(false) // false: no execution happened
                    )) {
                        log.warn("No renderer felt responsible for handling template task ({}) of type ({})", templateTask, templateTask.getType().getTemplateTaskType());
                    }
                }
        );
        return Optional.of(container);
    }

}
