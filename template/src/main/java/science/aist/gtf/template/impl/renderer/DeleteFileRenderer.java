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
import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.TemplateTask;
import science.aist.gtf.template.TemplateTaskResult;
import science.aist.gtf.template.TemplateTaskResultTypeEnum;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * <p>Task to delete a file</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@CustomLog
public class DeleteFileRenderer extends AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> {

    private final PropertyPlaceholderHelper propertyPlaceholderHelper;
    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.deleteFile.path.key}")
    private String pathKey = "path";
    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.deleteFile.errorNoExist.key}")
    private String errorNoExistKey = "errorNoExist";

    public DeleteFileRenderer(RendererCondition<TemplateTask> rendererCondition, PropertyPlaceholderHelper propertyPlaceholderHelper) {
        super(rendererCondition);
        this.propertyPlaceholderHelper = propertyPlaceholderHelper;
    }

    @Override
    public TemplateTaskResult executeTask(TemplateTaskResult taskResult, GeneratorTemplate generatorTemplate, TemplateTask task) {
        String path = task.getProperties().getProperty(pathKey);
        // parse possible expression in strings
        path = propertyPlaceholderHelper.replacePlaceholders(path, generatorTemplate.getProperties());
        log.info("Executing template task of type: " + task.getType() + ", create dir at " + path);

        boolean errorNoExist = Boolean.parseBoolean(task.getProperties().getProperty(errorNoExistKey, "true"));

        try {
            deleteFile(path, errorNoExist);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.FAILED);
        }

        return taskResult;
    }

    @SuppressWarnings("java:S1162" /* ignore throwing of NoSuchFileException */)
    private void deleteFile(String path, boolean errorNoExist) throws IOException {
        try {
            Files.delete(Path.of(path));
        } catch (NoSuchFileException noSuchFileException) {
            if (errorNoExist) {
                throw noSuchFileException;
            } else {
                log.info("File {} not found, but errorNoExist is false", path);
            }
        }
    }

    @Override
    public TemplateTaskResult createElement() {
        return new TemplateTaskResult();
    }
}
