/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.transformation;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

/**
 * <p>Tests {@link Transformer}. As this is an interface, it tests the default implemented methods.</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class TransformerTest {
    @Test
    public void testVerificateBefore() {
        // given
        Transformer<Integer, Double> transformer = input -> input + 1.0;

        // when
        Transformer<Integer, Double> transformer2 = transformer.verificateBefore((input) -> input == 1.0);
        double res = transformer2.applyTransformation(1);

        // then
        // if there is no exception everything works fine.
        Assert.assertNotNull(transformer2);
        Assert.assertEquals(res, 2.0);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testVerificateBeforeException() {
        // given
        Transformer<Integer, Double> transformer = input -> input + 1.0;

        // when
        Transformer<Integer, Double> transformer2 = transformer.verificateBefore(input -> input == 1.0);
        transformer2.applyTransformation(2);

        // then
        // exception expected
    }

    @Test
    public void testVerificateAfter() {
        // given
        Transformer<Integer, Double> transformer = input -> input + 1.0;

        // when
        Transformer<Integer, Double> transformer2 = transformer.verificateAfter((input, output) -> output == input + 1.0);
        double res = transformer2.applyTransformation(1);

        // then
        Assert.assertNotNull(transformer2);
        Assert.assertEquals(res, 2.0);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testVerificateAfterException() {
        // given
        Transformer<Integer, Double> transformer = input -> input + 1.0;

        // when
        Transformer<Integer, Double> transformer2 = transformer.verificateAfter((input, output) -> false);
        transformer2.applyTransformation(2);

        // then
        // exception expected
    }

    @Test
    public void testAndThen() {
        // given
        Transformer<Integer, Double> transformer = input -> input + 1.0;
        Transformer<Double, Double> transformer2 = input -> input * 3.0;
        Transformer<Double, String> transformer3 = Objects::toString;

        // when
        String res = transformer.andThen(transformer2).andThen(transformer3).applyTransformation(1);

        // then
        Assert.assertNotNull(res);
        Assert.assertEquals(res, "6.0");
    }

    @Test
    public void testAndThenConsumeResult() {
        // given
        Transformer<Integer, Double> transformer = input -> input + 1.0;
        Transformer<Double, Double> transformer2 = input -> input * 3.0;
        Transformer<Double, String> transformer3 = Objects::toString;

        // when
        String res = transformer
                .andThenConsumeResult((integer, aDouble) -> {
                    Assert.assertEquals(integer, Integer.valueOf(1));
                    Assert.assertEquals(aDouble, 2.0);
                })
                .andThen(transformer2.andThenConsumeResult((aDouble, aDouble2) -> {
                    Assert.assertEquals(aDouble, 2.0);
                    Assert.assertEquals(aDouble2, 6.0);
                }))
                .andThen(transformer3.andThenConsumeResult((aDouble, s) -> {
                    Assert.assertEquals(aDouble, 6.0);
                    Assert.assertEquals(s, "6.0");
                }))
                .applyTransformation(1);

        // then
        Assert.assertNotNull(res);
        Assert.assertEquals(res, "6.0");
    }
}
