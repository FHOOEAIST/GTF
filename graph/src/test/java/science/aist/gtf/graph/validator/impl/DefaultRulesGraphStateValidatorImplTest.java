/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph.validator.impl;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.GraphState;
import science.aist.gtf.graph.impl.EdgeImpl;
import science.aist.gtf.graph.impl.GraphStateImpl;
import science.aist.gtf.graph.impl.VertexImpl;

import java.util.stream.Stream;

/**
 * <p>Test class for {@link DefaultRulesGraphStateValidatorImpl}</p>
 *
 * @author Andreas Pointner
 */

public class DefaultRulesGraphStateValidatorImplTest {

    @Test
    public void testIsValidGraphStateTrue() {
        // given
        DefaultRulesGraphStateValidatorImpl<Void, Void> validator = new DefaultRulesGraphStateValidatorImpl<>();
        GraphState<Void, Void> graphState = new GraphStateImpl<>();
        VertexImpl<Void, Void> v1 = new VertexImpl<>(null);
        VertexImpl<Void, Void> v2 = new VertexImpl<>(null);
        EdgeImpl<Void, Void> e = new EdgeImpl<>(null);
        Stream.of(v1, v2).forEach(graphState::addVertex);
        graphState.addEdge(e, v1, v2);

        // when
        boolean validGraphState = validator.isValidGraphState(graphState);

        // then
        Assert.assertTrue(validGraphState);
    }

    @Test
    public void testIsValidGraphStateFalse() {
        // given
        DefaultRulesGraphStateValidatorImpl<Void, Void> validator = new DefaultRulesGraphStateValidatorImpl<>();
        GraphState<Void, Void> graphState = new GraphStateImpl<>();

        // when
        boolean validGraphState = validator.isValidGraphState(graphState);

        // then
        Assert.assertFalse(validGraphState);
    }
}