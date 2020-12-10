/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl;

import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.GeneratorTemplateFactory;
import science.aist.gtf.template.TemplateResource;

import java.util.List;

/**
 * <p>Test class for {@link GeneratorTemplateFactoryImpl}</p>
 *
 * @author Andreas Pointner
 */

public class GeneratorTemplateFactoryImplTest {
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testListAvailableTemplateResources() {
        // given
        GeneratorTemplateFactory gtf = new GeneratorTemplateFactoryImpl();

        // when
        gtf.listAvailableTemplateResources();

        // then
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testLoadAllGeneratorTemplates() {
        // given
        GeneratorTemplateFactory gtf = new GeneratorTemplateFactoryImpl();

        // when
        gtf.loadAllGeneratorTemplates();

        // then
    }

    @Test
    public void testLoadAllGeneratorTemplates2() {
        // given
        Resource r = Mockito.mock(Resource.class);
        TemplateResource tr = new TemplateResource(r);
        GeneratorTemplate gt = new GeneratorTemplate();
        GeneratorTemplateFactory gtf = new GeneratorTemplateFactoryImpl();
        GeneratorTemplateFactory gtfSpy = Mockito.spy(gtf);
        Mockito.doReturn(gt).when(gtfSpy).loadGeneratorTemplate(Mockito.eq(tr));
        Mockito.doReturn(List.of(tr)).when(gtfSpy).listAvailableTemplateResources();

        // when
        List<GeneratorTemplate> generatorTemplates = gtfSpy.loadAllGeneratorTemplates();

        // then
        Assert.assertNotNull(generatorTemplates);
        Assert.assertEquals(generatorTemplates.size(), 1);
        Assert.assertTrue(generatorTemplates.contains(gt));
        Mockito.verify(gtfSpy, Mockito.times(1)).listAvailableTemplateResources();
        Mockito.verify(gtfSpy, Mockito.times(1)).loadGeneratorTemplate(Mockito.any());
        Mockito.verify(gtfSpy, Mockito.times(1)).loadAllGeneratorTemplates();
        Mockito.verifyNoMoreInteractions(gtfSpy);
    }

}