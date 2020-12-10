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
 * <p>Renderer implementation to create a directory</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@CustomLog
public class DirectoryCreateRenderer extends AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> {

    private final PropertyPlaceholderHelper propertyPlaceholderHelper;
    private final IOHandler<GeneratorTemplate> ioHandler;

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.createDirectory.path.key}")
    private String pathKey = "path";

    public DirectoryCreateRenderer(RendererCondition<TemplateTask> condition, PropertyPlaceholderHelper propertyPlaceholderHelper, IOHandler<GeneratorTemplate> ioHandler) {
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
        String path = task.getProperties().getProperty(pathKey);
        // parse possible expression in strings
        path = propertyPlaceholderHelper.replacePlaceholders(path, generatorTemplate.getProperties());
        log.info("Executing template task of type: " + task.getType() + ", create dir at " + path);
        try {
            this.ioHandler.handleDirectoryCreate(generatorTemplate, path);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.FAILED);

        }
        return taskResult;
    }
}
