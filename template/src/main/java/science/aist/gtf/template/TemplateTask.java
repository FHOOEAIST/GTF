/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * <p>A specific template task</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@Getter
@Setter
public class TemplateTask {
    /**
     * The name of the task
     */
    private String name;

    /**
     * the type of the task
     */
    private TemplateTaskTypeEnum type;

    /**
     * The properties that are available for this task
     */
    private Properties properties;
}
