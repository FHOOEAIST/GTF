/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.renderer;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.PropertyPlaceholderHelper;
import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.template.*;
import science.aist.gtf.template.impl.renderer.AbstractGeneratorTemplateRenderer;
import science.aist.gtf.template.impl.renderer.FileCopyRenderer;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;
import science.aist.jack.general.util.CastUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * <p>Test class for {@link FileCopyRenderer}</p>
 *
 * @author Andreas Schuler
 * @author Andreas Pointner
 */
@ContextConfiguration("classpath:template-test-config.xml")
public class FileCopyRendererTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("sampleTemplateResource")
    private TemplateResource templateResource;

    @Autowired
    private GeneratorTemplateFactory generatorTemplateFactory;

    @Autowired
    @Qualifier("fileCopyCondition")
    private RendererCondition<TemplateTask> condition;

    @Autowired
    private PropertyPlaceholderHelper propertyPlaceholderHelper;


    @Test
    public void testFileCopyRendererSuccess() {
        // given
        IOHandler<GeneratorTemplate> success = CastUtils.cast(Mockito.mock(IOHandler.class));
        GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);
        Properties properties = new Properties();
        properties.setProperty("target", "target");
        template.setProperties(properties);
        AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> copyDirectoryTemplate =
                new FileCopyRenderer(condition, propertyPlaceholderHelper, success);

        // when
        Optional<TemplateTaskResult> result = copyDirectoryTemplate.renderElement(template, template.getTemplateTasks().get(0));

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getTemplateTaskResultTypeEnum(), TemplateTaskResultTypeEnum.SUCCESS);
        Mockito.verify(success).handleFileCopy(Mockito.eq(template), Mockito.eq("sampledata/samplefile.empty"), Mockito.eq("target/samplefile.empty"));
        Mockito.verifyNoMoreInteractions(success);
    }

    @Test
    public void testFileCopyRendererUseFailed() {
        // given
        IOHandler<GeneratorTemplate> failed = CastUtils.cast(Mockito.mock(IOHandler.class));
        Mockito.doThrow(IOException.class).when(failed).handleFileCopy(Mockito.any(), Mockito.any(), Mockito.any());
        GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);
        Properties properties = new Properties();
        properties.setProperty("target", "target");
        template.setProperties(properties);
        AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> copyDirectoryTemplate =
                new FileCopyRenderer(condition, propertyPlaceholderHelper, failed);

        // when
        Optional<TemplateTaskResult> result = copyDirectoryTemplate.renderElement(template, template.getTemplateTasks().get(0));

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getTemplateTaskResultTypeEnum(), TemplateTaskResultTypeEnum.FAILED);
        Mockito.verify(failed).handleFileCopy(Mockito.eq(template), Mockito.eq("sampledata/samplefile.empty"), Mockito.eq("target/samplefile.empty"));
        Mockito.verifyNoMoreInteractions(failed);
    }
}