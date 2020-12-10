/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import science.aist.gtf.template.exception.TemplateEngineException;

import java.io.Writer;

/**
 * <p>The Template Engine</p>
 *
 * @param <C> The context of the template engine
 * @author Andreas Schuler
 * @since 1.0
 */
public interface TemplateEngine<C> {
    /**
     * Runs the engine
     *
     * @param context           the context in which this should happen
     * @param templateFile      the template file
     * @param destinationWriter the writer for the result
     * @throws TemplateEngineException template exception if something fails during processing
     */
    void process(C context, String templateFile, Writer destinationWriter) throws TemplateEngineException;
}
