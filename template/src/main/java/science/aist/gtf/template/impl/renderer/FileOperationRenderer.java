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
 * <p>Base class to perform operations on files</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@CustomLog
public class FileOperationRenderer extends AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> {
    private final PropertyPlaceholderHelper propertyPlaceholderHelper;

    private final FilePathOperation<GeneratorTemplate> filePathOperation;

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.copyFile.source.key}")
    private String sourceKey = "src";

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.copyFile.destination.key}")
    private String destinationKey = "dest";

    public FileOperationRenderer(RendererCondition<TemplateTask> rendererCondition, PropertyPlaceholderHelper propertyPlaceholderHelper, FilePathOperation<GeneratorTemplate> filePathOperation) {
        super(rendererCondition);
        this.propertyPlaceholderHelper = propertyPlaceholderHelper;
        this.filePathOperation = filePathOperation;
    }

    @Override
    public TemplateTaskResult createElement() {
        return new TemplateTaskResult();
    }

    @Override
    public TemplateTaskResult executeTask(TemplateTaskResult taskResult, GeneratorTemplate generatorTemplate, TemplateTask task) {
        // copy directory from source to destination
        String sourceFile = task.getProperties().getProperty(sourceKey);
        String destinationFile = task.getProperties().getProperty(destinationKey);

        // parse possible expression in strings
        sourceFile = propertyPlaceholderHelper.replacePlaceholders(sourceFile, generatorTemplate.getProperties());
        destinationFile = propertyPlaceholderHelper.replacePlaceholders(destinationFile, generatorTemplate.getProperties());
        log.info("Executing template task of type: " + task.getType() + ", executing a file operation from " + sourceFile + " to " + destinationFile);

        try {
            filePathOperation.perform(generatorTemplate, sourceFile, destinationFile);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.FAILED);
        }

        return taskResult;
    }
}
