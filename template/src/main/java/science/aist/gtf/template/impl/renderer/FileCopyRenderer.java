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
import org.springframework.util.PropertyPlaceholderHelper;
import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.IOHandler;
import science.aist.gtf.template.TemplateTask;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

/**
 * <p>Implementation of a file copy task</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@CustomLog
public class FileCopyRenderer extends FileOperationRenderer {
    public FileCopyRenderer(RendererCondition<TemplateTask> condition, PropertyPlaceholderHelper propertyPlaceholderHelper, IOHandler<GeneratorTemplate> ioHandler) {
        super(condition, propertyPlaceholderHelper, ioHandler::handleFileCopy);
    }
}
