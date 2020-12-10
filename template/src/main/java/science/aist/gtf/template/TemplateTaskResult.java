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

/**
 * <p>Contains the result of a template task</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
@Getter
@Setter
public class TemplateTaskResult {
    /**
     * The result of a template task
     */
    private TemplateTaskResultTypeEnum templateTaskResultTypeEnum;
}
