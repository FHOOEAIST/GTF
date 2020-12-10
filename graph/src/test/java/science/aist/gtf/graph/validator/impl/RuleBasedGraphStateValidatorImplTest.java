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

/**
 * <p>Test class for {@link RuleBasedGraphStateValidatorImpl}</p>
 *
 * @author Andreas Pointner
 */

public class RuleBasedGraphStateValidatorImplTest {

    @Test
    public void testRuleBasedGraphValidation1() {
        // given
        RuleBasedGraphStateValidatorImpl<Void, Void> ruleBasedGraphStateValidator = new RuleBasedGraphStateValidatorImpl<>();

        // when
        // If there are no rules it succeeds all rules
        boolean validGraphState = ruleBasedGraphStateValidator.isValidGraphState(null);

        // then
        Assert.assertTrue(validGraphState);
    }

    @Test
    public void testRuleBasedGraphValidation2() {
        // given
        RuleBasedGraphStateValidatorImpl<Void, Void> ruleBasedGraphStateValidator = new RuleBasedGraphStateValidatorImpl<>();
        // A single rule where the analyzer returns true, and the rule states, that the analyzer result has to be true
        ruleBasedGraphStateValidator.addAnalyzerRule(
                x -> true,
                Boolean.TRUE::equals
        );

        // when
        boolean validGraphState = ruleBasedGraphStateValidator.isValidGraphState(null);

        // then
        Assert.assertTrue(validGraphState);
    }

    @Test
    public void testRuleBasedGraphValidation3() {
        // given
        RuleBasedGraphStateValidatorImpl<Void, Void> ruleBasedGraphStateValidator = new RuleBasedGraphStateValidatorImpl<>();
        // A single rule where the analyzer returns 0, but the rule states, that the value must be bigger than 0
        ruleBasedGraphStateValidator.addAnalyzerRule(
                x -> 0,
                x -> x > 0
        );

        // when
        boolean validGraphState = ruleBasedGraphStateValidator.isValidGraphState(null);

        // then
        Assert.assertFalse(validGraphState);
    }

    @Test
    public void testRuleBasedGraphValidation4() {
        // given
        RuleBasedGraphStateValidatorImpl<Void, Void> ruleBasedGraphStateValidator = new RuleBasedGraphStateValidatorImpl<>();
        // Two rules, where one fails the other one success, the final result should therefore fail.
        ruleBasedGraphStateValidator.addAnalyzerRule(
                x -> 0,
                x -> x > 0
        );
        ruleBasedGraphStateValidator.addAnalyzerRule(
                x -> true,
                Boolean.TRUE::equals
        );

        // when
        boolean validGraphState = ruleBasedGraphStateValidator.isValidGraphState(null);

        // then
        Assert.assertFalse(validGraphState);
    }


}