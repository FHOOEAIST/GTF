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
import org.testng.reporters.Files;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.builder.impl.GraphBuilderImpl;
import science.aist.gtf.graph.impl.traversal.DepthFirstSearchTraversalStrategy;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>Full Example Tests. Transforming a graph into a graphwiz representation</p>
 *
 * @author Andreas Pointner
 */
@ContextConfiguration("classpath:template-test-config.xml")
public class GraphWizSampleTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier(DefaultTemplateResources.GRAPH_WIZ_TEMPLATE)
    private TemplateResource templateResource;

    @Autowired
    private GeneratorTemplateFactory generatorTemplateFactory;

    @Autowired
    private GeneratorTemplateRenderer<GeneratorTemplate, Void> generatorTemplateRenderer;

    @Test
    public void testTemplateExecutionWithGraphWiz() throws IOException {
        // given
        GeneratorTemplate template = generatorTemplateFactory.loadGeneratorTemplate(templateResource);
        Graph<String, Void> graph = GraphBuilderImpl.<String, Void>create()
                .from("A").to("B")
                .from("A").to("C")
                .from("C").to("D")
                .toGraph();
        graph.setVertexTraversalStrategy(new DepthFirstSearchTraversalStrategy<>(graph));
        Properties properties = new Properties();
        properties.put("graph", graph);
        properties.setProperty("target", "target");
        template.setProperties(properties);

        // when
        generatorTemplateRenderer.renderElement(template, null);

        // then
        final String result = Files.readFile(new File("target/result.graphWiz"));
        Assert.assertTrue(result.contains("digraph G"));
        Assert.assertTrue(result.contains("D[label=\"D\"];"));
        Assert.assertTrue(result.contains("B[label=\"B\"];"));
        Assert.assertTrue(result.contains("C[label=\"C\"];"));
        Assert.assertTrue(result.contains("A[label=\"A\"];"));
        Assert.assertTrue(result.contains("A->B;"));
        Assert.assertTrue(result.contains("C->D;"));
    }
}
