/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Paths;

/**
 * <p>Test class for {@link GeneratorTemplate}</p>
 *
 * @author Andreas Pointner
 */
@ContextConfiguration("classpath:template-test-config.xml")
public class TestGeneratorTemplate extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("sampleTemplateResource")
    private TemplateResource templateResource;

    @Autowired
    private GeneratorTemplateFactory generatorTemplateFactory;

    @Test
    public void testTemplateResourceCreation() {
        // given

        // when

        // then
        Assert.assertNotNull(templateResource);
        Assert.assertNotNull(templateResource.getTemplateResourceLocation());
    }

    @Test
    public void testTemplateResourceLoading() throws Exception {
        // given

        // when
        File yaml = Paths.get(templateResource.getTemplateResourceLocation().toURI()).toFile();

        // then
        Assert.assertNotNull(yaml);
    }

    @Test
    public void testTemplateResourceFactory() {
        // given

        // when
        GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);

        // then
        Assert.assertNotNull(template);
        Assert.assertEquals(template.getTemplateName(), "example");
        Assert.assertEquals(template.getTemplateTasks().size(), 6);
    }
}
