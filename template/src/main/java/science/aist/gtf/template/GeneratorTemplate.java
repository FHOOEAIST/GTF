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
import lombok.ToString;

import java.util.List;
import java.util.Properties;

/**
 * <p>This class is used to represent a template that is rendered to a desired output sink. A template can, e.g.
 * represent a gradle build file and contains all the data relevant to render the template to the desired output format.
 * </p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class GeneratorTemplate {
    /**
     * The name of the template
     */
    private String templateName;

    /**
     * The template tasks that should be executed
     */
    private List<TemplateTask> templateTasks;

    /**
     * The properties that are supplied for the template
     */
    private Properties properties;
}
