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
 * <p>Test class for {@link ConnectedGraphAnalyzer}</p>
 *
 * @author Andreas Pointner
 */

public class ConnectedGraphAnalyzerTest {

    @Test
    public void testAnalyzeGraphState() {
        // given
        ConnectedGraphAnalyzer<String, Void> connectedGraphAnalyzer = new ConnectedGraphAnalyzer<>();
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                .from("1").to("2")
                .from("2").to("3")
                .from("3").to("1")
                .getGraphState();

        // when
        Boolean aBoolean = connectedGraphAnalyzer.analyzeGraphState(graphState);

        // then
        Assert.assertTrue(aBoolean);
    }

    @Test
    public void testAnalyzeGraphState2() {
        // given
        ConnectedGraphAnalyzer<String, Void> connectedGraphAnalyzer = new ConnectedGraphAnalyzer<>();
        GraphStateAccessor<String, Void> graphState = GraphBuilderImpl.<String, Void>create()
                // subgraph 1
                .from("1").to("2")
                .from("2").to("3")
                .from("3").to("1")
                // subgraph 2
                .from("4").to("5")
                .from("5").to("6")
                .getGraphState();

        // when
        Boolean aBoolean = connectedGraphAnalyzer.analyzeGraphState(graphState);

        // then
        Assert.assertFalse(aBoolean);
    }
}