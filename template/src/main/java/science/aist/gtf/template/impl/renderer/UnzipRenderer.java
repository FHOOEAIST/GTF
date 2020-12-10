/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.renderer;

import org.springframework.util.PropertyPlaceholderHelper;
import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.IOHandler;
import science.aist.gtf.template.TemplateTask;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

/**
 * <p>Renderer to unzip a file</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class UnzipRenderer extends FileOperationRenderer {
    public UnzipRenderer(RendererCondition<TemplateTask> rendererCondition, PropertyPlaceholderHelper propertyPlaceholderHelper, IOHandler<GeneratorTemplate> ioHandler) {
        super(rendererCondition, propertyPlaceholderHelper, ioHandler::unzipFile);
    }
}
