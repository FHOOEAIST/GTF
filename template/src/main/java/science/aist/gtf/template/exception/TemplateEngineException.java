/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.exception;

import lombok.NoArgsConstructor;
import science.aist.gtf.template.TemplateEngine;

/**
 * <p>Exception Class for {@link TemplateEngine}</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@NoArgsConstructor
public class TemplateEngineException extends Exception {
    public TemplateEngineException(String message) {
        super(message);
    }

    public TemplateEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
