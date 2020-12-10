/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl.condition;

import science.aist.gtf.template.TemplateTask;
import science.aist.gtf.template.TemplateTaskTypeEnum;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.util.function.Predicate;

/**
 * <p>Renderer Condition for copy directory</p>
 *
 * @author Andreas Schuler
 * @since 1.0
 */
public class DirectoryCopyCondition implements RendererCondition<TemplateTask> {

    @Override
    public Predicate<TemplateTask> createCondition() {
        return element -> TemplateTaskTypeEnum.COPYDIR == element.getType();
    }
}

