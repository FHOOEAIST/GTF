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
import science.aist.gtf.template.GeneratorTemplate;
import science.aist.gtf.template.GeneratorTemplateFactory;
import science.aist.gtf.template.GeneratorTemplateRenderer;
import science.aist.gtf.template.TemplateResource;
import science.aist.gtf.template.impl.renderer.GraphVizImageRenderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>Test class for {@link GraphVizImageRenderer}</p>
 *
 * @author Andreas Pointner
 */
@ContextConfiguration("classpath:template-test-config.xml")
public class GraphVizImageRendererTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("graphVizRendering")
    private TemplateResource templateResource;

    @Autowired
    private GeneratorTemplateFactory generatorTemplateFactory;

    @Autowired
    private GeneratorTemplateRenderer<GeneratorTemplate, Void> generatorTemplateRenderer;

    @Test
    public void testPipeline() throws IOException {
        // given
        String basePath = System.getProperty("user.dir") + "/target/";
        String graphWiz = "digraph G {\n" +
                "\n" +
                "compound=true;\n" +
                "rankdir=LR;\n" +
                "\n" +
                "start1[ label=\"\", shape=circle, width=0.5, height=0.5 ];\n" +
                "ad_2[ label=\"Task 2\", shape=rect, style=rounded, width=1.75, height=1, fixedsize=true, fontname=\"Courier\" ];\n" +
                "end2[ label=\"\", shape=circle, width=0.5, height=0.5, penwidth=5 ];\n" +
                "ad_3[ label=\"Task 3\", shape=rect, style=rounded, width=1.75, height=1, fixedsize=true, fontname=\"Courier\" ];\n" +
                "ad_1[ label=\"Task 1\", shape=rect, style=rounded, width=1.75, height=1, fixedsize=true, fontname=\"Courier\" ];\n" +
                "\n" +
                "start1 -> ad_1 ;\n" +
                "ad_2 -> ad_3 ;\n" +
                "ad_3 -> end2 ;\n" +
                "ad_1 -> ad_2 ;\n" +
                "}";
        try (FileWriter fw = new FileWriter(basePath + "/sample.viz")) {
            fw.write(graphWiz);
        }
        Properties p = new Properties();
        p.setProperty("target", basePath);
        GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);
        template.setProperties(p);

        // when
        generatorTemplateRenderer.renderElement(template, null);

        // then
        Assert.assertTrue(new File(basePath + "/sample.png").exists());
    }

}
