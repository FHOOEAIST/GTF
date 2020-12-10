/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.template.impl;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.builder.impl.GraphBuilderImpl;
import science.aist.gtf.graph.impl.traversal.DepthFirstSearchTraversalStrategy;
import science.aist.gtf.template.exception.TemplateEngineException;

import java.io.StringWriter;
import java.util.Map;

/**
 * <p>Test class for {@link FreemarkerTemplateEngine}</p>
 *
 * @author Andreas Pointner
 */

public class FreemarkerTemplateEngineTest {

    @Test
    public void testProcess() throws TemplateEngineException {
        // given
        Graph<String, Void> graph = GraphBuilderImpl.<String, Void>create()
                .from("A").to("B")
                .from("A").to("C")
                .from("C").to("D")
                .toGraph();
        graph.setVertexTraversalStrategy(new DepthFirstSearchTraversalStrategy<>(graph));
        FreemarkerTemplateEngine fte = new FreemarkerTemplateEngine();
        StringWriter sw = new StringWriter();

        // when
        fte.process(Map.of("graph", graph), "classpath:/templates/graphWiz.ftl", sw);

        // then
        String result = sw.toString();
        Assert.assertTrue(result.contains("digraph G"));
        Assert.assertTrue(result.contains("D[label=\"D\"];"));
        Assert.assertTrue(result.contains("B[label=\"B\"];"));
        Assert.assertTrue(result.contains("C[label=\"C\"];"));
        Assert.assertTrue(result.contains("A[label=\"A\"];"));
        Assert.assertTrue(result.contains("A->B;"));
        Assert.assertTrue(result.contains("C->D;"));
    }
}
