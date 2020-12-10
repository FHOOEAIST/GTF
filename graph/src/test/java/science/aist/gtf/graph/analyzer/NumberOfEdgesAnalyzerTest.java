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
 * <p>Test class for {@link NumberOfEdgesAnalyzer}</p>
 *
 * @author Andreas Pointner
 */

public class NumberOfEdgesAnalyzerTest {

    @Test
    public void testAnalyzeGraphState() {
        // given
        NumberOfEdgesAnalyzer<String, Void> numberOfEdgesAnalyzer = new NumberOfEdgesAnalyzer<>();
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                .from("1").to("2")
                .from("2").to("3")
                .from("3").to("1")
                .getGraphState();

        // when
        Integer integer = numberOfEdgesAnalyzer.analyzeGraphState(graphState);

        // then
        Assert.assertEquals(integer.intValue(), 3);
    }

    @Test
    public void testAnalyzeGraphState2() {
        // given
        NumberOfEdgesAnalyzer<String, Void> numberOfEdgesAnalyzer = new NumberOfEdgesAnalyzer<>();
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                .from("1").to("2")
                .from("2").to("3")
                .from("3").to("1")
                // this edge already exists, and should not be added anymore
                .from("1").to("2")
                .getGraphState();

        // when
        Integer integer = numberOfEdgesAnalyzer.analyzeGraphState(graphState);

        // then
        Assert.assertEquals(integer.intValue(), 3);
    }
}