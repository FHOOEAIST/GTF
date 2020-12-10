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
import science.aist.gtf.template.exception.TemplateEngineException;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * <p>Implementation of a template renderer task</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@CustomLog
public class InstantiateTemplateRenderer extends AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> {

    private final PropertyPlaceholderHelper propertyPlaceholderHelper;

    private final TemplateEngine<? super Properties> templateEngine;

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.instantiateTemplate.template.key:template}")
    private String templateKey = "template";

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.instantiateTemplate.destination.key:dest}")
    private String destinationKey = "dest";

    @SuppressWarnings("FieldMayBeFinal" /*We ignore that, because Spring can have problems with @Value when field is finale*/)
    @Value("${renderer.instantiateTemplate.append.key:append}")
    private String appendKey = "append";

    public InstantiateTemplateRenderer(RendererCondition<TemplateTask> condition, PropertyPlaceholderHelper propertyPlaceholderHelper, TemplateEngine<? super Properties> templateEngine) {
        super(condition);
        this.propertyPlaceholderHelper = propertyPlaceholderHelper;
        this.templateEngine = templateEngine;
    }

    @Override
    public TemplateTaskResult createElement() {
        return new TemplateTaskResult();
    }

    @Override
    public TemplateTaskResult executeTask(TemplateTaskResult taskResult, GeneratorTemplate generatorTemplate, TemplateTask task) {
        // copy directory from source to destination
        String template = task.getProperties().getProperty(this.templateKey);
        String destinationFile = task.getProperties().getProperty(destinationKey);
        boolean append = Boolean.parseBoolean(task.getProperties().getProperty(appendKey, "false"));

        // parse possible expression in strings
        template = propertyPlaceholderHelper.replacePlaceholders(template, generatorTemplate.getProperties());
        destinationFile = propertyPlaceholderHelper.replacePlaceholders(destinationFile, generatorTemplate.getProperties());
        log.info("Executing template task of type: " + task.getType() + ", instantiating template file from " + template + " to " + destinationFile);

        try (Writer fw = new OutputStreamWriter(new FileOutputStream(destinationFile, append), StandardCharsets.UTF_8)) {
            templateEngine.process(generatorTemplate.getProperties(), template, fw);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.SUCCESS);
        } catch (TemplateEngineException | IOException e) {
            log.error(e.getMessage(), e);
            taskResult.setTemplateTaskResultTypeEnum(TemplateTaskResultTypeEnum.FAILED);
        }

        return taskResult;
    }

}
