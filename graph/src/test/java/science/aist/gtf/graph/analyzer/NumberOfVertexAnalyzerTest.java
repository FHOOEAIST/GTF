/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.analyzer;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.GraphStateAccessor;
import science.aist.gtf.graph.builder.impl.GraphBuilderImpl;

/**
 * <p>Test class for {@link NumberOfVertexAnalyzer}</p>
 *
 * @author Andreas Pointner
 */

public class NumberOfVertexAnalyzerTest {

    @Test
    public void testAnalyzeGraphState() {
        // given
        NumberOfVertexAnalyzer<String, Void> numberOfVertexAnalyzer = new NumberOfVertexAnalyzer<>();
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                .addVertex("123")
                .getGraphState();

        // when
        Integer integer = numberOfVertexAnalyzer.analyzeGraphState(graphState);

        // then
        Assert.assertEquals(integer.intValue(), 1);
    }

    @Test
    public void testAnalyzeGraphState2() {
        // given
        NumberOfVertexAnalyzer<String, Void> numberOfVertexAnalyzer = new NumberOfVertexAnalyzer<>();
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                .addVertex("123")
                .addVertex("456")
                .addVertex("789")
                .addVertex("123")
                .getGraphState();

        // when
        Integer integer = numberOfVertexAnalyzer.analyzeGraphState(graphState);

        // then
        Assert.assertEquals(integer.intValue(), 3);
    }
}