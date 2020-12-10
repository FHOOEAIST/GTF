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
import science.aist.gtf.template.exception.TemplateEngineException;
import science.aist.gtf.template.impl.renderer.AbstractGeneratorTemplateRenderer;
import science.aist.gtf.template.impl.renderer.InstantiateTemplateRenderer;
import science.aist.gtf.transformation.renderer.condition.RendererCondition;
import science.aist.jack.general.util.CastUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * <p>Test class for {@link InstantiateTemplateRenderer}</p>
 *
 * @author Andreas Schuler
 * @author Andreas Pointner
 */
@ContextConfiguration("classpath:template-test-config.xml")
public class InstantiateTemplateRendererTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("sampleTemplateResource")
    private TemplateResource templateResource;
    @Autowired
    private GeneratorTemplateFactory generatorTemplateFactory;
    @Autowired
    @Qualifier("instantiateTemplateCondition")
    private RendererCondition<TemplateTask> condition;
    @Autowired
    private PropertyPlaceholderHelper propertyPlaceholderHelper;

    @Test
    public void testInstantiateTemplateRendererSuccess() throws TemplateEngineException {
        // given
        TemplateEngine<Object> success = CastUtils.cast(Mockito.mock(TemplateEngine.class));
        GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);
        Properties properties = new Properties();
        properties.setProperty("target", "target/");
        template.setProperties(properties);
        AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> instantiateTemplateRenderer =
                new InstantiateTemplateRenderer(condition, propertyPlaceholderHelper, success);

        // when
        Optional<TemplateTaskResult> result = instantiateTemplateRenderer.renderElement(template, template.getTemplateTasks().get(2));

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getTemplateTaskResultTypeEnum(), TemplateTaskResultTypeEnum.SUCCESS);
        Mockito.verify(success).process(Mockito.any(Map.class), Mockito.eq("templates/graphWiz.ftl"), Mockito.any());
        Mockito.verifyNoMoreInteractions(success);
    }

    @Test
    public void testInstantiateTemplateRendererUseFailed() throws TemplateEngineException {
        // given
        TemplateEngine<Object> failed = CastUtils.cast(Mockito.mock(TemplateEngine.class));
        Mockito.doThrow(TemplateEngineException.class).when(failed).process(Mockito.any(), Mockito.any(), Mockito.any());
        GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);
        Properties properties = new Properties();
        properties.setProperty("target", "target/");
        template.setProperties(properties);
        AbstractGeneratorTemplateRenderer<TemplateTaskResult, TemplateTask> instantiateTemplateRenderer =
                new InstantiateTemplateRenderer(condition, propertyPlaceholderHelper, failed);

        // when
        Optional<TemplateTaskResult> result = instantiateTemplateRenderer.renderElement(template, template.getTemplateTasks().get(2));

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getTemplateTaskResultTypeEnum(), TemplateTaskResultTypeEnum.FAILED);
        Mockito.verify(failed).process(Mockito.any(Map.class), Mockito.eq("templates/graphWiz.ftl"), Mockito.any());
        Mockito.verifyNoMoreInteractions(failed);
    }


}
