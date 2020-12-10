/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.renderer;

import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.PropertyPlaceholderHelper;
import science.aist.gtf.template.*;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

/**
 * <p>Generator Template renderer for copying directories</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@CustomLog
public class DirectoryCopyRenderer extends AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> {

    private final PropertyPlaceholderHelper propertyPlaceholderHelper;
    private final IOHandler<GeneratorTemplate> ioHandler;

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.copyDirectory.source.key}")
    private String sourceKey = "src";

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.copyDirectory.destination.key}")
    private String destinationKey = "dest";

    public DirectoryCopyRenderer(RendererCondition<TemplateTask> condition, PropertyPlaceholderHelper propertyPlaceholderHelper, IOHandler<GeneratorTemplate> ioHandler) {
        super(condition);
        this.propertyPlaceholderHelper = propertyPlaceholderHelper;
        this.ioHandler = ioHandler;
    }

    @Override
    public TemplateTaskResult createElement() {
        return new TemplateTaskResult();
    }

    @Override
    public TemplateTaskResult executeTask(TemplateTaskResult taskResult, GeneratorTemplate generatorTemplate, TemplateTask task) {
        // copy directory from source to destination
        String sourceDirectory = task.getProperties().getProperty(sourceKey);
        String destinationDirectory = task.getProperties().getProperty(destinationKey);

        // parse possible expression in strings
        sourceDirectory = propertyPlaceholderHelper.replacePlaceholders(sourceDirectory, generatorTemplate.getProperties());
        destinationDirectory = propertyPlaceholderHelper.replacePlaceholders(destinationDirectory, generatorTemplate.getProperties());
        log.info("Executing template task of type: " + task.getType() + ", copy from " + sourceDirectory + " to " + destinationDirectory);

        try {
            ioHandler.handleDirectoryCopy(generatorTemplate, sourceDirectory, destinationDirectory, false);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.FAILED);
        }

        return taskResult;
    }
}
