/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * <p>Test class for {@link TemplateTaskResultTypeEnum}</p>
 *
 * @author Andreas Pointner
 */

public class TemplateTaskResultTypeEnumTest {

    @Test
    public void testFromString1() {
        // given
        String input = "failed";

        // when
        TemplateTaskResultTypeEnum templateTaskResultTypeEnum = TemplateTaskResultTypeEnum.fromString(input);

        // then
        Assert.assertEquals(templateTaskResultTypeEnum, TemplateTaskResultTypeEnum.FAILED);
    }

    @Test
    public void testFromString2() {
        // given
        String input = "success";

        // when
        TemplateTaskResultTypeEnum templateTaskResultTypeEnum = TemplateTaskResultTypeEnum.fromString(input);

        // then
        Assert.assertEquals(templateTaskResultTypeEnum, TemplateTaskResultTypeEnum.SUCCESS);
    }

    @Test
    public void testFromBoolean1() {
        // given

        // when
        TemplateTaskResultTypeEnum templateTaskResultTypeEnum = TemplateTaskResultTypeEnum.fromBoolean(false);

        // then
        Assert.assertEquals(templateTaskResultTypeEnum, TemplateTaskResultTypeEnum.FAILED);
    }

    @Test
    public void testFromBoolean2() {
        // given

        // when
        TemplateTaskResultTypeEnum templateTaskResultTypeEnum = TemplateTaskResultTypeEnum.fromBoolean(true);

        // then
        Assert.assertEquals(templateTaskResultTypeEnum, TemplateTaskResultTypeEnum.SUCCESS);
    }
}