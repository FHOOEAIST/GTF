/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.NonNull;
import science.aist.gtf.template.TemplateEngine;
import science.aist.gtf.template.exception.TemplateEngineException;

import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>Freemarker implementation of the template engine</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class FreemarkerTemplateEngine implements TemplateEngine<Object> {

    private final Configuration configuration = new Configuration(new Version("2.3.27"));

    @Override
    public void process(Object data,
                        @NonNull String templateFile,
                        @NonNull Writer destinationWriter) throws TemplateEngineException {
        try {
            Path templatePath;
            if (templateFile.startsWith("classpath:")) {
                templatePath = Paths.get(getClass().getResource(templateFile.substring(templateFile.indexOf(':') + 1)).toURI());
            } else {
                templatePath = Paths.get(templateFile);
            }

            configuration.setDirectoryForTemplateLoading(templatePath.getParent().toFile());
            configuration.setDefaultEncoding("UTF-8");
            Template tmp = configuration.getTemplate(templatePath.getFileName().toString());

            tmp.process(data, destinationWriter);
            destinationWriter.flush();
        } catch (IOException | TemplateException | URISyntaxException e) {
            throw new TemplateEngineException(e.getMessage(), e);
        }
    }
}
