/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.PropertyPlaceholderHelper;
import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.template.*;
import science.aist.gtf.template.impl.renderer.AbstractGeneratorTemplateRenderer;
import science.aist.gtf.template.impl.renderer.ShellCommandRenderer;
import science.aist.gtf.template.impl.renderer.UnzipRenderer;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;

import java.util.Optional;
import java.util.Properties;

/**
 * <p>Test class for {@link UnzipRenderer}</p>
 *
 * @author Andreas Pointner
 */
@ContextConfiguration("classpath:template-test-config.xml")
public class ShellCommandRendererTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("sampleTemplateResource")
    private TemplateResource templateResource;

    @Autowired
    private GeneratorTemplateFactory generatorTemplateFactory;

    @Autowired
    @Qualifier("shellCommandCondition")
    private RendererCondition<TemplateTask> condition;

    @Autowired
    private PropertyPlaceholderHelper propertyPlaceholderHelper;

    @Test
    public void testShellCommandRendererSuccess() {
        // given
        GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);
        Properties properties = new Properties();
        properties.put("target", System.getProperty("user.dir") + "/target/");
        template.setProperties(properties);
        AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> unzipRenderer =
                new ShellCommandRenderer(condition, propertyPlaceholderHelper);

        // when
        Optional<TemplateTaskResult> result = unzipRenderer.renderElement(template, template.getTemplateTasks().get(5));

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getTemplateTaskResultTypeEnum(), TemplateTaskResultTypeEnum.SUCCESS);
    }
}