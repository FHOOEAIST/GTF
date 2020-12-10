/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.condition;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.template.TemplateTask;
import science.aist.gtf.template.TemplateTaskTypeEnum;
import science.aist.gtf.template.impl.condition.GraphVizImageCondition;

/**
 * <p>Test class for {@link GraphVizImageCondition}</p>
 *
 * @author Andreas Pointner
 */

public class GraphVizImageConditionTest {
    @Test
    public void testCreateCondition() {
        // given
        GraphVizImageCondition copyCondition = new GraphVizImageCondition();
        TemplateTask templateTask = new TemplateTask();
        templateTask.setType(TemplateTaskTypeEnum.fromString("graphvizimage"));

        // when
        final boolean test = copyCondition.createCondition().test(templateTask);

        // then
        Assert.assertTrue(test);
    }

    @Test
    public void testCreateConditionNeg() {
        // given
        GraphVizImageCondition copyCondition = new GraphVizImageCondition();
        TemplateTask templateTask = new TemplateTask();
        templateTask.setType(TemplateTaskTypeEnum.fromString("somethingElse"));

        // when
        final boolean test = copyCondition.createCondition().test(templateTask);

        // then
        Assert.assertFalse(test);
    }
}