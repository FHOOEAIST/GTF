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
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.reporters.Files;
import science.aist.gtf.template.*;
import science.aist.gtf.template.impl.renderer.DeleteFileRenderer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * <p>Tests {@link DeleteFileRenderer}</p>
 *
 * @author Andreas Pointner
 */
@ContextConfiguration("classpath:template-test-config.xml")
public class DeleteFileRendererTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("deleteFileRenderer")
    private GeneratorTemplateRenderer<? extends TemplateTaskResult, TemplateTask> deleteFileRenderer;

    @Test
    public void testDeleteFileSuccess() throws IOException {
        // given
        String target = System.getProperty("user.dir") + "/target/";
        String filename = "sample.txt";
        File file = new File(target + filename);
        Files.writeFile("nobody cares what's in this file ...", file);
        // Assert that the file exists here, so that we know that writing of our test file was successful
        Assert.assertTrue(file.exists());
        TemplateTask tt = new TemplateTask();
        tt.setType(TemplateTaskTypeEnum.DELETE);
        tt.setName("");
        Properties ttProps = new Properties();
        ttProps.setProperty("path", "${target}/" + filename);
        tt.setProperties(ttProps);
        GeneratorTemplate gt = new GeneratorTemplate();
        gt.setTemplateTasks(List.of(tt));
        gt.setTemplateName("");
        Properties p = new Properties();
        p.setProperty("target", target);
        gt.setProperties(p);

        // when
        final Optional<? extends TemplateTaskResult> templateTaskResult = deleteFileRenderer.renderElement(gt, tt);

        // then
        Assert.assertTrue(templateTaskResult.isPresent());
        Assert.assertEquals(templateTaskResult.get().getTemplateTaskResultTypeEnum(), TemplateTaskResultTypeEnum.SUCCESS);
        Assert.assertFalse(file.exists());
    }

    @Test
    public void testDeleteFileFail() throws IOException {
        // given
        String target = System.getProperty("user.dir") + "/target/";
        String filename = "sample.txt";
        File file = new File(target + filename);
        Files.writeFile("nobody cares what's in this file ...", file);
        // Assert that the file exists here, so that we know that writing of our test file was successful
        Assert.assertTrue(file.exists());
        TemplateTask tt = new TemplateTask();
        tt.setType(TemplateTaskTypeEnum.DELETE);
        tt.setName("");
        Properties ttProps = new Properties();
        ttProps.setProperty("path", "${target}/not_existing_file");
        tt.setProperties(ttProps);
        GeneratorTemplate gt = new GeneratorTemplate();
        gt.setTemplateTasks(List.of(tt));
        gt.setTemplateName("");
        Properties p = new Properties();
        p.setProperty("target", target);
        gt.setProperties(p);

        // when
        final Optional<? extends TemplateTaskResult> templateTaskResult = deleteFileRenderer.renderElement(gt, tt);

        // then
        Assert.assertTrue(templateTaskResult.isPresent());
        Assert.assertEquals(templateTaskResult.get().getTemplateTaskResultTypeEnum(), TemplateTaskResultTypeEnum.FAILED);
        Assert.assertTrue(file.exists()); // didn't delete the file because wrong path was provided
    }
}
